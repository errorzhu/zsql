package org.errorzhu.zsql.core.plan;

import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.core.executor.ExecutorFactory;
import org.errorzhu.zsql.core.lineage.Lineage;
import org.errorzhu.zsql.core.lineage.LineageType;
import org.errorzhu.zsql.engine.IEngine;
import org.errorzhu.zsql.engine.ZSqlEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

public class PhysicalPlan {

    private static final String EXECUTE = "execute";
    private final String extDir;
    private final DataSources dataTargets;
    private final Lineage lineage;
    private String sql;
    private IEngine engine;
    private DataSources dataSources;
    private String engineType;


    private Logger logger = LoggerFactory.getLogger(PhysicalPlan.class);

    public PhysicalPlan(String extDir, String engine, String sql, DataSources dataSources, DataSources dataTargets, Lineage lineage) throws IOException {
        this.engine = ZSqlEngineFactory.getInstance(engine);
        this.sql = sql;
        this.dataSources = dataSources;
        this.dataTargets = dataTargets;
        this.engineType = engine;
        this.extDir = extDir;
        this.lineage = lineage;
    }

    private String substituteTableName(String sql, DataSources dataSources) {
        for (String table : dataSources.getSources().keySet()) {
            sql = sql.replace(table, table.replace(".", "_") + "_temp");
        }
        return sql;
    }

    public void execute() throws Exception {

        String executeSql = substituteTableName(this.sql,this.dataSources);
        if (lineage.getType().equals(LineageType.INSERT)) {
            String targetTable = lineage.getTargets().get(0);
            executeSql = substituteTableName(this.sql.split(targetTable)[1].trim(),this.dataSources);
        }
        
        String code = this.engine.getCode(executeSql, this.dataSources,this.dataTargets);
        logger.info("execute code: " + code);
        Class<?> executor = ExecutorFactory.getInstance(this.extDir, this.engineType);
        Method method = executor.getDeclaredMethod(EXECUTE, String.class);
        method.invoke(executor.newInstance(), code);
    }
}
