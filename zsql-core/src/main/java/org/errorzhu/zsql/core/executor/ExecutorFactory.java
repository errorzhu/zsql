package org.errorzhu.zsql.core.executor;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;

public class ExecutorFactory {

    private static final String DRIVER_DIR="driver";
    private static final String EXECUTOR_DIR="executor";
    private static final String ENGINE_DIR="engine";
    private static final String COMPILER_CLASS_NAME = "org.errorzhu.zsql.executor.DynamicCompiler";


    private ExecutorFactory() {
    }

    private static List<File> findJars(String dependencyDir) {
        List<File> jars = Lists.newLinkedList();
        Iterator<File> files = Files.fileTraverser().breadthFirst(new File(dependencyDir)).iterator();
        while (files.hasNext()) {
            File file = files.next();
            if (file.getName().endsWith(".jar")) {
                jars.add(file);
            }
        }
        return jars;
    }

    public static Class<?> getInstance(String extDir, String engineType) throws ClassNotFoundException, InstantiationException, IllegalAccessException, MalformedURLException, NoSuchMethodException {

        String driverPath = Paths.get(extDir, DRIVER_DIR).toString();
        String executorPath = Paths.get(extDir, EXECUTOR_DIR).toString();
        String enginePath = Paths.get(extDir, ENGINE_DIR, engineType).toString();
        List<File> jars = findJars(driverPath);
        jars.addAll(findJars(executorPath));
        jars.addAll(findJars(enginePath));

        URL[] urls = new URL[jars.size()];
        for (int i = 0; i < jars.size(); i++) {
            urls[i] = jars.get(i).toURI().toURL();
        }

        ExecutorClassLoader loader = new ExecutorClassLoader(urls);
        Thread.currentThread().setContextClassLoader(loader);

        Class<?> clazz = loader.loadClass(COMPILER_CLASS_NAME);

        return clazz;

    }

}
