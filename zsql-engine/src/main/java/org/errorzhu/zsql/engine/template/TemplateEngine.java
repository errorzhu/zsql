package org.errorzhu.zsql.engine.template;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class TemplateEngine {

    private Configuration configuration;
    private static TemplateEngine engine = null;

    public static TemplateEngine getInstance() throws IOException {
        if (engine == null) {
            return new TemplateEngine();
        } else {
            return engine;
        }
    }


    private TemplateEngine() throws IOException {
        this.configuration = new Configuration(Configuration.getVersion());
        configuration.setClassForTemplateLoading(this.getClass(), "/templates/");
        configuration.setDefaultEncoding("utf-8");
    }


    public Template getTemplate(String templateFile) throws IOException {
        Template template = configuration.getTemplate(templateFile);
        return template;
    }

    public String render(Template template, Map<String, Object> variables) throws IOException, TemplateException {
        Writer out = new StringWriter();
        template.process(variables, out);
        out.close();
        return out.toString();
    }

}
