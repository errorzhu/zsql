package org.errorzhu.zsql.meta.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "DB_PARAMS")
public class DB_PARAMS {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column(name = "id")
    private String id;
    @Column(name = "db_id")
    private String dbId;
    @Column(name = "param_key")
    private String key;
    @Column(name = "param_value")
    private String value;

    public DB_PARAMS(String dbId, String key, String value) {

        this.dbId = dbId;
        this.key = key;
        this.value = value;
    }

    public DB_PARAMS() {

    }

    public String getId() {
        return id;
    }

    public String getDbId() {
        return dbId;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "DB_PARAMS{" +
                "id='" + id + '\'' +
                ", dbId='" + dbId + '\'' +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
