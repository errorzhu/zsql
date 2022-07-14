package org.errorzhu.zsql.meta.template;

import java.util.Map;

public class JdbcSchema extends AbstractSchema{

    public JdbcSchema(String name , Map<String,Object> operand) {
        this.name = name;
        this.factory = "org.apache.calcite.adapter.jdbc.JdbcSchema$Factory";
        this.operand = operand;
    }

}
