package org.errorzhu.spark;

import org.apache.spark.sql.AnalysisException;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class App {
public static void main(String[] args) throws AnalysisException {
SparkSession spark = SparkSession.builder().master("local[*]").appName("test").getOrCreate();
Dataset<${r"Row"}> df = spark.read().csv("${csv_file}").toDF("name", "id");
    df.createTempView("test");
    Dataset<${r"Row"}> result = spark.sql("select name from test");
        result.show();
   }
}