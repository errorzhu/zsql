package org.errorzhu.zsql.meta;

import org.errorzhu.zsql.meta.entity.DBS;
import org.errorzhu.zsql.meta.entity.TBLS;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.internal.QueryImpl;
import org.hibernate.transform.Transformers;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class HibernateTest {

    public static void main(String[] args) {
        SessionFactory sessionFactory = null;
        try {
            if (sessionFactory == null) {
                StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                        .configure("hibernate.cfg.xml").build();

                Metadata metaData = new MetadataSources(standardRegistry)
                        .getMetadataBuilder()
                        .build();

                sessionFactory = metaData.getSessionFactoryBuilder().build();

                EntityManager entityManager = sessionFactory.createEntityManager();
                entityManager.getTransaction().begin();
                entityManager.persist(new DBS("1", "h2", "h2", "h2"));
                entityManager.persist(new DBS("2", "sqlite", "sqlite", "sqlite"));
                entityManager.persist(new TBLS("1", "1", "h2_table", "h2_table", 1L));
                entityManager.getTransaction().commit();

//                Query query = entityManager.createQuery("from DBS", DBS.class);
//
//
//                List<DBS> dbs = query.getResultList();
//                System.out.println(dbs.get(0).toString());

                Query query1 = entityManager.createQuery("select d.name as dbName,t.name as tableName from DBS d join TBLS t on d.id = t.dbId");
                query1.unwrap(QueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
                List<Map<String,Object>> resultList = query1.getResultList();
                System.out.println(resultList.get(0).toString());


                entityManager.close();

            }

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        } finally {
            sessionFactory.close();
        }

    }


}
