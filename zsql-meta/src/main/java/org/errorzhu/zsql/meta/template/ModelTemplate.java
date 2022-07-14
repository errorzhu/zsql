package org.errorzhu.zsql.meta.template;

import com.google.common.collect.Lists;

import java.util.List;

public class ModelTemplate {

    private String version = "1.0";
    private String defaultSchema = "system";
    private List<AbstractSchema> schemas = Lists.newLinkedList();

    public String getVersion() {
        return version;
    }

    public String getDefaultSchema() {
        return defaultSchema;
    }

    public List<AbstractSchema> getSchemas() {
        return schemas;
    }

    public void addSchema(AbstractSchema schema) {
        schemas.add(schema);
    }
}
