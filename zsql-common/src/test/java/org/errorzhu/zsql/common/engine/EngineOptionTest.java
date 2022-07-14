package org.errorzhu.zsql.common.engine;

import org.junit.Assert;
import org.junit.Test;

public class EngineOptionTest {
    
    @Test
    public void test_option_value(){
        EngineOption memory = EngineOption.valueOf("MEMORY");
        Assert.assertEquals(memory,EngineOption.MEMORY);
    }




}
