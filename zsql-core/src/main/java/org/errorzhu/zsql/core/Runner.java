package org.errorzhu.zsql.core;

import org.apache.calcite.rel.RelNode;
import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.common.engine.EngineOption;
import org.errorzhu.zsql.core.cmd.ZSqlOptionParser;
import org.errorzhu.zsql.core.cmd.ZSqlOptions;
import org.errorzhu.zsql.core.data.DataSourceExtractor;
import org.errorzhu.zsql.core.plan.PhysicalPlan;

import java.util.Locale;

public class Runner {


    public static void main(String[] args) throws Exception {

        ZSqlOptions options = ZSqlOptionParser.parse(args);
        ZSqlParser parser = new ZSqlParser();
        String engine = options.getEngine().trim().toUpperCase(Locale.ROOT);
        String sql = options.getSql().trim();
        String extDir = options.getExtDir().trim();

        if (EngineOption.valueOf(engine).equals(EngineOption.MEMORY)) {
            parser.parseAndRun(sql);
            return;
        }

        RelNode relNode = parser.parse(sql);
        DataSourceExtractor extractor = new DataSourceExtractor(relNode);
        DataSources dataSources = extractor.extract();
        PhysicalPlan physicalPlan = new PhysicalPlan(extDir, engine, sql, dataSources);
        physicalPlan.execute();

        System.exit(0);


    }


}
