package org.errorzhu.zsql.engine.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FreeMarkerTest {


    public static void main(String[] args) throws IOException, TemplateException {

        Configuration configuration = new Configuration(Configuration.getVersion());
        //2.设置模板文件所在目录
        configuration.setDirectoryForTemplateLoading(new File("src\\test\\resources\\"));
        //3.设置字符集
        configuration.setDefaultEncoding("utf-8");
        //4.加载模板
        Template template = configuration.getTemplate("test.ftl");
        //5.准备模板文件中所需的数据，一般通过map构造
        Map<String,String> map = new HashMap<>();
        map.put("csv_file","aaaa.csv");
        //6.创建Writer对象,用于输出静态文件
        Writer out = new StringWriter();
        template.process(map,out);
        //8.关闭流
        out.close();
        System.out.println(out.toString());


    }


}
