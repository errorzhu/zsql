package org.errorzhu.zsql.common.data;

import java.util.Map;

public class DataSources {
    private Map<String, DataSource> allDataSources;


    public DataSources(Map<String, DataSource> allDataSources) {
        this.allDataSources = allDataSources;
    }


    public Map<String, DataSource> getSources() {
        return allDataSources;
    }

    @Override
    public String toString() {
        return "DataSources{" +
                "allDataSources=" + allDataSources +
                '}';
    }
}
