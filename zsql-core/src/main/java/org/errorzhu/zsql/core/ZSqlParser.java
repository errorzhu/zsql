package org.errorzhu.zsql.core;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.schema.SchemaPlus;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.tools.FrameworkConfig;
import org.apache.calcite.tools.Frameworks;
import org.apache.calcite.tools.Planner;
import org.errorzhu.zsql.core.result.ResultWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class ZSqlParser {

    private Logger logger = LoggerFactory.getLogger(ZSqlParser.class);
    private ResultSet rs = null;
    private Statement statement = null;
    private CalciteConnection connection = null;



    public void parseAndRun(String sql,String model) {
        try {
            Properties config = new Properties();
            config.put("model", model);
            config.put("lex", "MYSQL");
            connection = (CalciteConnection) DriverManager.getConnection("jdbc:calcite:", config);
            statement = connection.createStatement();
            rs = statement.executeQuery(sql);
            ResultWrapper resultWrapper = new ResultWrapper(rs);
            resultWrapper.print();

        } catch (Exception e) {
            logger.error("run sql failure", e);

        } finally {
            close();
        }

    }

    public RelNode parse(String sql,String model) {
        try {
            Properties config = new Properties();
            config.put("model", model);
            config.put("lex", "MYSQL");
            CalciteConnection connection = (CalciteConnection) DriverManager.getConnection("jdbc:calcite:", config);
            SchemaPlus rootSchema = connection.getRootSchema();
            Frameworks.ConfigBuilder builder = Frameworks.newConfigBuilder().defaultSchema(rootSchema).parserConfig(SqlParser.config().withParserFactory(SqlDdlParserImpl.FACTORY).withCaseSensitive(false));
            FrameworkConfig plannerConfig = builder.build();
            Planner planner = Frameworks.getPlanner(plannerConfig);
            SqlNode root = planner.parse(sql);
            SqlNode validate = planner.validate(root);
            RelNode relNode = planner.rel(validate).project();
            return relNode;
        } catch (Exception e) {
            logger.error("parse sql failure", e);
        } finally {
            close();
        }
        return null;
    }

    private void close() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException throwables) {
        }
    }
}








