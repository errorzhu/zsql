package org.errorzhu.zsql.executor;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.commons.compiler.IScriptEvaluator;
import org.codehaus.janino.ScriptEvaluator;

import java.lang.reflect.InvocationTargetException;

public class DynamicCompiler  {

    private void compileAndRun(String code) throws CompileException, InvocationTargetException {
        IScriptEvaluator evaluator = new ScriptEvaluator();
        evaluator.cook(code);
        evaluator.evaluate(null);
        System.out.println("finish !!!");
    }

    public void execute(String code) throws Exception {
        compileAndRun(code);
    }
}
