package org.errorzhu.zsql.engine;

import org.errorzhu.zsql.common.engine.EngineOption;
import org.errorzhu.zsql.engine.memory.ZSqlMemoryEngine;
import org.errorzhu.zsql.engine.spark.ZSqlSparkEngine;

import java.io.IOException;

public class ZSqlEngineFactory {

    public static IEngine getInstance(String engine) throws IOException {
        switch (EngineOption.valueOf(engine)) {
            case SPARK:
                return new ZSqlSparkEngine();
            case MEMORY:
                return new ZSqlMemoryEngine();
            default:
                throw new IllegalArgumentException("not support the engine: " + engine);
        }
    }


}
