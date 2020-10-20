package com.github.thestyleofme.plugin.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * 插件文件工具类
 * </p>
 *
 * @author isaac 2020/6/16 13:59
 * @since 1.0
 */
public final class PluginFileUtils {

    private static final Logger log = LoggerFactory.getLogger(PluginFileUtils.class);

    private PluginFileUtils() {
        throw new IllegalStateException("util class");
    }

    public static String getMd5ByFile(File file) {
        String value = null;
        try (FileInputStream in = new FileInputStream(file)) {
            MappedByteBuffer byteBuffer = in.getChannel()
                    .map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (IOException | NoSuchAlgorithmException e) {
            log.error("Md5 error,", e);
        }
        return value;
    }


    public static void cleanEmptyFile(Path path) {
        if (path == null) {
            return;
        }
        if (!Files.exists(path)) {
            return;
        }
        try (Stream<Path> stream = Files.list(path)) {
            stream.forEach(subPath -> {
                File file = subPath.toFile();
                if (!file.isFile()) {
                    return;
                }
                long length = file.length();
                if (length == 0) {
                    try {
                        Files.deleteIfExists(subPath);
                    } catch (IOException e) {
                        log.error("delete file error,", e);
                    }
                }
            });
        } catch (IOException e) {
            log.error("list file error,", e);
        }
    }

    /**
     * 如果文件不存在, 则会创建
     *
     * @param path 插件路径
     * @return 插件路径
     * @throws IOException 没有发现文件异常
     */
    public static Path createExistFile(Path path) throws IOException {
        Path parent = path.getParent();
        if (!Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
        return path;
    }


}
