package org.errorzhu.zsql.meta.entity;


import javax.persistence.*;

@Entity
@Table(name = "TBLS")
public class TBLS {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "db_id")
    private String dbId;

    @Column(name = "name")
    private String name;

    @Column(name = "desc")
    private String desc;

    @Column(name = "create_time")
    private Long createTime;


    public TBLS() {
    }

    public TBLS(String id, String dbId, String name, String desc, Long createTime) {
        this.id = id;
        this.dbId = dbId;
        this.name = name;
        this.desc = desc;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public String getDbId() {
        return dbId;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Long getCreateTime() {
        return createTime;
    }
}
