package org.errorzhu.zsql.meta.repository;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class MetaRepositoryFactory {


    public static ZSqlMetaRepository getInstance(String type) {

        String config = getConfig(type);

        StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                .configure(config).build();

        Metadata metaData = new MetadataSources(standardRegistry)
                .getMetadataBuilder()
                .build();

        SessionFactory sessionFactory = metaData.getSessionFactoryBuilder().build();

        return new ZSqlMetaRepository(sessionFactory);
    }

    private static String getConfig(String type) {
        if (StringUtils.isEmpty(type)) {
            return "hibernate.cfg.xml";
        } else {
            return String.format("hibernate.%s.cfg.xml", type);
        }
    }
}
