package com.github.codingdebugallday.plugin.framework.integration.pf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.pf4j.PluginRuntimeException;
import org.pf4j.PropertiesPluginDescriptorFinder;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/16 15:56
 * @since 1.0
 */
class ResolvePropertiesPluginDescriptorFinder extends PropertiesPluginDescriptorFinder {

    @Override
    protected Properties readProperties(Path pluginPath) {
        Path propertiesPath = getPropertiesPath(pluginPath, propertiesFileName);
        if (propertiesPath == null) {
            throw new PluginRuntimeException("Cannot find the properties path");
        }
        if (Files.notExists(propertiesPath)) {
            throw new PluginRuntimeException("Cannot find '{}' path", propertiesPath);
        }
        Properties properties = new Properties();
        try (InputStreamReader input = new InputStreamReader(Files.newInputStream(propertiesPath),
                StandardCharsets.UTF_8);) {
            properties.load(input);
        } catch (IOException e) {
            throw new PluginRuntimeException(e);
        }
        return properties;
    }
}
