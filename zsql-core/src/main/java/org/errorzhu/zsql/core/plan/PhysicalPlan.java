package org.errorzhu.zsql.core.plan;

import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.core.executor.ExecutorFactory;
import org.errorzhu.zsql.engine.IEngine;
import org.errorzhu.zsql.engine.ZSqlEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;

public class PhysicalPlan {

    private static final String EXECUTE = "execute";
    private final String extDir;
    private String executeSql;
    private IEngine engine;
    private DataSources dataSources;
    private String engineType;


    private Logger logger = LoggerFactory.getLogger(PhysicalPlan.class);

    public PhysicalPlan(String extDir, String engine, String sql, DataSources dataSources) throws IOException {
        this.engine = ZSqlEngineFactory.getInstance(engine);
        this.executeSql = substituteTableName(sql, dataSources);
        this.dataSources = dataSources;
        this.engineType = engine;
        this.extDir = extDir;
    }

    private String substituteTableName(String sql, DataSources dataSources) {
        for (String table : dataSources.getSources().keySet()) {
            sql = sql.replace(table, table.replace(".", "_") + "_temp");
        }
        return sql;
    }

    public void execute() throws Exception {
        // todo:支持dml
        String code = this.engine.getCode(this.executeSql, this.dataSources);
        logger.info("execute code: " + code);
        Class<?> executor = ExecutorFactory.getInstance(this.extDir, this.engineType);
        Method method = executor.getDeclaredMethod(EXECUTE, String.class);
        method.invoke(executor.newInstance(), code);
    }
}
