package org.errorzhu.zsql.meta.tools.cmd;

import com.beust.jcommander.JCommander;

public class MetaOptionParser {
    public static MetaOptions parse(String[] args){
        MetaOptions options = new MetaOptions();
        JCommander.newBuilder()
                .addObject(options)
                .build()
                .parse(args);
        return options;
    }
}
