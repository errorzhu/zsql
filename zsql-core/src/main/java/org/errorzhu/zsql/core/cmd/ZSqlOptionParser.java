package org.errorzhu.zsql.core.cmd;

import com.beust.jcommander.JCommander;

public class ZSqlOptionParser {
    public static ZSqlOptions parse(String[] args){
        ZSqlOptions options = new ZSqlOptions();
        JCommander.newBuilder()
                .addObject(options)
                .build()
                .parse(args);
        return options;
    }
}
