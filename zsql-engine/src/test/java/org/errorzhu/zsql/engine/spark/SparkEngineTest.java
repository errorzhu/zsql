package org.errorzhu.zsql.engine.spark;

import com.google.common.collect.ImmutableMap;
import freemarker.template.TemplateException;
import org.errorzhu.zsql.common.data.DataSource;
import org.errorzhu.zsql.common.data.DataSources;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SparkEngineTest {
    @Test
    public void test_spark_csv_code() throws IOException, TemplateException {

        Map<String, DataSource> dataSources = new HashMap<>();
        DataSource source = new DataSource("csv", "depts", "csv");
        source.addColumn("id", "id", "string");
        source.addColumn("name", "name", "string");
        source.addParam("directory", "/opt/test");
        dataSources.put("csv.depts", source);
        DataSources sources = new DataSources(dataSources);
        DataSources targets = new DataSources(ImmutableMap.of());

        ZSqlSparkEngine engine = new ZSqlSparkEngine();
        String code = engine.getCode("select * from csv_depts_temp", sources, targets);


        String expect = "import org.apache.spark.sql.AnalysisException;\n" +
                "import org.apache.spark.sql.Dataset;\n" +
                "import org.apache.spark.sql.Row;\n" +
                "import org.apache.spark.sql.SparkSession;\n" +
                "import org.apache.spark.sql.SaveMode;\n" +
                "import java.util.Properties;\n" +
                "\n" +
                "\n" +
                "try{\n" +
                "SparkSession spark = SparkSession.builder().master(\"local[*]\").appName(\"test\").getOrCreate();\n" +
                "Dataset<Row> df_csv_depts_temp = spark.read().option(\"header\",\"true\").csv(\"\\opt\\test\\depts.csv\").toDF(\"id\",\"name\");\n" +
                "df_csv_depts_temp.createTempView(\"csv_depts_temp\");\n" +
                "Dataset<Row> result = spark.sql(\"select * from csv_depts_temp\");\n" +
                "result.show();\n" +
                "\n" +
                "}catch (Exception e){\n" +
                "System.out.println(e);\n" +
                "}";

        Assert.assertEquals(expect, code);

    }

    @Test
    public void test_spark_jdbc_code() throws TemplateException, IOException {

        Map<String, DataSource> dataSources = new HashMap<>();
        DataSource source = new DataSource("mysql", "department2", "mysql");
        source.addColumn("dep_id", "dep_id", "string");
        source.addColumn("cycle", "cycle", "string");
        source.addColumn("times", "times", "string");
        source.addColumn("type", "type", "string");
        source.addParam("jdbcDriver", "com.mysql.jdbc.Driver");
        source.addParam("jdbcUrl", "jdbc:mysql://127.0.0.1:3306/qsql");
        source.addParam("jdbcUser", "root");
        source.addParam("jdbcPassword", "123456");
        dataSources.put("mysql.department2", source);
        DataSources sources = new DataSources(dataSources);
        DataSources targets = new DataSources(ImmutableMap.of());


        String expect = "import org.apache.spark.sql.AnalysisException;\n" +
                "import org.apache.spark.sql.Dataset;\n" +
                "import org.apache.spark.sql.Row;\n" +
                "import org.apache.spark.sql.SparkSession;\n" +
                "import org.apache.spark.sql.SaveMode;\n" +
                "import java.util.Properties;\n" +
                "\n" +
                "\n" +
                "try{\n" +
                "SparkSession spark = SparkSession.builder().master(\"local[*]\").appName(\"test\").getOrCreate();\n" +
                "Class.forName(\"com.mysql.jdbc.Driver\");\n" +
                "Properties properties = new Properties();\n" +
                "properties.setProperty(\"user\",\"root\");\n" +
                "properties.setProperty(\"password\",\"123456\");\n" +
                "Dataset<Row> df_mysql_department2_temp = spark.read().jdbc(\"jdbc:mysql://127.0.0.1:3306/qsql\", \"department2\", properties);\n" +
                "df_mysql_department2_temp.createTempView(\"mysql_department2_temp\");\n" +
                "Dataset<Row> result = spark.sql(\"select * from mysql_department2_temp\");\n" +
                "result.show();\n" +
                "\n" +
                "}catch (Exception e){\n" +
                "System.out.println(e);\n" +
                "}";

        ZSqlSparkEngine engine = new ZSqlSparkEngine();
        String code = engine.getCode("select * from mysql_department2_temp", sources, targets);
        Assert.assertEquals(expect, code);

    }

    @Test
    public void test_csv_dml() throws IOException, TemplateException {
        Map<String, DataSource> dataSources = new HashMap<>();
        DataSource source = new DataSource("csv", "depts", "csv");
        source.addColumn("id", "id", "string");
        source.addColumn("name", "name", "string");
        source.addParam("directory", "/opt/test");
        dataSources.put("csv.depts", source);
        DataSources sources = new DataSources(dataSources);
        DataSources targets = new DataSources(dataSources);
        ZSqlSparkEngine engine = new ZSqlSparkEngine();
        String expect = "import org.apache.spark.sql.AnalysisException;\n" +
                "import org.apache.spark.sql.Dataset;\n" +
                "import org.apache.spark.sql.Row;\n" +
                "import org.apache.spark.sql.SparkSession;\n" +
                "import org.apache.spark.sql.SaveMode;\n" +
                "import java.util.Properties;\n" +
                "\n" +
                "\n" +
                "try{\n" +
                "SparkSession spark = SparkSession.builder().master(\"local[*]\").appName(\"test\").getOrCreate();\n" +
                "Dataset<Row> df_csv_depts_temp = spark.read().option(\"header\",\"true\").csv(\"\\opt\\test\\depts.csv\").toDF(\"id\",\"name\");\n" +
                "df_csv_depts_temp.createTempView(\"csv_depts_temp\");\n" +
                "Dataset<Row> result = spark.sql(\"select * from csv_depts_temp\");\n" +
                "result.show();\n" +
                "result.write().mode(SaveMode.Append).csv(\"\\opt\\test\\depts\");\n" +
                "}catch (Exception e){\n" +
                "System.out.println(e);\n" +
                "}";
        String code = engine.getCode("select * from csv_depts_temp", sources, targets);
        Assert.assertEquals(expect, code);
    }
}
