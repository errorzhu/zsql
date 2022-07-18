package org.errorzhu.zsql.core;

import com.google.common.io.CharStreams;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgram;
import org.apache.calcite.plan.hep.HepProgramBuilder;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.rel2sql.RelToSqlConverter;
import org.apache.calcite.rel.rules.FilterJoinRule;
import org.apache.calcite.rel.rules.ProjectMergeRule;
import org.apache.calcite.rel.rules.SubQueryRemoveRule;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.dialect.MysqlSqlDialect;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.tools.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

public class ZSqlPlannerTest {

    public static void main(String[] args) throws SqlParseException, ValidationException, RelConversionException, SQLException, IOException {


        String dbPath = ZSqlPlannerTest.class.getClassLoader().getResource("db").getPath();

        String model = CharStreams.toString(new InputStreamReader(Objects.requireNonNull(ZSqlPlannerTest.class.getClassLoader().getResourceAsStream("mix.json"))));

        model = model.replace("@db@", dbPath.substring(1));
        model = model.replace("@dir@", dbPath.substring(1,dbPath.indexOf("db")));

        System.out.println(model);


        Properties config = new Properties();
        config.put("model", "inline: " + model);
        config.put("lex", "MYSQL");
        CalciteConnection connection = (CalciteConnection) DriverManager.getConnection("jdbc:calcite:", config);
        SchemaPlus rootSchema = connection.getRootSchema();
//        String sql = "select * from CSV.depts d join mysql.test i  on d.DEPTNO = i.id where i.id = 'E01'";

        String sql = "select * from MYSQL.test";
        Frameworks.ConfigBuilder builder = Frameworks.newConfigBuilder().defaultSchema(rootSchema).parserConfig(SqlParser.config().withParserFactory(SqlDdlParserImpl.FACTORY).withCaseSensitive(false));
        FrameworkConfig plannerConfig = builder.build();
        Planner planner = Frameworks.getPlanner(plannerConfig);
        SqlNode root = planner.parse(sql);

        SqlNode validate = planner.validate(root);
        RelNode relNode = planner.rel(validate).project();

        System.out.println(relNode.explain());


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


        System.out.println(optimisedRelNode.explain());


        ResultSet resultSet = connection.createStatement().executeQuery("explain plan for " + sql);
        while (resultSet.next()) {
            System.out.println(resultSet.getObject(1));
        }


        System.out.println(optimisedRelNode.getInput(0).getInput(0).getInput(0).getInput(1).getRelTypeName());

        SqlNode sqlNode = new RelToSqlConverter(MysqlSqlDialect.DEFAULT)
                .visitInput(optimisedRelNode.getInput(0).getInput(0).getInput(0).getInput(1), 0)
                .asStatement();
        String sqlString = sqlNode.toSqlString(MysqlSqlDialect.DEFAULT).getSql();
        System.out.println("#################");
        System.out.println(sqlString);
        System.out.println("#################");
        PreparedStatement statement = RelRunners.run(optimisedRelNode);
        ResultSet resultSet1 = statement.executeQuery();
        while (resultSet1.next()) {
            System.out.println(resultSet1.getObject(1));
        }


    }

}
