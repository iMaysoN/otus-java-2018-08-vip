import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InitTests {
    private final static String BEFORE = "tests.MyBefore";
    private final static String TEST = "tests.MyTest";
    private final static String AFTER = "tests.MyAfter";

    public static void main(String[] args) {
        final String toTest = args[0];
//        final String toTest = "tests2.innertest2";
        try {
            //if it class
            final Class<?> testClass = Class.forName(toTest);
            runTestsInTestClass(testClass);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            //if it package
            final ClassLoader cl = Thread.currentThread().getContextClassLoader();
            try {
                final ArrayList<Class<?>> classes = getClassesByPackageName(toTest, cl);
                if (classes.size() == 0) {
                    System.out.println("FRAMEWORK: [Error] No tests or file found.");
                } else {
                    for (final Class<?> clazz : classes) {
                        runTestsInTestClass(clazz);
                    }
                }
            } catch (ClassNotFoundException | IOException | IllegalAccessException | InvocationTargetException | InstantiationException e1) {
                e1.printStackTrace();
            }
        }
    }

    private static ArrayList<Class<?>> getClassesByPackageName(final String packageName, final ClassLoader cl) throws ClassNotFoundException, IOException {
        String path = packageName.replace('.', '/');
        Enumeration resources = cl.getResources(path);
        ArrayList<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        final File[] files = directory.listFiles();
        for (final File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                Class<?> aClass = Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6));
                if (!aClass.isInterface()
                        && !aClass.isAnnotation()) {
                    classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
                }
            }
        }
        return classes;
    }

    private static void runTestsInTestClass(final Class<?> testClass) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        List<Method> beforeMethods = Stream.of(testClass.getMethods())
                .filter(method -> isMethodAnnotatedWith(method, BEFORE))
                .collect(Collectors.toList());

        List<Method> afterMethods = Stream.of(testClass.getMethods())
                .filter(method -> isMethodAnnotatedWith(method, AFTER))
                .collect(Collectors.toList());

        Stream.of(testClass.getMethods())
                .filter(method -> isMethodAnnotatedWith(method, TEST))
                .forEach(testMethod -> {
                    invokeTest(beforeMethods, afterMethods, testMethod, testClass);
                });
    }

    private static void invokeTest(final List<Method> before, final List<Method> after, final Method test, final Class<?> testClass) {
        try {
            Object inClass = testClass.getDeclaredConstructors()[0].newInstance();
            before.forEach(beforeMethod -> invokeStepsForTest(beforeMethod, test, inClass));
            invokeTestMethod(test, inClass);
            after.forEach(afterMethod -> invokeStepsForTest(afterMethod, test, inClass));
        } catch (Exception e) {
            System.out.println("Some unexpected problems: " + e.getMessage());
        }
    }

    private static void invokeTestMethod(final Method testMethod, final Object object) {
        try {
            testMethod.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.printf("FRAMEWORK: [Error] Failed launch test `%s.%s`, will be skipped.%n%s%n",
                    testMethod.getDeclaringClass().getName(),
                    testMethod.getName(),
                    e.getMessage());
            throw new RuntimeException();
        }
    }

    private static void invokeStepsForTest(final Method method, final Method testMethod, final Object object) {
        try {
            method.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.out.printf("FRAMEWORK: [Error] Failed launch `%s.%s`, test `%s.%s` skipped.%n%s%n",
                    method.getDeclaringClass().getName(),
                    method.getName(),
                    testMethod.getDeclaringClass().getName(),
                    testMethod.getName(),
                    e.getMessage());
            throw new RuntimeException();
        }
    }

    private static boolean isMethodAnnotatedWith(final Method method, final String annotationName) {
        return Stream.of(method.getDeclaredAnnotations())
                .anyMatch(annotation -> annotation.annotationType().getName().contains(annotationName));
    }
}
