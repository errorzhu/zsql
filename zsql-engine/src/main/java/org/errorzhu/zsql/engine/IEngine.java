package org.errorzhu.zsql.engine;

import freemarker.template.TemplateException;
import org.errorzhu.zsql.common.data.DataSources;

import java.io.IOException;

public interface IEngine {

    String getCode(String sql, DataSources dataSources, DataSources dataTargets) throws IOException, TemplateException;
}
