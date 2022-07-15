package org.errorzhu.zsql.core.lineage;

import org.apache.calcite.sql.parser.SqlParseException;
import org.junit.Assert;
import org.junit.Test;

public class LineageParserTest {

    private LineageParser parser = new LineageParser();

    @Test
    public void test_simple_select() throws SqlParseException {
        Lineage lineage = parser.parse("select * from test.a");
        Assert.assertEquals(lineage.getSources().get(0),"test.a");
        Assert.assertNull(lineage.getTargets());
        Assert.assertEquals(LineageType.NONE,lineage.getType());
    }

    @Test
    public void test_create_as_simple_select() throws SqlParseException {
        Lineage lineage = parser.parse("create table B as select * from a");
        Assert.assertEquals(lineage.getSources().get(0),"a");
        Assert.assertEquals(lineage.getTargets().get(0),"B");
        Assert.assertEquals(LineageType.CREATE,lineage.getType());
    }

    @Test
    public void test_insert_as_simple_select() throws SqlParseException {
        Lineage lineage = parser.parse("insert into   b  select * from a");
        Assert.assertEquals(lineage.getSources().get(0),"a");
        Assert.assertEquals(lineage.getTargets().get(0),"b");
        Assert.assertEquals(LineageType.INSERT,lineage.getType());
    }

}
