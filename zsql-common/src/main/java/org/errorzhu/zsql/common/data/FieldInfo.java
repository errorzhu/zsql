package org.errorzhu.zsql.common.data;

public class FieldInfo {

    private String columnName;
    private String columnDesc;
    private String getColumnType;

    public FieldInfo(String columnName, String columnDesc, String getColumnType) {
        this.columnName = columnName;
        this.columnDesc = columnDesc;
        this.getColumnType = getColumnType;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumnDesc() {
        return columnDesc;
    }

    public String getGetColumnType() {
        return getColumnType;
    }
}
