package org.errorzhu.zsql.core;

import com.google.common.io.CharStreams;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.plan.hep.HepProgramBuilder;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.rules.FilterJoinRule;
import org.apache.calcite.rel.rules.ProjectMergeRule;
import org.apache.calcite.rel.rules.SubQueryRemoveRule;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.errorzhu.zsql.core.data.DataSourceExtractor;
import org.errorzhu.zsql.common.data.DataSources;

import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.util.Objects;
import java.util.Properties;

public class DataSourceExtractorTest {


    public static void main(String[] args) throws Exception {


        Properties config = new Properties();
        config.put("model", "mix.json");
        config.put("lex", "MYSQL");
        CalciteConnection connection = (CalciteConnection) DriverManager.getConnection("jdbc:calcite:", config);
        SchemaPlus rootSchema = connection.getRootSchema();
        String sql = "select * from csv.depts d join mysql.influence_factor_data i  on d.DEPTNO = i.org_code where i.org_code = 'E01'";
        Frameworks.ConfigBuilder builder = Frameworks.newConfigBuilder().defaultSchema(rootSchema).parserConfig(SqlParser.config().withParserFactory(SqlDdlParserImpl.FACTORY).withCaseSensitive(false));
        FrameworkConfig plannerConfig = builder.build();
        Planner planner = Frameworks.getPlanner(plannerConfig);
        SqlNode root = planner.parse(sql);

        SqlNode validate = planner.validate(root);
        RelNode relNode = planner.rel(validate).project();

        System.out.println("-----------------");
//        System.out.println(relNode.explain());


        HepProgram program = new HepProgramBuilder()
                .addRuleInstance(ProjectMergeRule.Config.DEFAULT.toRule())
                .addRuleInstance(SubQueryRemoveRule.Config.PROJECT.toRule())
                .addRuleInstance(SubQueryRemoveRule.Config.FILTER.toRule())
                .addRuleInstance(SubQueryRemoveRule.Config.JOIN.toRule())
                .addRuleInstance(FilterJoinRule.JoinConditionPushRule.JoinConditionPushRuleConfig.DEFAULT.toRule())
                .addRuleInstance(FilterJoinRule.FilterIntoJoinRule.FilterIntoJoinRuleConfig.DEFAULT.toRule())
                .build();

        HepPlanner prePlanner = new HepPlanner(program);
        prePlanner.setRoot(relNode);


        RelNode optimisedRelNode = prePlanner.findBestExp();

        DataSourceExtractor extractor2 = new DataSourceExtractor(optimisedRelNode);
        DataSources dataSources = extractor2.extract();
    }




}
