package org.errorzhu.zsql.engine.spark;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.errorzhu.zsql.common.data.DataSource;
import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.engine.IEngine;
import org.errorzhu.zsql.engine.constant.TemplateConstant;
import org.errorzhu.zsql.engine.template.TemplateEngine;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.StringJoiner;

public class ZSqlSparkEngine implements IEngine {

    private TemplateEngine templateEngine = TemplateEngine.getInstance();

    public ZSqlSparkEngine() throws IOException {
    }


    @Override
    public String getCode(String sql, DataSources dataSources, DataSources dataTargets) throws IOException, TemplateException {
        Map<String, Object> mainVariables = Maps.newHashMap();
        Template template = templateEngine.getTemplate("spark/import.ftl");
        String importCode = templateEngine.render(template, ImmutableMap.of());
        mainVariables.put(TemplateConstant.IMPORT, importCode);
        Map<String, DataSource> sources = dataSources.getSources();
        Map<String, DataSource> targets = dataTargets.getSources();
        StringBuilder tableCode = new StringBuilder();
        StringBuilder dmlCode = new StringBuilder();

        for (String index : sources.keySet()) {
            DataSource source = sources.get(index);
            switch (source.getType()) {
                case "csv":
                    genCsvCodePhase(tableCode, index, source);
                    break;
                case "mysql":
                    genJdbcCodePhase(tableCode, index, source);
                    break;
            }
        }

        for (String index : targets.keySet()) {
            DataSource target = targets.get(index);
            switch (target.getType()) {
                case "csv":
                    genCsvCodeDmlPhase(dmlCode, target);
                    break;
                case "mysql":
                    genJdbcCodeDmlPhase(dmlCode, index, target);
                    break;
            }
        }
        mainVariables.put(TemplateConstant.TABLE_CODE, tableCode.toString());


        mainVariables.put(TemplateConstant.SQL, sql);
        mainVariables.put(TemplateConstant.DML_CODE, dmlCode.toString());


        Template main = templateEngine.getTemplate("spark/main.ftl");
        String mainCode = templateEngine.render(main, mainVariables);


        return mainCode;
    }


    private void genCsvCodeDmlPhase(StringBuilder dmlCode, DataSource target) throws IOException, TemplateException {
        Template csvTemplate = templateEngine.getTemplate("spark/csv_insert.ftl");
        String directory = target.getParams().get("directory").toString();
        String file = Paths.get(directory, target.getTableName()).toString();
        String csvCode = templateEngine.render(csvTemplate, ImmutableMap.of(TemplateConstant.CSV_FILE, file));
        dmlCode.append(csvCode);
    }

    private void genJdbcCodeDmlPhase(StringBuilder dmlCode, String index, DataSource target) throws IOException, TemplateException {
        Template jdbcTempate = templateEngine.getTemplate("spark/jdbc_insert.ftl");
        String user = target.getParams().get("jdbcUser").toString();
        String password = target.getParams().get("jdbcPassword").toString();
        String url = target.getParams().get("jdbcUrl").toString();
        String driver = target.getParams().get("jdbcDriver").toString();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("jdbc_table", index.split("\\.")[1]);
        variables.put("jdbc_user", user);
        variables.put("jdbc_password", password);
        variables.put("jdbc_url", url);
        variables.put("jdbc_driver", driver);
        String tempTable = getTempTableName(index);
        String jdbc = templateEngine.render(jdbcTempate, variables);
        dmlCode.append(jdbc);
    }


    private void genCsvCodePhase(StringBuilder tableCode, String index, DataSource source) throws IOException, TemplateException {
        StringJoiner joiner = new StringJoiner(",");
        Template csvTemplate = templateEngine.getTemplate("spark/csv.ftl");
        String directory = source.getParams().get("directory").toString();
        String file = Paths.get(directory, source.getTableName() + ".csv").toString();
        String tempTable = getTempTableName(index);
        source.getColumns().stream().forEach(x -> joiner.add("\"" + x.getColumnName() + "\""));
        String csvCode = templateEngine.render(csvTemplate, ImmutableMap.of(TemplateConstant.FIELDS, joiner.toString(), TemplateConstant.CSV_FILE, file, TemplateConstant.TEMP_TABLE, tempTable, TemplateConstant.DF_NAME, "df_" + tempTable));
        tableCode.append(csvCode);
    }

    private String getTempTableName(String tableName) {
        return tableName.replace(".", "_") + "_temp";
    }

    private void genJdbcCodePhase(StringBuilder sb, String index, DataSource source) throws IOException, TemplateException {
        Template jdbcTempate = templateEngine.getTemplate("spark/jdbc.ftl");
        String user = source.getParams().get("jdbcUser").toString();
        String password = source.getParams().get("jdbcPassword").toString();
        String url = source.getParams().get("jdbcUrl").toString();
        String driver = source.getParams().get("jdbcDriver").toString();
        Map<String, Object> variables = Maps.newHashMap();
        variables.put("jdbc_table", index.split("\\.")[1]);
        variables.put("jdbc_user", user);
        variables.put("jdbc_password", password);
        variables.put("jdbc_url", url);
        variables.put("jdbc_driver", driver);
        String tempTable = getTempTableName(index);
        variables.put(TemplateConstant.TEMP_TABLE, tempTable);
        variables.put(TemplateConstant.DF_NAME, "df_" + tempTable);
        String jdbc = templateEngine.render(jdbcTempate, variables);
        sb.append(jdbc);
    }
}
