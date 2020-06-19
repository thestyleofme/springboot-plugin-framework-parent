package com.github.codingdebugallday.extension.mybatis.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * <p>
 * mybatis xml 操作者
 * </p>
 *
 * @author isaac 2020/6/18 16:31
 * @since 1.0
 */
public class MybatisXmlProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(MybatisXmlProcess.class);

    private static volatile MybatisXmlProcess mybatisXmlProcess = null;

    private final Map<String, List<PluginMybatisXmlMapperBuilder>> xmlMapperBuilders;
    private final SqlSessionFactory factory;

    private MybatisXmlProcess(SqlSessionFactory sqlSessionFactory) {
        this.factory = sqlSessionFactory;
        this.xmlMapperBuilders = new ConcurrentHashMap<>();
    }

    /**
     * 得到单例
     *
     * @param sqlSessionFactory sqlSessionFactory
     * @return MybatisXmlProcess
     */
    public static MybatisXmlProcess getInstance(SqlSessionFactory sqlSessionFactory) {
        Objects.requireNonNull(sqlSessionFactory);
        if (mybatisXmlProcess == null) {
            synchronized (MybatisXmlProcess.class) {
                if (mybatisXmlProcess == null) {
                    mybatisXmlProcess = new MybatisXmlProcess(sqlSessionFactory);
                }
            }
        }
        return mybatisXmlProcess;
    }


    /**
     * 加载xml资源
     *
     * @param resources         resources
     * @param pluginClassLoader pluginClassLoader
     */
    public void loadXmlResource(String pluginId,
                                List<Resource> resources,
                                ClassLoader pluginClassLoader) {
        if (resources == null || resources.isEmpty()) {
            return;
        }
        Configuration configuration = factory.getConfiguration();
        ClassLoader defaultClassLoader = Resources.getDefaultClassLoader();
        try {
            Resources.setDefaultClassLoader(pluginClassLoader);
            List<PluginMybatisXmlMapperBuilder> pluginMybatisXmlMapperBuilders =
                    xmlMapperBuilders.computeIfAbsent(pluginId, k -> new ArrayList<>());
            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    PluginMybatisXmlMapperBuilder xmlMapperBuilder = new PluginMybatisXmlMapperBuilder(
                            inputStream,
                            configuration,
                            resource.getFilename(),
                            configuration.getSqlFragments(),
                            pluginClassLoader);
                    pluginMybatisXmlMapperBuilders.add(xmlMapperBuilder);
                    xmlMapperBuilder.parse();
                } catch (IOException e) {
                    LOGGER.error("resource error", e);
                }
            }
        } finally {
            ErrorContext.instance().reset();
            Resources.setDefaultClassLoader(defaultClassLoader);
        }
    }

    public void unregister(String pluginId) {
        List<PluginMybatisXmlMapperBuilder> pluginMybatisXmlMapperBuilders = xmlMapperBuilders.get(pluginId);
        if (pluginMybatisXmlMapperBuilders == null) {
            return;
        }
        for (PluginMybatisXmlMapperBuilder pluginMybatisXmlMapperBuilder : pluginMybatisXmlMapperBuilders) {
            try {
                pluginMybatisXmlMapperBuilder.unregister();
            } catch (Exception e) {
                LOGGER.warn("UnRegistry xml type alias cache class error of plugin '{}', error '{}'",
                        pluginId, e.getMessage());
            }
        }
    }

}
