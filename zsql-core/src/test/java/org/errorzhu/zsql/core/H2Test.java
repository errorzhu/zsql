package org.errorzhu.zsql.core;

import org.errorzhu.zsql.core.result.ResultWrapper;

import java.sql.*;

public class H2Test {


    public static void main(String[] args) throws ClassNotFoundException, SQLException {


        Class.forName("org.h2.Driver");

        String dbPath = ZSqlPlannerTest.class.getClassLoader().getResource("db").getPath();
        dbPath=dbPath.substring(1);
        System.out.println(dbPath);


        Connection connection = DriverManager.getConnection("jdbc:h2:" + dbPath, "sa", "a");
        Statement statement = connection.createStatement();


        ResultSet schemas = connection.getMetaData().getSchemas();

        ResultSet catalogs = connection.getMetaData().getCatalogs();

        ResultSet tables = connection.getMetaData().getTables("TEST", "PUBLIC", "%", null);

//        ResultSet resultSet = statement.executeQuery("select * from TEST");
        ResultWrapper wrapper = new ResultWrapper(schemas);
        wrapper.print();

        ResultWrapper wrapper1 = new ResultWrapper(catalogs);
        wrapper1.print();

        ResultWrapper wrapper2 = new ResultWrapper(tables);
        wrapper2.print();
    }

}
