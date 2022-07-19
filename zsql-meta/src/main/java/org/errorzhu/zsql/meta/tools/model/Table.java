package org.errorzhu.zsql.meta.tools.model;

import java.util.List;

public class Table {

    private String name;
    private String desc;
    private List<Column> columns;

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public List<Column> getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", columns=" + columns +
                '}';
    }
}
