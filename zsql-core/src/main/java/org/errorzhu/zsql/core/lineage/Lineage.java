package org.errorzhu.zsql.core.lineage;

import java.util.List;

public class Lineage {

    private LineageType type;
    private List<String> sources;
    private List<String> targets;

    public Lineage(List<String> sources, List<String> targets, LineageType type) {
        this.sources = sources;
        this.targets = targets;
        this.type = type;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getTargets() {
        return targets;
    }

    public LineageType getType() {
        return type;
    }
}
