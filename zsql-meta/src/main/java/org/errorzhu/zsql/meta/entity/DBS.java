package org.errorzhu.zsql.meta.entity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "DBS")
public class DBS implements Serializable {


    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "desc")
    private String desc;
    @Column(name = "type")
    private String type;


    public DBS(String id, String name, String desc, String type) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.type = type;
    }

    public DBS() {

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }


    @Override
    public String toString() {
        return "DBS{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
