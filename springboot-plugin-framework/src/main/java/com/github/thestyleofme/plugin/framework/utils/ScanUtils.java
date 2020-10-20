package com.github.thestyleofme.plugin.framework.utils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.github.thestyleofme.plugin.framework.constants.BaseConstants;
import org.springframework.util.ClassUtils;

/**
 * <p>
 * 扫描工具类
 * </p>
 *
 * @author isaac 2020/6/16 11:33
 * @since 1.0
 */
public class ScanUtils {

    private static final String JAVA_CLASS_SUFFIX = ".class";

    private ScanUtils() {
        throw new IllegalStateException("util class");
    }

    /**
     * 扫描指定包中的类，包括子包中的类
     *
     * @param basePackage 包名
     * @param baseClass   当前操作的基础类
     * @return 类全路径(形如 ： xx.bb.cc)
     * @throws IOException 扫描异常
     */
    public static Set<String> scanClassPackageName(String basePackage, Class<?> baseClass) throws IOException {
        if (BaseConstants.OS_IS_WINDOWS) {
            // windows
            return scanClassPackageNameOfWindows(basePackage, baseClass);
        } else {
            // unix or linux
            return scanClassPackageNameOfOther(baseClass);
        }
    }

    /**
     * 扫描windows环境下的类。包括子包中的类
     *
     * @param basePackage 包名
     * @param baseClass   当前操作的基础类
     * @return 类全路径(形如 ： xx.bb.cc)
     * @throws IOException 扫描异常
     */
    private static Set<String> scanClassPackageNameOfWindows(String basePackage, Class<?> baseClass) throws IOException {
        String classpathRootPath = baseClass.getResource("/").getPath();
        final String classpath = classpathRootPath
                .replace("/", "\\")
                .replaceFirst("\\\\", "");
        // 把包名 packageName 转换为路径名
        basePackage = basePackage.replace(".", File.separator);
        // class 文件全路径
        String fullPath = classpath + basePackage;

        return filterPath(fullPath).map(path -> {
            String pathString = path.toString();
            return pathString
                    .replace(classpath, "")
                    .replace("\\", ".")
                    .replace(JAVA_CLASS_SUFFIX, "");
        }).collect(Collectors.toSet());
    }


    /**
     * 扫描linux/unix/mac环境下的类。包括子包中的类
     *
     * @param baseClass 当前操作的基础类
     * @return 类全路径(形如 ： xx.bb.cc)
     * @throws IOException 扫描异常
     */
    private static Set<String> scanClassPackageNameOfOther(Class<?> baseClass) throws IOException {
        final String classpath = baseClass.getResource("/").getPath();
        // class 文件全路径
        String fullPath = classpath + ClassUtils.classPackageAsResourcePath(baseClass);

        return filterPath(fullPath).map(path -> {
            String pathString = path.toString();
            // 去头去尾
            pathString = pathString
                    .replace(classpath, "")
                    .replace(JAVA_CLASS_SUFFIX, "");
            // 转换为 aa.bb.cc
            return ClassUtils.convertResourcePathToClassName(pathString);
        }).collect(Collectors.toSet());
    }

    /**
     * 过滤类
     *
     * @param fullPath 类的全路径
     * @return Stream<Path>
     * @throws IOException IOException
     */
    private static Stream<Path> filterPath(String fullPath) throws IOException {
        return Files.walk(Paths.get(fullPath))
                .filter(Objects::nonNull)
                .filter(Files::isRegularFile)
                .filter(path -> {
                    String fileName = path.getFileName().toString();
                    if (fileName == null) {
                        return false;
                    }
                    return fileName.endsWith(JAVA_CLASS_SUFFIX);
                });
    }


    /**
     * 扫描jar包中的类。
     *
     * @param basePackage 包名
     * @param classLoader jar的ClassLoader
     * @return 类全路径
     * @throws IOException 扫描异常
     */
    public static Set<String> scanClassPackageName(String basePackage, ClassLoader classLoader) throws IOException {
        Enumeration<URL> urlEnumeration = classLoader.getResources(basePackage.replace(".", "/"));
        Set<String> classPackageNames = new HashSet<>();
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();
            String protocol = url.getProtocol();
            if (!"jar".equalsIgnoreCase(protocol)) {
                // 不是jar协议
                return classPackageNames;
            }
            JarURLConnection connection = (JarURLConnection) url.openConnection();
            if (connection == null) {
                return classPackageNames;
            }
            connection.setUseCaches(false);
            JarFile jarFile = connection.getJarFile();
            if (jarFile == null) {
                return classPackageNames;
            }
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            // 迭代
            try {
                while (jarEntryEnumeration.hasMoreElements()) {
                    JarEntry entry = jarEntryEnumeration.nextElement();
                    String jarEntryName = entry.getName();
                    if (jarEntryName.contains(JAVA_CLASS_SUFFIX) &&
                            jarEntryName.replace("/", ".").startsWith(basePackage)) {
                        String className = jarEntryName
                                .substring(0, jarEntryName.lastIndexOf('.'))
                                .replace("/", ".");
                        classPackageNames.add(className);
                    }
                }
            } finally {
                jarFile.close();
            }
        }
        return classPackageNames;
    }


}
