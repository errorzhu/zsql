package org.errorzhu.zsql.core.lineage;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.calcite.avatica.util.Casing;
import org.apache.calcite.sql.*;
import org.apache.calcite.sql.parser.SqlParseException;
import org.apache.calcite.sql.parser.SqlParser;
import org.apache.calcite.sql.parser.ddl.SqlDdlParserImpl;
import org.apache.calcite.util.SourceStringReader;

import java.io.Reader;
import java.util.List;

public class LineageParser {

    SqlParser.Config config = SqlParser
            .config()
            .withCaseSensitive(true)
            .withParserFactory(SqlDdlParserImpl.FACTORY)
            .withQuotedCasing(Casing.UNCHANGED)
            .withUnquotedCasing(Casing.UNCHANGED);


    private List<String> getSourceTables(SqlNode node) throws SqlParseException {
        List<String> tables = Lists.newLinkedList();
        return getSourceTables(node,tables);
    }

    private List<String> getTargetTables(SqlNode node) {
        switch (node.getKind()){
            case CREATE_TABLE:
                SqlCreate create = (SqlCreate) node;
                node = create.getOperandList().get(2);
                return ImmutableList.of(create.getOperandList().get(0).toString());
            case INSERT:
                SqlInsert insert = (SqlInsert) node;
                node = insert.getSource();
                return ImmutableList.of(insert.getTargetTable().toString());
        }
        return null;
    }

    private SqlNode parseSQL(String sql) throws SqlParseException {
        Reader source = new SourceStringReader(sql);
        SqlParser sqlParser = SqlParser.create(source, config);
        SqlNode root = sqlParser.parseQuery();
        return root;
    }

    public Lineage parse(String sql) throws SqlParseException {
        SqlNode node = parseSQL(sql);
        List<String> targets = getTargetTables(node);
        List<String> source = getSourceTables(node);
        return new Lineage(source, targets);
    }


    private List<String> getSourceTables(SqlNode sqlNode, List<String> result) throws SqlParseException {

        switch (sqlNode.getKind()) {
            case CREATE_TABLE:
                processCreateTable((SqlCreate) sqlNode, result);
                break;
            case JOIN:
                processJoin((SqlJoin) sqlNode, result);
                break;
            case IDENTIFIER:
                processTable(sqlNode, result);
                break;
            case INSERT:
                processInsert((SqlInsert) sqlNode, result);
                break;
            case SELECT:
                processSelect((SqlSelect) sqlNode, result);
                break;
            case AS:
                processAs((SqlBasicCall) sqlNode, result);
                break;
            case UNION:
                processUnion((SqlBasicCall) sqlNode, result);
                break;
            case ORDER_BY:
                processOrderBy((SqlOrderBy) sqlNode, result);
                break;
        }

        return result;
    }

    private void processCreateTable(SqlCreate sqlNode, List<String> result) throws SqlParseException {
        SqlCreate create = sqlNode;
        getSourceTables(create.getOperandList().get(2), result);
    }

    private void processOrderBy(SqlOrderBy sqlNode, List<String> result) throws SqlParseException {
        SqlOrderBy sqlKind = sqlNode;
        getSourceTables(sqlKind.getOperandList().get(0), result);
    }

    private void processUnion(SqlBasicCall sqlNode, List<String> result) throws SqlParseException {
        SqlBasicCall sqlKind = sqlNode;

        getSourceTables(sqlKind.getOperandList().get(0), result);
        getSourceTables(sqlKind.getOperandList().get(1), result);
    }

    private void processAs(SqlBasicCall sqlNode, List<String> result) throws SqlParseException {
        SqlBasicCall sqlKind = sqlNode;
        getSourceTables(sqlKind.getOperandList().get(0), result);
    }

    private void processSelect(SqlSelect sqlNode, List<String> result) throws SqlParseException {
        SqlSelect sqlKind = sqlNode;
        getSourceTables(sqlKind.getFrom(), result);
    }

    private void processInsert(SqlInsert sqlNode, List<String> result) throws SqlParseException {
        SqlInsert sqlKind = sqlNode;
        getSourceTables(sqlKind.getSource(), result);
    }

    private void processTable(SqlNode sqlNode, List<String> result) {
        result.add(sqlNode.toString());
    }

    private void processJoin(SqlJoin sqlNode, List<String> result) throws SqlParseException {
        SqlJoin sqlKind = sqlNode;
        getSourceTables(sqlKind.getLeft(), result);
        getSourceTables(sqlKind.getRight(), result);
    }


}
