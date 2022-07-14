package org.errorzhu.zsql.common.engine;

public enum EngineOption {
    MEMORY("memory"), SPARK("spark"), FLINK("flink");

    private final String value;

    EngineOption(String engine) {
        this.value = engine;
    }

    public String getValue() {
        return value;
    }
}
