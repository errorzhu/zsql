package org.errorzhu.zsql.core.executor;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class ExecutorClassLoaderTest {

    private static List<File> findJars(String dependencyDir) {
        List<File> jars = Lists.newLinkedList();
        Iterator<File> files = Files.fileTraverser().breadthFirst(new File(dependencyDir)).iterator();
        while (files.hasNext()) {
            File file = files.next();
            if (file.getName().endsWith(".jar")) {
                jars.add(file);
            }
        }
        return jars;
    }

    public static void main(String[] args) throws Exception {

        String code = "import org.apache.spark.sql.Dataset;\n" +
                "import org.apache.spark.sql.Row;\n" +
                "import org.apache.spark.sql.SparkSession;\n" +
                "\n" +
                "\n" +
                "try{\n" +
                "SparkSession spark = SparkSession.builder().master(\"local[*]\").appName(\"test\").getOrCreate();\n" +
                "Dataset<Row> df = spark.read().option(\"header\",\"true\").csv(\"test.csv\").toDF(\"id\",\"name\");\n" +
                "df.createTempView(\"csv_depts_temp\");\n" +
                "Dataset<Row> result = spark.sql(\"select name from csv_depts_temp\");\n" +
                "result.show();\n" +
                "}catch (Exception e){\n" +
                "System.out.println(e);\n" +
                "}";

        String code2 = "import org.apache.spark.sql.Dataset;\n" +
                "import org.apache.spark.sql.Row;\n" +
                "import org.apache.spark.sql.SparkSession;\n" +
                "import java.util.Properties;\n" +
                "\n" +
                "\n" +
                "try{\n" +
                "    SparkSession spark = SparkSession.builder().master(\"local[*]\").appName(\"test\").getOrCreate();\n" +
                "    Properties properties = new Properties();\n" +
                "    properties.setProperty(\"user\",\"root\");\n" +
                "    properties.setProperty(\"password\",\"123456\");\n" +
                "    Dataset<Row> df = spark.read().jdbc(\"jdbc:mysql://127.0.0.1:3306/qsql\", \"department2\", properties);\n" +
                "    df.createTempView(\"mysql_department2\");\n" +
                "    Dataset<Row> result = spark.sql(\"select * from mysql_department2\");\n" +
                "    result.show();\n" +
                "}catch (Exception e){\n" +
                "    System.out.println(e);\n" +
                "}";


        List<File> jars = findJars("zsql-executor\\target\\lib");
        jars.addAll(findJars("zsql-executor\\target"));
        jars.addAll(findJars("zsql-bundle\\spark-bundle\\target\\lib"));
        jars.addAll(findJars("zsql-bundle\\jdbc-bundle\\target\\lib"));
        URL[] urls = new URL[jars.size()];
        for (int i = 0; i < jars.size(); i++) {
            urls[i] = jars.get(i).toURI().toURL();
        }

        ExecutorClassLoader loader = new ExecutorClassLoader(urls);
        Thread.currentThread().setContextClassLoader(loader);

        Class<?> compilerClazz = loader.loadClass("org.errorzhu.zsql.executor.DynamicCompiler");
        Method method = compilerClazz.getDeclaredMethod("execute", String.class);

        method.invoke(compilerClazz.newInstance(),code2);
//        executor.execute(code2);
    }


}
