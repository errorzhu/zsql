package org.errorzhu.zsql.core;

public class RunnerTest {
    public static void main(String[] args) throws Exception {
        String [] cmd = new String[]{"-engine","memory","-sql","select * from csv.depts","-ext","/"};
        Runner runner = new Runner();

        runner.main(cmd);

    }
}
