package org.errorzhu.zsql.meta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.errorzhu.zsql.common.data.DataSource;
import org.errorzhu.zsql.meta.repository.MetaRepositoryFactory;
import org.errorzhu.zsql.meta.repository.ZSqlMetaRepository;
import org.errorzhu.zsql.meta.template.AbstractSchema;
import org.errorzhu.zsql.meta.template.CsvSchema;
import org.errorzhu.zsql.meta.template.JdbcSchema;
import org.errorzhu.zsql.meta.template.ModelTemplate;

import java.util.List;
import java.util.Map;

public class ZSqlMetaService {

    private ZSqlMetaRepository repository;
    private ObjectMapper om = new ObjectMapper();

    public ZSqlMetaService(String dbType) {
        this.repository = MetaRepositoryFactory.getInstance(dbType);
    }

    public ZSqlMetaRepository getRepository() {
        return repository;
    }

    private AbstractSchema getSchema(DataSource dataSource) {

        switch (dataSource.getType()) {
            case "csv":
                return new CsvSchema(dataSource.getDbName(), dataSource.getParams());
            case "mysql":
                return new JdbcSchema(dataSource.getDbName(), dataSource.getParams());
            default:
                throw new IllegalArgumentException("not support db type:  " + dataSource.getType());

        }

    }

    public Map<String, DataSource> getDataSources() {
        return repository.getAllSchemas();
    }

    public Map<String, DataSource> getDataSources(List<String> tables) {
        return repository.getSchemas(tables);
    }

    public String getMetaModel() throws JsonProcessingException {

        ModelTemplate template = new ModelTemplate();

        Map<String, DataSource> schemas = repository.getAllSchemas();
        for (String index : schemas.keySet()) {
            DataSource dataSource = schemas.get(index);
            template.addSchema(getSchema(dataSource));
        }

        return "inline: " + om.writeValueAsString(template);
    }

    public String getMetaModel(List<String> tables) throws JsonProcessingException {

        ModelTemplate template = new ModelTemplate();

        Map<String, DataSource> schemas = repository.getSchemas(tables);
        for (String index : schemas.keySet()) {
            DataSource dataSource = schemas.get(index);
            template.addSchema(getSchema(dataSource));
        }

        return "inline: " + om.writeValueAsString(template);
    }

    public void close(){
        this.repository.close();
    }

}
