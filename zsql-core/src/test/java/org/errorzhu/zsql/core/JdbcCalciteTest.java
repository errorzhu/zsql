package org.errorzhu.zsql.core;

import org.apache.calcite.jdbc.CalciteConnection;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcCalciteTest {
    public static void main(String[] args) throws SQLException {
        Properties config = new Properties();
        config.put("model", "mix.json");
        config.put("lex", "MYSQL");
        CalciteConnection connection = (CalciteConnection) DriverManager.getConnection("jdbc:calcite:", config);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("select * from csv.depts d join csv.sdepts s on d.deptno = s.deptno where d.name='Engineering'");
        while (rs.next()){
            System.out.println(rs.getObject(1));
        }

    }
}
