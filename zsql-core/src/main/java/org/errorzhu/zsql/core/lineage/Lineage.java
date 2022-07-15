package org.errorzhu.zsql.core.lineage;

import java.util.List;

public class Lineage {

    private List<String> sources;
    private List<String> targets;

    public Lineage(List<String> sources, List<String> targets) {
        this.sources = sources;
        this.targets = targets;
    }

    public List<String> getSources() {
        return sources;
    }

    public List<String> getTargets() {
        return targets;
    }


}
