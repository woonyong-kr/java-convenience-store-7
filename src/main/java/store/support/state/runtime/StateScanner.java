package store.support.state.runtime;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import store.support.state.annotation.ContextConfiguration;
import store.support.state.annotation.State;

public class StateScanner {

    private static final String CLASS_EXTENSION = ".class";
    private static final String FILE_PROTOCOL = "file";
    private static final String JAR_PROTOCOL = "jar";
    private static final String PACKAGE_SEPARATOR = ".";
    private static final String PATH_SEPARATOR = "/";
    private static final String ERROR_SCAN_CLASSES = "[ERROR] 클래스 스캔 중 오류가 발생했습니다: ";
    private static final String ERROR_SCAN_JAR = "[ERROR] JAR 스캔 중 오류가 발생했습니다.";
    private static final String ERROR_NO_INITIAL_STATE = "[ERROR] @InitialState 어노테이션이 붙은 클래스를 찾을 수 없습니다: ";

    private StateScanner() {
    }

    public static List<Class<?>> scanStateClasses(String basePackage) {
        List<Class<?>> allClasses = scanClasses(basePackage);
        return allClasses.stream()
                .filter(clazz -> clazz.isAnnotationPresent(State.class))
                .toList();
    }

    private static List<Class<?>> scanClasses(String basePackage) {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageToPath(basePackage);

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(path);
            scanResources(resources, basePackage, path, classes);
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_SCAN_CLASSES + basePackage, e);
        }

        return classes;
    }

    private static String packageToPath(String basePackage) {
        return basePackage.replace(PACKAGE_SEPARATOR.charAt(0), PATH_SEPARATOR.charAt(0));
    }

    private static void scanResources(Enumeration<URL> resources, String basePackage, String path, List<Class<?>> classes) throws IOException {
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            scanResource(url, basePackage, path, classes);
        }
    }

    private static void scanResource(URL url, String basePackage, String path, List<Class<?>> classes) throws IOException {
        String protocol = url.getProtocol();
        if (FILE_PROTOCOL.equals(protocol)) {
            String filePath = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8);
            scanDirectory(basePackage, filePath, classes);
        }
        if (JAR_PROTOCOL.equals(protocol)) {
            scanJar(url, path, classes);
        }
    }

    private static void scanDirectory(String basePackage, String directoryPath, List<Class<?>> classes) {
        File directory = new File(directoryPath);
        if (!isValidDirectory(directory)) {
            return;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            processFile(file, basePackage, classes);
        }
    }

    private static boolean isValidDirectory(File directory) {
        return directory.exists() && directory.isDirectory();
    }

    private static void processFile(File file, String basePackage, List<Class<?>> classes) {
        if (file.isDirectory()) {
            String subPackage = basePackage + PACKAGE_SEPARATOR + file.getName();
            scanDirectory(subPackage, file.getAbsolutePath(), classes);
        }
        if (isClassFile(file)) {
            String className = extractClassName(file, basePackage);
            tryLoadClass(className, classes);
        }
    }

    private static boolean isClassFile(File file) {
        return file.getName().endsWith(CLASS_EXTENSION);
    }

    private static String extractClassName(File file, String basePackage) {
        String fileName = file.getName().replace(CLASS_EXTENSION, "");
        return basePackage + PACKAGE_SEPARATOR + fileName;
    }

    private static void tryLoadClass(String className, List<Class<?>> classes) {
        try {
            Class<?> clazz = Class.forName(className);
            classes.add(clazz);
        } catch (ClassNotFoundException e) {
            // 클래스 로딩 실패 무시
        }
    }

    private static void scanJar(URL jarUrl, String path, List<Class<?>> classes) {
        try {
            JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
            JarFile jarFile = jarConnection.getJarFile();
            scanJarEntries(jarFile, path, classes);
        } catch (IOException e) {
            throw new IllegalStateException(ERROR_SCAN_JAR, e);
        }
    }

    private static void scanJarEntries(JarFile jarFile, String path, List<Class<?>> classes) {
        jarFile.stream()
                .filter(entry -> isClassEntry(entry, path))
                .forEach(entry -> {
                    String className = jarEntryToClassName(entry);
                    tryLoadClass(className, classes);
                });
    }

    private static boolean isClassEntry(JarEntry entry, String path) {
        String name = entry.getName();
        return !entry.isDirectory()
                && name.startsWith(path)
                && name.endsWith(CLASS_EXTENSION);
    }

    private static String jarEntryToClassName(JarEntry entry) {
        return entry.getName()
                .replace(PATH_SEPARATOR.charAt(0), PACKAGE_SEPARATOR.charAt(0))
                .replace(CLASS_EXTENSION, "");
    }

    public static Class<?> findInitialState(String basePackage) {
        List<Class<?>> stateClasses = scanStateClasses(basePackage);
        return stateClasses.stream()
                .filter(clazz -> clazz.getAnnotation(State.class).initial())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(ERROR_NO_INITIAL_STATE + basePackage));
    }

    @SuppressWarnings("unchecked")
    public static Class<? extends StateContext> findContextClass(String basePackage) {
        List<Class<?>> allClasses = scanClasses(basePackage);
        return (Class<? extends StateContext>) allClasses.stream()
                .filter(clazz -> clazz.isAnnotationPresent(ContextConfiguration.class))
                .filter(StateContext.class::isAssignableFrom)
                .findFirst()
                .orElse(null);
    }
}
