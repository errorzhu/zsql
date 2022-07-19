Properties prop = new Properties();
prop.setProperty("user","${jdbc_user}");
prop.setProperty("password","${jdbc_password}");
result.write().mode(SaveMode.Append).jdbc("${jdbc_url}", "${jdbc_table}", prop);