package org.errorzhu.zsql.core;

import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.common.engine.EngineOption;
import org.errorzhu.zsql.core.cmd.ZSqlOptionParser;
import org.errorzhu.zsql.core.cmd.ZSqlOptions;
import org.errorzhu.zsql.core.data.DataSourceConverter;
import org.errorzhu.zsql.core.lineage.Lineage;
import org.errorzhu.zsql.core.lineage.LineageParser;
import org.errorzhu.zsql.core.lineage.LineageType;
import org.errorzhu.zsql.core.plan.PhysicalPlan;

import java.util.List;
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

        LineageParser lineageParser = new LineageParser();
        Lineage lineage = lineageParser.parse(sql);
        if (lineage.getType().equals(LineageType.CREATE)){
            throw new IllegalArgumentException("DDL not support yet");
        }

        List<String> sources = lineage.getSources();
        List<String> targets = lineage.getTargets();

        DataSourceConverter converter = new DataSourceConverter();

        DataSources dataSources = converter.convert(sources);
        DataSources dataTargets = converter.convert(targets);


        PhysicalPlan physicalPlan = new PhysicalPlan(extDir, engine, sql, dataSources);
        physicalPlan.execute();

        System.exit(0);


    }


}
