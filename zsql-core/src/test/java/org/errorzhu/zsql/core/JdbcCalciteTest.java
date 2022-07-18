package org.errorzhu.zsql.core;

import com.google.common.io.CharStreams;
import org.apache.calcite.jdbc.CalciteConnection;
import org.errorzhu.zsql.core.result.ResultWrapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Properties;

public class JdbcCalciteTest {
    public static void main(String[] args) throws SQLException, IOException {

        String dbPath = ZSqlPlannerTest.class.getClassLoader().getResource("db").getPath();

        String model = CharStreams.toString(new InputStreamReader(Objects.requireNonNull(ZSqlPlannerTest.class.getClassLoader().getResourceAsStream("mix.json"))));

        model = model.replace("@db@", dbPath.substring(1)+"/example");
        model = model.replace("@dir@", dbPath.substring(1,dbPath.indexOf("db")));

        System.out.println(model);

        Properties config = new Properties();
        config.put("model", "inline: "+model);
        config.put("lex", "MYSQL");
        CalciteConnection connection = (CalciteConnection) DriverManager.getConnection("jdbc:calcite:", config);


        ResultSet schemas = connection.getMetaData().getSchemas();

        ResultSet catalogs = connection.getMetaData().getCatalogs();


        ResultSet tables = connection.getMetaData().getTables(null, "%", "%", null);

        ResultWrapper wrapper = new ResultWrapper(tables);
        wrapper.print();

        ResultWrapper wrapper1 = new ResultWrapper(catalogs);
        wrapper1.print();

        Statement statement = connection.createStatement();

        ResultSet rs = statement.executeQuery("select * from h2.test s join CSV.depts c on s.name = c.deptno ");
        ResultWrapper wrapper2 = new ResultWrapper(rs);
        wrapper2.print();

    }
}
