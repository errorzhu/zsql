package org.errorzhu.zsql.meta.tools.cmd;

import com.beust.jcommander.Parameter;

public class MetaOptions {

    private String data;

    @Parameter(names = "-data", description = "meta data")
    public String getData() {
        return data;
    }
}
