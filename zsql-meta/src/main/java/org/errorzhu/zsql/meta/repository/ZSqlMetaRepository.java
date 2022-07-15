package org.errorzhu.zsql.meta.repository;

import com.google.common.collect.Maps;
import org.errorzhu.zsql.common.data.DataSource;
import org.errorzhu.zsql.meta.entity.DBS;
import org.hibernate.SessionFactory;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ZSqlMetaRepository {
    private final EntityManager entityManager;
    private final SessionFactory factory;

    protected ZSqlMetaRepository(SessionFactory factory) {
        this.factory = factory;
        this.entityManager = factory.createEntityManager();
    }


    public void save(Object row) {
        entityManager.getTransaction().begin();
        entityManager.persist(row);
        entityManager.getTransaction().commit();
    }

    public void batchSave(List<Object> rows) {
        entityManager.getTransaction().begin();
        for (Object row : rows) {
            entityManager.persist(row);
        }
        entityManager.getTransaction().commit();
    }

    public List<DBS> getDatabase() {
        Query query = entityManager.createQuery("from DBS", DBS.class);
        return (List<DBS>) query.getResultList();
    }

    public Map<String, DataSource> getAllSchemas() {
        Map<String, DataSource> schemas = Maps.newHashMap();
        Query query = entityManager.createQuery("select d.name as dbName,d.type as dbType,t.name as tableName ,c.name as columnName , c.desc as columnDesc,c.type as columnType from DBS d join TBLS t on d.id = t.dbId join COLUMNS c on t.id = c.tableId");
        query.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> dbTableRelation = query.getResultList();
        Query query2 = entityManager.createQuery("select d.name as dbName,t.name as tableName , p.key as paramKey,p.value as paramValue from DBS d join DB_PARAMS p on d.id = p.dbId join TBLS t on t.dbId = d.id");
        query2.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> dbParameter = query2.getResultList();

        for (Map<String, Object> row : dbTableRelation) {
            String index = row.get("dbName").toString() + "." + row.get("tableName").toString();
            if (schemas.containsKey(index)) {
                schemas.get(index).addColumn(row.get("columnName").toString(), row.get("columnDesc").toString(), row.get("columnType").toString());
            } else {
                DataSource dataSource = new DataSource(row.get("dbName").toString(), row.get("tableName").toString(),row.get("dbType").toString());
                dataSource.addColumn(row.get("columnName").toString(), row.get("columnDesc").toString(), row.get("columnType").toString());
                schemas.put(index, dataSource);
            }
        }

        for (Map<String, Object> row : dbParameter) {
            String index = row.get("dbName").toString() + "." + row.get("tableName").toString();

            if (schemas.containsKey(index)) {
                schemas.get(index).addParam(row.get("paramKey").toString(), row.get("paramValue"));
            }

        }


        return schemas;
    }


    public void clear() {
        entityManager.clear();
    }

    public void close(){
        this.entityManager.close();
        this.factory.close();
    }

    public Map<String, DataSource> getSchemas(List<String> tables) {
        Map<String, DataSource> allSchemas = getAllSchemas();
        Map<String, DataSource> result = allSchemas.entrySet().stream().filter(x -> tables.contains(x.getKey())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return result;
    }
}
