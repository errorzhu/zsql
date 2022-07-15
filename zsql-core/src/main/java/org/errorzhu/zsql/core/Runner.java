package org.errorzhu.zsql.core;

import com.google.common.collect.Lists;
import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.common.engine.EngineOption;
import org.errorzhu.zsql.core.cmd.ZSqlOptionParser;
import org.errorzhu.zsql.core.cmd.ZSqlOptions;
import org.errorzhu.zsql.core.data.DataSourceConverter;
import org.errorzhu.zsql.core.lineage.Lineage;
import org.errorzhu.zsql.core.lineage.LineageParser;
import org.errorzhu.zsql.core.lineage.LineageType;
import org.errorzhu.zsql.core.plan.PhysicalPlan;
import org.errorzhu.zsql.meta.ZSqlMetaService;

import java.util.List;
import java.util.Locale;

public class Runner {


    public static void main(String[] args) throws Exception {

        ZSqlOptions options = ZSqlOptionParser.parse(args);
        String engine = options.getEngine().trim().toUpperCase(Locale.ROOT);
        String sql = options.getSql().trim();
        String extDir = options.getExtDir().trim();

        ZSqlMetaService metaService = new ZSqlMetaService("h2");



        LineageParser lineageParser = new LineageParser();
        Lineage lineage = lineageParser.parse(sql);
        if (lineage.getType().equals(LineageType.CREATE)){
            throw new IllegalArgumentException("DDL not support yet");
        }

        List<String> sources = lineage.getSources();
        List<String> targets = lineage.getTargets();


        List<String> allDataSource = Lists.newLinkedList();
        allDataSource.addAll(sources);
        allDataSource.addAll(targets);

        if (EngineOption.valueOf(engine).equals(EngineOption.MEMORY)) {
            ZSqlParser parser = new ZSqlParser();
            String metaModel = metaService.getMetaModel(allDataSource);
            parser.parseAndRun(sql,metaModel);
            return;
        }


        DataSourceConverter converter = new DataSourceConverter();
        DataSources dataSources = converter.convert(sources);
        DataSources dataTargets = converter.convert(targets);


        PhysicalPlan physicalPlan = new PhysicalPlan(extDir, engine, sql, dataSources);
        physicalPlan.execute();

        System.exit(0);


    }


}
