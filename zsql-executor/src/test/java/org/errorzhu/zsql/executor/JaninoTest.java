package org.errorzhu.zsql.executor;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;

import java.lang.reflect.InvocationTargetException;

public class JaninoTest {

    public static void main(String[] args) throws CompileException, InvocationTargetException {

        String content = "System.out.println(\"hello\");";
        IScriptEvaluator evaluator = new ScriptEvaluator();
        evaluator.cook(content);
        evaluator.evaluate(null);
    }
}
