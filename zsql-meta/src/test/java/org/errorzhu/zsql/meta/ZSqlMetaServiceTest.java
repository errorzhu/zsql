package org.errorzhu.zsql.meta;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.errorzhu.zsql.meta.entity.COLUMNS;
import org.errorzhu.zsql.meta.entity.DBS;
import org.errorzhu.zsql.meta.entity.DB_PARAMS;
import org.errorzhu.zsql.meta.entity.TBLS;
import org.errorzhu.zsql.meta.repository.MetaRepositoryFactory;
import org.errorzhu.zsql.meta.repository.ZSqlMetaRepository;
import org.junit.Assert;
import org.junit.Test;

public class ZSqlMetaServiceTest {


    private ZSqlMetaService service = new ZSqlMetaService("");
    ZSqlMetaRepository repository = MetaRepositoryFactory.getInstance("");


    @Test
    public void test_get_csv_model() throws JsonProcessingException {

        repository.clear();
        repository.save(new DBS("1", "csv", "csv", "csv"));
        repository.save(new TBLS("1", "1", "depts", "depts", System.currentTimeMillis()));
        repository.save(new COLUMNS("1","id","id","string"));
        repository.save(new COLUMNS("1","name","name","string"));
        repository.save(new DB_PARAMS("1","directory","sales"));

        String model = service.getMetaModel();
        String expect = "inline: {\"version\":\"1.0\",\"defaultSchema\":\"system\",\"schemas\":[{\"name\":\"csv\",\"type\":\"custom\",\"factory\":\"org.apache.calcite.adapter.file.FileSchemaFactory\",\"operand\":{\"directory\":\"sales\"}}]}";
        Assert.assertEquals(expect,model);


    }


}
