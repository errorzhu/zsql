package org.errorzhu.zsql.meta.tools.model;

import java.util.List;
import java.util.Map;

public class Schema {

    private String db;
    private String desc;
    private String type;
    private Map<String,String> params;
    private List<Table> tables;

    public String getDb() {
        return db;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public List<Table> getTables() {
        return tables;
    }


    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }
}
