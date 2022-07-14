package org.errorzhu.zsql.meta.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "COLUMNS")
public class COLUMNS {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy = "uuid2")
    @Column(name = "id")
    private String id;
    @Column(name = "table_id")
    private String tableId;
    @Column(name = "name")
    private String name;
    @Column(name = "desc")
    private String desc;
    @Column(name = "type")
    private String type;


    public COLUMNS(String tableId, String name, String desc, String type) {
        this.tableId = tableId;
        this.name = name;
        this.desc = desc;
        this.type = type;
    }

    public COLUMNS() {

    }

    public String getId() {
        return id;
    }

    public String getTableId() {
        return tableId;
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
        return "COLUMNS{" +
                "id='" + id + '\'' +
                ", tableId='" + tableId + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
