package org.errorzhu.zsql.meta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.errorzhu.zsql.meta.tools.model.Schemas;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class SchemasTest {

    private ObjectMapper om = new ObjectMapper();
    @Test
    public void test_sede() throws IOException {
        String expect = "{\"schemas\":[{\"db\":\"test\",\"desc\":\"test\",\"type\":\"mysql\",\"params\":{\"user\":\"root\"},\"tables\":[{\"name\":\"t1\",\"desc\":\"t1\",\"columns\":[{\"name\":\"c1\",\"desc\":\"c1\",\"type\":\"string\"}]}]}]}";
        Schemas schemas = om.readValue(expect, Schemas.class);
        Assert.assertEquals("test",schemas.getSchemas().get(0).getDb());
    }
}
