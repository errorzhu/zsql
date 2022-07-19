package org.errorzhu.zsql.meta.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.errorzhu.zsql.meta.entity.COLUMNS;
import org.errorzhu.zsql.meta.entity.DBS;
import org.errorzhu.zsql.meta.entity.DB_PARAMS;
import org.errorzhu.zsql.meta.entity.TBLS;
import org.errorzhu.zsql.meta.repository.MetaRepositoryFactory;
import org.errorzhu.zsql.meta.repository.ZSqlMetaRepository;
import org.errorzhu.zsql.meta.tools.cmd.MetaOptionParser;
import org.errorzhu.zsql.meta.tools.cmd.MetaOptions;
import org.errorzhu.zsql.meta.tools.model.Column;
import org.errorzhu.zsql.meta.tools.model.Schema;
import org.errorzhu.zsql.meta.tools.model.Schemas;
import org.errorzhu.zsql.meta.tools.model.Table;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SchemaLoader {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MetaOptions options = MetaOptionParser.parse(args);
        String data = options.getData();
        Schemas schemas = objectMapper.readValue(data, Schemas.class);
        ZSqlMetaRepository repository = MetaRepositoryFactory.getInstance("h2");
        List<Object> rows = Lists.newLinkedList();
        for (Schema schema : schemas.getSchemas()) {
            String db = schema.getDb();
            String dbId = UUID.randomUUID().toString();
            rows.add(new DBS(dbId, db, schema.getDesc(), schema.getType()));
            for (Map.Entry<String, String> entry : schema.getParams().entrySet()) {
                rows.add(new DB_PARAMS(dbId, entry.getKey(), entry.getValue()));
            }
            for (Table table : schema.getTables()) {
                String tableId = UUID.randomUUID().toString();
                rows.add(new TBLS(tableId, dbId, table.getName(), table.getDesc(), System.currentTimeMillis()));
                for (Column column : table.getColumns()) {
                    rows.add(new COLUMNS(tableId, column.getName(), column.getDesc(), column.getType()));
                }
            }
        }
        repository.batchSave(rows);

    }

}
