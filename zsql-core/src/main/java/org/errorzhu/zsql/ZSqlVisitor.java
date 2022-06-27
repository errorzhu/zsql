package org.errorzhu.zsql;

import org.apache.calcite.sql.*;
import org.apache.calcite.sql.util.SqlVisitor;

public class ZSqlVisitor implements SqlVisitor<String> {
    @Override
    public String visit(SqlLiteral sqlLiteral) {
        return null;
    }

    @Override
    public String visit(SqlCall sqlCall) {
        return null;
    }

    @Override
    public String visit(SqlNodeList sqlNodeList) {
        return null;
    }

    @Override
    public String visit(SqlIdentifier sqlIdentifier) {
        return null;
    }

    @Override
    public String visit(SqlDataTypeSpec sqlDataTypeSpec) {
        return null;
    }

    @Override
    public String visit(SqlDynamicParam sqlDynamicParam) {
        return null;
    }

    @Override
    public String visit(SqlIntervalQualifier sqlIntervalQualifier) {
        return null;
    }
}
