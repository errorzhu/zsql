package org.errorzhu.zsql.core.data;

import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.meta.ZSqlMetaService;

import java.util.List;

public class DataSourceConverter {


    private ZSqlMetaService zSqlMetaService;

    public DataSourceConverter() {
        this.zSqlMetaService = new ZSqlMetaService("h2");
    }

    public DataSourceConverter(String type) {
        this.zSqlMetaService = new ZSqlMetaService(type);
    }

    public DataSources convert(List<String> tables) {
        DataSources dataSources = new DataSources(zSqlMetaService.getDataSources(tables));
        return dataSources;
    }

}
