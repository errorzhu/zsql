${import}

try{
SparkSession spark = SparkSession.builder().master("local[*]").appName("test").getOrCreate();
${table_code}
Dataset<${r"Row"}> result = spark.sql("${sql}");
result.show();
}catch (Exception e){
System.out.println(e);
}