# zsql （异构查询引擎）

# 特点

1. 通用元数据服务

2. calcite解析sql

3. 执行计划与执行引擎解耦

4. 支持执行引擎扩展

5. 独立classloader

6. 动态编译执行

   

# 编译

```
mvn clean package
```



# 使用

```
tar -zxvf zsql.tar.gz
sh bin/zsql -sql 'select 1' -engine memory
```





# 参考

https://github.com/Qihoo360/Quicksql
https://calcite.apache.org/
https://matt33.com/2019/03/17/apache-calcite-planner/
https://blog.csdn.net/QXC1281/article/details/89070285
https://quicksql.readthedocs.io/en/stable/reference/getting-started/
https://github.com/WalrusOlap/walrus
https://vimsky.com/examples/detail/java-method-org.apache.calcite.tools.Frameworks.getPlanner.html