package org.errorzhu.zsql;

import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;

public class TableCollector  {
    private SqlParser.Config config = SqlParser.config().withParserFactory(SqlDdlParserImpl.FACTORY);


}
