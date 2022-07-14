package org.errorzhu.zsql.core.data;

import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.RelShuttleImpl;
import org.apache.calcite.rel.core.TableScan;
import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.meta.ZSqlMetaService;

import java.util.List;

public class DataSourceExtractor extends RelShuttleImpl {

    private RelNode relNode;
    private ZSqlMetaService metaService = new ZSqlMetaService("h2");
    private DataSources dataSources;

    public DataSourceExtractor(RelNode relNode) {
        this.relNode = relNode;
        this.dataSources = new DataSources(metaService.getDataSources());
        metaService.close();
    }

    @Override
    public RelNode visit(TableScan scan) {
        List<String> name = scan.getTable().getQualifiedName();
        this.dataSources.add(name);
        return super.visit(scan);
    }

    public DataSources extract() {
        this.relNode.accept(this);
        return dataSources;
    }
}
