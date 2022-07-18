package org.errorzhu.zsql.core;

import org.errorzhu.zsql.core.result.ResultWrapper;

import java.sql.*;

public class SqliteTest {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("org.sqlite.JDBC");

        String dbPath = ZSqlPlannerTest.class.getClassLoader().getResource("db").getPath();
        dbPath=dbPath.substring(1);
        System.out.println(dbPath);


        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        Statement statement = connection.createStatement();
        ResultSet schemas = connection.getMetaData().getSchemas();

        ResultSet catalogs = connection.getMetaData().getCatalogs();

        ResultSet tables = connection.getMetaData().getTables(null, null, "%", null);

        ResultWrapper wrapper = new ResultWrapper(schemas);
        wrapper.print();

        ResultWrapper wrapper1 = new ResultWrapper(catalogs);
        wrapper1.print();

        ResultWrapper wrapper2 = new ResultWrapper(tables);
        wrapper2.print();


    }

}
