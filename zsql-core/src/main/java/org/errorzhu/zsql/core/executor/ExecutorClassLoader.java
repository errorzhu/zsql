package org.errorzhu.zsql.core.executor;

import java.net.URL;
import java.net.URLClassLoader;

public class ExecutorClassLoader extends URLClassLoader {
    public ExecutorClassLoader(URL[] urls) {
        super(urls, null);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        try {
            return super.findClass(name);
        } catch (ClassNotFoundException e) {
            return ExecutorClassLoader.class.getClassLoader().loadClass(name);
        }
    }
}
