package com.github.codingdebugallday.integration;

import java.util.Objects;

import org.pf4j.RuntimeMode;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 通过构造者进行配置插件初始化配置
 * </p>
 *
 * @author isaac 2020/6/16 15:59
 * @since 1.0
 */
public class ConfigurationBuilder extends AbstractIntegrationConfiguration {

    private final RuntimeMode runtimeMode;
    private final String pluginPath;
    private final String pluginConfigFilePath;

    private final String uploadTempPath;
    private final String backupPath;
    private final String pluginRestControllerPathPrefix;
    private final Boolean enablePluginIdRestControllerPathPrefix;

    public ConfigurationBuilder(Builder builder) {
        this.runtimeMode = Objects.requireNonNull(builder.runtimeMode, "runtimeMode can't be empty");
        this.pluginPath = Objects.requireNonNull(builder.pluginPath, "pluginPath can't be empty");
        this.pluginConfigFilePath = Objects.requireNonNull(builder.pluginConfigFilePath,
                "pluginConfigFilePath can't be empty");
        this.uploadTempPath = builder.uploadTempPath;
        this.backupPath = builder.backupPath;
        this.pluginRestControllerPathPrefix = builder.pluginRestControllerPathPrefix;
        this.enablePluginIdRestControllerPathPrefix = builder.enablePluginIdRestControllerPathPrefix;
    }

    public static Builder toBuilder() {
        return new Builder();
    }

    public static class Builder {
        private RuntimeMode runtimeMode = RuntimeMode.DEVELOPMENT;
        private String pluginPath = "";
        private String pluginConfigFilePath = "";

        private String uploadTempPath;
        private String backupPath;
        private String pluginRestControllerPathPrefix;
        private Boolean enablePluginIdRestControllerPathPrefix;

        public Builder runtimeMode(RuntimeMode runtimeMode) {
            this.runtimeMode = runtimeMode;
            return this;
        }

        public Builder pluginPath(String pluginPath) {
            this.pluginPath = pluginPath;
            return this;
        }

        public Builder pluginConfigFilePath(String pluginConfigFilePath) {
            this.pluginConfigFilePath = pluginConfigFilePath;
            return this;
        }

        public Builder uploadTempPath(String uploadTempPath) {
            this.uploadTempPath = uploadTempPath;
            return this;
        }

        public Builder backupPath(String backupPath) {
            this.backupPath = backupPath;
            return this;
        }

        public Builder pluginRestControllerPathPrefix(String pluginRestControllerPathPrefix) {
            this.pluginRestControllerPathPrefix = pluginRestControllerPathPrefix;
            return this;
        }

        public Builder enablePluginIdRestControllerPathPrefix(Boolean enablePluginIdRestControllerPathPrefix) {
            this.enablePluginIdRestControllerPathPrefix = enablePluginIdRestControllerPathPrefix;
            return this;
        }

        public com.github.codingdebugallday.integration.ConfigurationBuilder build() {
            return new com.github.codingdebugallday.integration.ConfigurationBuilder(this);
        }

    }

    @Override
    public RuntimeMode environment() {
        return runtimeMode;
    }

    @Override
    public String pluginPath() {
        return pluginPath;
    }

    @Override
    public String pluginConfigFilePath() {
        return pluginConfigFilePath;
    }

    @Override
    public String uploadTempPath() {
        if (StringUtils.isEmpty(uploadTempPath)) {
            return super.uploadTempPath();
        } else {
            return uploadTempPath;
        }
    }

    @Override
    public String backupPath() {
        if (StringUtils.isEmpty(backupPath)) {
            return super.backupPath();
        } else {
            return backupPath;
        }
    }

    @Override
    public String pluginRestControllerPathPrefix() {
        if (StringUtils.isEmpty(pluginRestControllerPathPrefix)) {
            return super.pluginRestControllerPathPrefix();
        } else {
            return pluginRestControllerPathPrefix;
        }
    }

    @Override
    public boolean enablePluginIdRestControllerPathPrefix() {
        if (enablePluginIdRestControllerPathPrefix == null) {
            return super.enablePluginIdRestControllerPathPrefix();
        } else {
            return enablePluginIdRestControllerPathPrefix;
        }
    }

}
