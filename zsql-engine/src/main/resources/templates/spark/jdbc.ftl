Class.forName("${jdbc_driver}");
Properties properties = new Properties();
properties.setProperty("user","${jdbc_user}");
properties.setProperty("password","${jdbc_password}");
Dataset<${r"Row"}> ${df_name} = spark.read().jdbc("${jdbc_url}", "${jdbc_table}", properties);
${df_name}.createTempView("${temp_table}");