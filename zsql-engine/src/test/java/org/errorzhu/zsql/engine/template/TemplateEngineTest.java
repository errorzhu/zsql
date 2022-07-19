package org.errorzhu.zsql.engine.template;

import com.google.common.collect.ImmutableMap;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

public class TemplateEngineTest {

    @Ignore
    @Test
    public void test_render_import() throws IOException, TemplateException {
        TemplateEngine engine = TemplateEngine.getInstance();
        Template template = engine.getTemplate("spark/import.ftl");
        String result = engine.render(template, ImmutableMap.of());
        String expect =
                "import org.apache.spark.sql.AnalysisException;\n" +
                "import org.apache.spark.sql.Dataset;\n" +
                "import org.apache.spark.sql.Row;\n" +
                "import org.apache.spark.sql.SparkSession;\n"+
                "import org.apache.spark.sql.SaveMode;\n"+
                "import java.util.Properties;\n";
        Assert.assertEquals(expect,result);


    }
}
