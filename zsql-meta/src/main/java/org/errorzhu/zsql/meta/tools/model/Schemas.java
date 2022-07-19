package org.errorzhu.zsql.meta.tools.model;

import java.util.List;

public class Schemas {

    private List<Schema> schemas;

    public List<Schema> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<Schema> schemas) {
        this.schemas = schemas;
    }

    @Override
    public String toString() {
        return "Schemas{" +
                "schemas=" + schemas +
                '}';
    }
}
