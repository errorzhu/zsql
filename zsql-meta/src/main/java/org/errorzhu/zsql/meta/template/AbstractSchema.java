package org.errorzhu.zsql.meta.template;

import com.google.common.collect.Maps;

import java.util.Map;

public abstract class AbstractSchema {
    protected String name;
    protected String type = "custom";
    protected String factory ;
    protected Map<String,Object> operand = Maps.newHashMap();

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFactory() {
        return factory;
    }

    public Map<String, Object> getOperand() {
        return operand;
    }
}
