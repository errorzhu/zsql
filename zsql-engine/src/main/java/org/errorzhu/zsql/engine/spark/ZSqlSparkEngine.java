package org.errorzhu.zsql.engine.spark;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.errorzhu.zsql.common.data.DataSource;
import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.engine.IEngine;
import org.errorzhu.zsql.engine.template.TemplateEngine;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ZSqlSparkEngine implements IEngine {

    private TemplateEngine templateEngine = TemplateEngine.getInstance();

    public ZSqlSparkEngine() throws IOException {
    }


    @Override
    public String getCode(String sql, DataSources dataSources) throws IOException, TemplateException {
        Map<String, Object> mainVariables = Maps.newHashMap();
        Template template = templateEngine.getTemplate("spark/import.ftl");
        String importCode = templateEngine.render(template, ImmutableMap.of());
        mainVariables.put("import", importCode);
        Map<String, DataSource> sources = dataSources.getSources();
        StringBuilder sb = new StringBuilder();

        for (String index : sources.keySet()) {
            DataSource source = sources.get(index);
            switch (source.getType()) {
                case "csv":
                    genCsvCodePhase(sb, index, source);
                    break;
                case "mysql":
                    genJdbcCodePhase(sb, index, source);
                    break;
            }

        }
        mainVariables.put("table_code", sb.toString());


        mainVariables.put("sql", sql);


        Template main = templateEngine.getTemplate("spark/main.ftl");
        String mainCode = templateEngine.render(main, mainVariables);


        return mainCode;
    }

    private void genCsvCodePhase(StringBuilder sb, String index, DataSource source) throws IOException, TemplateException {
        StringJoiner joiner = new StringJoiner(",");
        Template csvTemplate = templateEngine.getTemplate("spark/csv.ftl");
        String directory = source.getParams().get("directory").toString();
        String file = Paths.get(directory, source.getTableName() + ".csv").toString();
        String tempTable = index.replace(".", "_") + "_temp";
        source.getColumns().stream().forEach(x -> joiner.add("\"" + x.getColumnName() + "\""));
        String csvCode = templateEngine.render(csvTemplate, ImmutableMap.of("fields", joiner.toString(), "csv_file", file, "temp_table", tempTable, "df_name", "df_" + tempTable));
        sb.append(csvCode);
    }

    private void genJdbcCodePhase(StringBuilder sb, String index, DataSource source) throws IOException, TemplateException {
        Template jdbcTempate = templateEngine.getTemplate("spark/jdbc.ftl");
        String user = source.getParams().get("jdbcUser").toString();
        String password = source.getParams().get("jdbcPassword").toString();
        String url = source.getParams().get("jdbcUrl").toString();
        String driver = source.getParams().get("jdbcDriver").toString();
        HashMap<String, Object> variables = Maps.newHashMap();
        variables.put("jdbc_table", index.split("\\.")[1]);
        variables.put("jdbc_user", user);
        variables.put("jdbc_password", password);
        variables.put("jdbc_url", url);
        variables.put("jdbc_driver", driver);
        String tempTable = index.replace(".", "_") + "_temp";
        variables.put("temp_table", tempTable);
        variables.put("df_name", "df_" + tempTable);
        String jdbc = templateEngine.render(jdbcTempate, variables);
        sb.append(jdbc);
    }
}
