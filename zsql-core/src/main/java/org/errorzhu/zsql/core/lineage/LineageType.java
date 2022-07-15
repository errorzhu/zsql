package org.errorzhu.zsql.core.lineage;

public enum LineageType {
    CREATE("create"), INSERT("insert"), NONE("none");

    private final String value;

    LineageType(String type) {
        this.value = type;
    }

    public String getValue() {
        return value;
    }
}
