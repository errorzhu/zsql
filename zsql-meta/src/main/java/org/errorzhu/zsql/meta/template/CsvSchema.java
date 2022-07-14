package org.errorzhu.zsql.meta.template;

import java.util.Map;

public class CsvSchema extends AbstractSchema{

    public CsvSchema(String name ,Map<String,Object> operand) {
        this.name = name;
        this.factory = "org.apache.calcite.adapter.file.FileSchemaFactory";
        this.operand = operand;
    }

}
