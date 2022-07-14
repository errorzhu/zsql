package org.errorzhu.zsql.engine.memory;

import freemarker.template.TemplateException;
import org.errorzhu.zsql.common.data.DataSources;
import org.errorzhu.zsql.engine.IEngine;

import java.io.IOException;

public class ZSqlMemoryEngine implements IEngine {
    @Override
    public String getCode(String sql, DataSources dataSources) throws IOException, TemplateException {
        return null;
    }
}
