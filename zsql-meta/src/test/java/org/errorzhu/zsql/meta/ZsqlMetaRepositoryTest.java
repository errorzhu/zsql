package org.errorzhu.zsql.meta;

import org.errorzhu.zsql.meta.entity.DBS;
import org.errorzhu.zsql.meta.repository.MetaRepositoryFactory;
import org.errorzhu.zsql.meta.repository.ZSqlMetaRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class ZsqlMetaRepositoryTest {

    ZSqlMetaRepository repository = MetaRepositoryFactory.getInstance("");

    @After
    public void setup() {
        repository.clear();
    }

    @Test
    public void test_save() {
        repository.save(new DBS("1", "DB1", "DB", "h2"));
        Assert.assertEquals(1,repository.getDatabase().size());
    }
}
