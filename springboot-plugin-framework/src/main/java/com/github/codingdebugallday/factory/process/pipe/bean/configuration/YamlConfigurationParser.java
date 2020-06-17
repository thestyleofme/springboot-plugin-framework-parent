package com.github.codingdebugallday.factory.process.pipe.bean.configuration;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TreeTraversingParser;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;
import com.github.codingdebugallday.exceptions.PluginException;
import com.github.codingdebugallday.integration.IntegrationConfiguration;
import org.springframework.core.io.Resource;

/**
 * <p>
 * yaml 配置解析者
 * </p>
 *
 * @author isaac 2020/6/16 14:06
 * @since 1.0
 */
public class YamlConfigurationParser extends AbstractConfigurationParser {

    private final YAMLFactory yamlFactory;
    private final ObjectMapper objectMapper;

    public YamlConfigurationParser(IntegrationConfiguration configuration) {
        super(configuration);
        this.yamlFactory = new YAMLFactory();
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    protected Object parse(Resource resource, Class<?> pluginConfigClass) {
        try (InputStream inputStream = resource.getInputStream()) {
            YAMLParser yamlParser = yamlFactory.createParser(inputStream);
            final JsonNode node = objectMapper.readTree(yamlParser);
            if (node == null) {
                return pluginConfigClass.newInstance();
            }
            TreeTraversingParser treeTraversingParser = new TreeTraversingParser(node);
            return objectMapper.readValue(treeTraversingParser, pluginConfigClass);
        } catch (IOException | IllegalAccessException | InstantiationException e) {
            throw new PluginException(e);
        }
    }
}
