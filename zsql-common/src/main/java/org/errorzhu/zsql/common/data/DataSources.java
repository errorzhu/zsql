package org.errorzhu.zsql.common.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSources {
    private Map<String, DataSource> allDataSources;
    private Map<String, DataSource> sources = new HashMap();


    public DataSources(Map<String, DataSource> allDataSources) {
        this.allDataSources = allDataSources;
    }

    public void add(List<String> schema) {
        if (schema.size() == 2) {
            String database = schema.get(0);
            String table = schema.get(1);
            String index = database + "." + table;
            if (allDataSources.containsKey(index)) {
                sources.put(index, allDataSources.get(index));
            }
        }
    }

    public Map<String, DataSource> getSources() {
        return sources;
    }
}
