package org.errorzhu.zsql.common.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataSource {

    private String dbName;
    private String tableName;
    private String type;
    private List<FieldInfo> columns = new LinkedList<>();
    private Map<String, Object> params = new HashMap<>();

    public DataSource(String dbName, String tableName, String type) {
        this.dbName = dbName;
        this.tableName = tableName;
        this.type = type;
    }

    public void addColumn(String columnName, String columnDesc, String columnType) {
        columns.add(new FieldInfo(columnName, columnDesc, columnType));
    }

    public void addParam(String key, Object value) {
        params.put(key, value);
    }

    public String getDbName() {
        return dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public List<FieldInfo> getColumns() {
        return columns;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "dbName='" + dbName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", type='" + type + '\'' +
                ", columns=" + columns +
                ", params=" + params +
                '}';
    }
}
