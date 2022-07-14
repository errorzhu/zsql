package org.errorzhu.zsql.core.cmd;


import com.beust.jcommander.Parameter;
import org.errorzhu.zsql.common.engine.EngineOption;

public class ZSqlOptions {

    @Parameter(names = "-engine", description = "execute engine: spark/flink/memory")
    private String engine = EngineOption.MEMORY.getValue();


    @Parameter(names = "-sql", description = "the sql which runs on engine")
    private String sql ;


    @Parameter(names = "-ext", description = "the extra dependency base dir")
    private String extDir;


    public String getEngine() {
        return engine;
    }

    public String getSql() {
        return sql;
    }

    public String getExtDir() {
        return extDir;
    }
}
