package com.github.codingdebugallday.integration.operator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import com.github.codingdebugallday.constants.BaseConstants;
import com.github.codingdebugallday.exceptions.PluginException;
import com.github.codingdebugallday.factory.DefaultPluginFactory;
import com.github.codingdebugallday.factory.PluginFactory;
import com.github.codingdebugallday.integration.IntegrationConfiguration;
import com.github.codingdebugallday.integration.listener.PluginInitializerListener;
import com.github.codingdebugallday.integration.listener.PluginInitializerListenerFactory;
import com.github.codingdebugallday.integration.listener.PluginListenerFactory;
import com.github.codingdebugallday.integration.operator.module.PluginInfo;
import com.github.codingdebugallday.integration.operator.verify.DefaultPluginVerify;
import com.github.codingdebugallday.integration.operator.verify.PluginLegalVerify;
import com.github.codingdebugallday.utils.GlobalRegistryInfo;
import com.github.codingdebugallday.utils.PluginFileUtils;
import com.github.codingdebugallday.utils.PluginOperatorInfo;
import org.pf4j.PluginManager;
import org.pf4j.PluginState;
import org.pf4j.PluginWrapper;
import org.pf4j.RuntimeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 默认的插件操作者
 * </p>
 *
 * @author isaac 2020/6/16 15:42
 * @since 1.0
 */
public class DefaultPluginOperator implements PluginOperator {

    private boolean isInit = false;
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern(BaseConstants.Pattern.NONE_DATETIME);

    protected final IntegrationConfiguration integrationConfiguration;
    protected final PluginManager pluginManager;
    protected final PluginFactory pluginFactory;
    protected final PluginInitializerListenerFactory pluginInitializerListenerFactory;

    protected PluginLegalVerify pluginLegalVerify;


    public DefaultPluginOperator(ApplicationContext applicationContext,
                                 IntegrationConfiguration integrationConfiguration,
                                 PluginManager pluginManager,
                                 PluginListenerFactory pluginListenerFactory) {
        Objects.requireNonNull(integrationConfiguration, "IntegrationConfiguration can't be null");
        Objects.requireNonNull(pluginManager, "PluginManager can't be null");
        this.integrationConfiguration = integrationConfiguration;
        this.pluginManager = pluginManager;
        this.pluginFactory = new DefaultPluginFactory(applicationContext, pluginListenerFactory);
        this.pluginInitializerListenerFactory = new PluginInitializerListenerFactory(applicationContext);

        this.pluginLegalVerify = new DefaultPluginVerify(pluginManager);
    }

    /**
     * 设置插件校验器
     *
     * @param uploadPluginVerify uploadPluginVerify
     */
    public void setUploadPluginVerify(PluginLegalVerify uploadPluginVerify) {
        if (uploadPluginVerify != null) {
            this.pluginLegalVerify = uploadPluginVerify;
        }
    }

    @Override
    public synchronized void initPlugins(PluginInitializerListener pluginInitializerListener) {
        if (isInit) {
            throw new PluginException("Plugins Already initialized. Cannot be initialized again");
        }
        try {
            // 启动前, 清除空文件
            PluginFileUtils.cleanEmptyFile(pluginManager.getPluginsRoot());

            pluginInitializerListenerFactory.addPluginInitializerListeners(pluginInitializerListener);
            log.info("Plugins start initialize of root path '{}'", pluginManager.getPluginsRoot());
            // 触发插件初始化监听器
            pluginInitializerListenerFactory.before();
            // 开始初始化插件工厂
            pluginFactory.initialize();
            // 开始加载插件
            pluginManager.loadPlugins();
            pluginManager.startPlugins();
            List<PluginWrapper> pluginWrappers = pluginManager.getStartedPlugins();
            if (pluginWrappers == null || pluginWrappers.isEmpty()) {
                log.warn("Not found plugin!");
                return;
            }
            boolean isFoundException = false;
            for (PluginWrapper pluginWrapper : pluginWrappers) {
                String pluginId = pluginWrapper.getPluginId();
                GlobalRegistryInfo.addOperatorPluginInfo(pluginId,
                        PluginOperatorInfo.OperatorType.INSTALL, false);
                isFoundException = register(pluginWrapper, pluginId);
            }
            pluginFactory.build();
            isInit = true;
            if (isFoundException) {
                log.error("Plugins initialize failure");
            } else {
                log.info("Plugins initialize success");
                pluginInitializerListenerFactory.complete();
            }
        } catch (Exception e) {
            pluginInitializerListenerFactory.failure(e);
            throw e;
        }
    }

    private boolean register(PluginWrapper pluginWrapper, String pluginId) {
        boolean isFoundException = false;
        try {
            // 依次注册插件信息到Spring boot
            pluginFactory.register(pluginWrapper);
        } catch (Exception e) {
            log.error("Plugin '{}' registry failure. Reason : {}", pluginId, e.getMessage(), e);
            isFoundException = true;
        }
        return isFoundException;
    }

    @Override
    public boolean install(Path path) {
        if (isDev()) {
            throw new PluginException("Plugin cannot be installed in 'dev' environment");
        }
        if (path == null) {
            throw new IllegalArgumentException("Method:install param 'pluginId' can not be empty");
        }
        String pluginId = null;
        try {
            pluginId = load(path);
            if (StringUtils.isEmpty(pluginId)) {
                log.error("Plugin '{}' install failure, this plugin id is empty.", pluginId);
                return false;
            }
            GlobalRegistryInfo.addOperatorPluginInfo(pluginId, PluginOperatorInfo.OperatorType.INSTALL, true);
            if (start(pluginId)) {
                log.info("Plugin '{}' install success", pluginId);
                return true;
            } else {
                log.error("Plugin '{}' install failure", pluginId);
                return false;
            }
        } catch (Exception e) {
            // 说明load成功, 但是没有启动成功, 则卸载该插件
            if (!StringUtils.isEmpty(pluginId)) {
                log.error("Plugin '{}' install failure. {}", pluginId, e.getMessage());
                log.info("Start uninstall plugin '{}' failure", pluginId);
                try {
                    uninstall(pluginId, false);
                } catch (Exception uninstallException) {
                    log.error("Plugin '{}' uninstall failure. {}", pluginId, uninstallException.getMessage());
                }
            }
            throw new PluginException(e);
        } finally {
            if (!StringUtils.isEmpty(pluginId)) {
                GlobalRegistryInfo.setOperatorPluginInfo(pluginId, false);
            }
        }
    }

    private String load(Path path) throws IOException {
        String pluginId;
        if (!Files.exists(path)) {
            throw new FileNotFoundException("Not found this path " + path);
        }
        // 校验插件文件
        pluginLegalVerify.verify(path);
        Path pluginsRoot = pluginManager.getPluginsRoot();
        if (path.getParent().compareTo(pluginsRoot) == 0) {
            // 说明该插件文件存在于插件root目录下。直接加载该插件
            pluginId = pluginManager.loadPlugin(path);
        } else {
            File sourceFile = path.toFile();
            String targetPathString = pluginsRoot.toString() + File.separator + sourceFile.getName();
            Path targetPath = Paths.get(targetPathString);
            if (Files.exists(targetPath)) {
                // 如果存在该文件, 则移动备份
                backup(targetPath, "install-backup", 1);
            }
            PluginFileUtils.createExistFile(targetPath);
            Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
            pluginId = pluginManager.loadPlugin(targetPath);
        }
        return pluginId;
    }

    @Override
    public boolean uninstall(String pluginId, boolean isBackup) {
        if (StringUtils.isEmpty(pluginId)) {
            throw new IllegalArgumentException("Method:uninstall param 'pluginId' can not be empty");
        }
        PluginWrapper pluginWrapper = pluginManager.getPlugin(pluginId);
        if (pluginWrapper == null) {
            throw new PluginException("Plugin uninstall failure, Not found plugin '" + pluginId + "'");
        }
        if (pluginWrapper.getPluginState() == PluginState.STARTED) {
            try {
                pluginFactory.unregister(pluginId);
                pluginFactory.build();
            } catch (Exception e) {
                log.error("Plugin '{}' uninstall failure, {}", pluginId, e.getMessage());
            }
        }
        try {
            if (pluginManager.unloadPlugin(pluginId)) {
                Path pluginPath = pluginWrapper.getPluginPath();
                if (isBackup) {
                    // 将插件文件移到备份文件中
                    backup(pluginPath, "uninstall", 1);
                } else {
                    // 不备份的话。直接删除该文件
                    Files.deleteIfExists(pluginPath);
                }
                log.info("Plugin '{}' uninstall success", pluginId);
                return true;
            } else {
                log.error("Plugin '{}' uninstall failure", pluginId);
                return false;
            }
        } catch (IOException e) {
            String error = String.format("Plugin '%s' uninstall failure: %s", pluginId, e.getMessage());
            log.error(error);
            throw new PluginException(error, e);
        }
    }


    @Override
    public boolean start(String pluginId) {
        if (StringUtils.isEmpty(pluginId)) {
            throw new IllegalArgumentException("Method:start param 'pluginId' can not be empty");
        }
        PluginWrapper pluginWrapper = getPluginWrapper(pluginId, "Start");
        if (pluginWrapper.getPluginState() == PluginState.STARTED) {
            throw new PluginException("This plugin '" + pluginId + "' have already started");
        }
        try {
            PluginState pluginState = pluginManager.startPlugin(pluginId);
            if (pluginState == PluginState.STARTED) {
                GlobalRegistryInfo.addOperatorPluginInfo(pluginId, PluginOperatorInfo.OperatorType.START, false);
                pluginFactory.register(pluginWrapper);
                pluginFactory.build();
                log.info("Plugin '{}' start success", pluginId);
                return true;
            }
            log.error("Plugin '{}' start failure, plugin state is not start. Current plugin state is '{}'",
                    pluginId, pluginState);
        } catch (Exception e) {
            String error = String.format("Plugin '%s' start failure: %s", pluginId, e.getMessage());
            log.error(error);
            log.info("Start stop plugin {}", pluginId);
            try {
                stop(pluginId);
            } catch (Exception stopException) {
                log.error("Plugin '{}' stop failure: {}", pluginId, stopException.getMessage());
            }
            throw new PluginException(e);
        }
        return false;
    }

    @Override
    public boolean stop(String pluginId) {
        if (StringUtils.isEmpty(pluginId)) {
            throw new IllegalArgumentException("Method:stop param 'pluginId' can not be empty");
        }
        PluginWrapper pluginWrapper = getPluginWrapper(pluginId, "Stop");
        if (pluginWrapper.getPluginState() != PluginState.STARTED) {
            throw new PluginException("This plugin '" + pluginId + "' is not started");
        }
        try {
            pluginFactory.unregister(pluginId);
            pluginFactory.build();
        } catch (Exception e) {
            log.error("Plugin '{}' stop failure. {}", pluginId, e.getMessage());
        }
        try {
            pluginManager.stopPlugin(pluginId);
            log.info("Plugin '{}' stop success", pluginId);
            return true;
        } catch (Exception e) {
            String error = String.format("Plugin '%s' stop failure: %s", pluginId, e.getMessage());
            log.error(error);
            throw new PluginException(e);
        }
    }


    @Override
    public boolean uploadPluginAndStart(MultipartFile pluginFile) {
        if (isDev()) {
            throw new PluginException("Plugin cannot be installed in the 'dev' environment");
        }
        if (pluginFile == null) {
            throw new IllegalArgumentException("Method:uploadPluginAndStart param 'pluginFile' can not be null");
        }
        Path path = uploadPlugin(pluginFile);
        if (this.install(path)) {
            log.info("Plugin upload and start success");
            return true;
        } else {
            log.error("Plugin upload and start failure");
            return false;
        }
    }

    @Override
    public boolean installConfigFile(Path path) {
        if (!Files.exists(path)) {
            throw new PluginException("path ' " + path + "'  does not exist!");
        }
        File sourceFile = path.toFile();
        String configPath = integrationConfiguration.pluginConfigFilePath() +
                File.separator + sourceFile.getName();
        try {
            Path targetPath = PluginFileUtils.createExistFile(Paths.get(configPath));
            if (Files.exists(targetPath)) {
                // 如果文件存在, 则移动备份
                backup(targetPath, "install-config-backup", 1);
            }
            Files.copy(path, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            throw new PluginException(e);
        }
    }

    @Override
    public boolean uploadConfigFile(MultipartFile configFile) {
        if (configFile == null) {
            throw new PluginException("Method:uploadConfigFile param 'configFile' can not be null");
        }
        String fileName = configFile.getOriginalFilename();
        String configPath = integrationConfiguration.pluginConfigFilePath() +
                File.separator + fileName;
        try {
            Path targetPath = PluginFileUtils.createExistFile(Paths.get(configPath));
            if (Files.exists(targetPath)) {
                // 如果文件存在, 则拷贝备份
                backup(targetPath, "upload-config-backup", 2);
            }
            // 然后写入数据到该文件
            Files.write(targetPath, configFile.getBytes());
            return true;
        } catch (IOException e) {
            throw new PluginException(e);
        }
    }

    @Override
    public boolean backupPlugin(Path path, String sign) {
        Objects.requireNonNull(path);
        return backup(path, sign, 2);
    }


    @Override
    public boolean backupPlugin(String pluginId, String sign) {
        PluginWrapper wrapper = getPluginWrapper(pluginId, "BackupPlugin by pluginId");
        return backupPlugin(wrapper.getPluginPath(), sign);
    }

    @Override
    public List<PluginInfo> getPluginInfo() {
        List<PluginWrapper> startedPlugins = pluginManager.getPlugins();
        List<PluginInfo> pluginInfos = new ArrayList<>();
        if (startedPlugins == null) {
            return pluginInfos;
        }
        return startedPlugins.stream()
                .filter(Objects::nonNull)
                .map(this::getPluginInfo)
                .collect(Collectors.toList());
    }


    @Override
    public PluginInfo getPluginInfo(String pluginId) {
        PluginWrapper pluginWrapper = pluginManager.getPlugin(pluginId);
        if (pluginWrapper == null) {
            throw new PluginException("Not found plugin '" + pluginId + "'");
        }
        return getPluginInfo(pluginWrapper);
    }

    /**
     * 通过PluginWrapper得到插件信息
     *
     * @param pluginWrapper pluginWrapper
     * @return PluginInfo
     */
    private PluginInfo getPluginInfo(PluginWrapper pluginWrapper) {
        return new PluginInfo(pluginWrapper.getDescriptor(), pluginWrapper.getPluginState(),
                pluginWrapper.getPluginPath().toAbsolutePath().toString(),
                pluginManager.getRuntimeMode().toString());
    }


    @Override
    public Set<String> getPluginFilePaths() {
        RuntimeMode environment = integrationConfiguration.environment();
        Set<String> paths = new HashSet<>();
        if (environment == RuntimeMode.DEVELOPMENT) {
            paths.add(integrationConfiguration.pluginPath());
            return paths;
        }
        List<File> files = org.pf4j.util.FileUtils.getJars(Paths.get(integrationConfiguration.pluginPath()));
        return files.stream()
                .filter(Objects::nonNull)
                .map(File::getAbsolutePath)
                .collect(Collectors.toSet());
    }

    @Override
    public List<PluginWrapper> getPluginWrapper() {
        return pluginManager.getPlugins();
    }

    @Override
    public PluginWrapper getPluginWrapper(String pluginId) {
        return pluginManager.getPlugin(pluginId);
    }

    /**
     * 上传插件
     *
     * @param pluginFile 插件文件
     * @return 返回上传的插件路径
     */
    protected Path uploadPlugin(MultipartFile pluginFile) {
        if (pluginFile == null) {
            throw new IllegalArgumentException("Method:uploadPlugin param 'pluginFile' can not be null");
        }
        // 获取文件的后缀名
        String fileName = pluginFile.getOriginalFilename();
        assert fileName != null;
        String suffixName = fileName.substring(fileName.lastIndexOf('.') + 1);
        //检查文件格式是否合法
        if (StringUtils.isEmpty(suffixName)) {
            throw new IllegalArgumentException("Invalid file type, please select .jar or .zip file");
        }
        if (!BaseConstants.Suffix.JAR.equalsIgnoreCase(suffixName) &&
                !BaseConstants.Suffix.ZIP.equalsIgnoreCase(suffixName)) {
            throw new IllegalArgumentException("Invalid file type, please select .jar or .zip file");
        }
        String tempPathString = integrationConfiguration.uploadTempPath() + File.separator + fileName;
        Path tempPath;
        try {
            tempPath = PluginFileUtils.createExistFile(Paths.get(tempPathString));
            Files.write(tempPath, pluginFile.getBytes());
        } catch (IOException e) {
            throw new PluginException(e);
        }
        try {
            Path verifyPath = pluginLegalVerify.verify(tempPath);
            if (verifyPath != null) {
                String targetPathString = pluginManager.getPluginsRoot().toString() +
                        File.separator + fileName;
                Path targetPluginPath = Paths.get(targetPathString);
                if (Files.exists(targetPluginPath)) {
                    // 存在则拷贝一份
                    backup(targetPluginPath, "upload", 2);
                }
                // 拷贝校验的路径到插件路径下
                Files.copy(verifyPath, targetPluginPath, StandardCopyOption.REPLACE_EXISTING);
                // 删除临时文件
                Files.deleteIfExists(tempPath);
                return targetPluginPath;
            } else {
                Exception exception =
                        new Exception(fileName + " verify failure, verifyPath is null");
                verifyFailureDelete(tempPath, exception);
                throw exception;
            }
        } catch (Exception e) {
            // 出现异常, 删除刚才上传的临时文件
            verifyFailureDelete(tempPath, e);
            throw new PluginException(e);
        }
    }


    /**
     * 得到插件包装类
     *
     * @param pluginId 插件id
     * @param errorMsg 错误信息
     * @return PluginWrapper
     */
    protected PluginWrapper getPluginWrapper(String pluginId, String errorMsg) {
        PluginWrapper pluginWrapper = pluginManager.getPlugin(pluginId);
        if (pluginWrapper == null) {
            throw new PluginException(errorMsg + " -> Not found plugin " + pluginId);
        }
        return pluginWrapper;
    }

    /**
     * 校验文件失败后, 删除临时文件
     *
     * @param tempPluginFile 临时文件路径
     * @param e              异常信息
     */
    protected void verifyFailureDelete(Path tempPluginFile, Exception e) {
        try {
            Files.deleteIfExists(tempPluginFile);
        } catch (IOException e1) {
            throw new PluginException("Verify failure and delete temp file failure : " + e.getMessage(), e);
        }
    }

    /**
     * 备份
     *
     * @param sourcePath 源文件的路径
     * @param sign       文件标志
     * @param type       类型 1移动 2拷贝
     * @return 结果
     */
    protected boolean backup(Path sourcePath, String sign, int type) {
        try {
            if (isDev()) {
                // 如果是开发环境, 则不进行备份
                return false;
            }
            if (sourcePath == null) {
                return false;
            }
            if (!Files.exists(sourcePath)) {
                log.error("Path '{}' does not exist", sourcePath);
                return false;
            }
            String fileName = sourcePath.getFileName().toString();
            String targetName = integrationConfiguration.backupPath() + File.separator;
            if (!StringUtils.isEmpty(sign)) {
                targetName = targetName + "[" + sign + "]";
            }
            targetName = targetName + "[" + getNowTimeByFormat() + "]";
            Path target = Paths.get(targetName + "_" + fileName);
            if (!Files.exists(target.getParent())) {
                Files.createDirectories(target.getParent());
            }
            File sourceFile = sourcePath.toFile();
            if (sourceFile.length() == 0) {
                // 源文件字节为0, 说明为删除的插件。不需要备份
                return true;
            }
            if (type == 1) {
                // 是移动的话, 则删除源文件
                Files.move(sourcePath, target, StandardCopyOption.REPLACE_EXISTING);
            } else {
                // 拷贝
                Files.copy(sourcePath, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return true;
        } catch (IOException e) {
            log.error("Backup plugin jar '{}' failure. {}", sourcePath, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取现在的时间
     *
     * @return String
     */
    protected String getNowTimeByFormat() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return FORMAT.format(localDateTime);
    }

    /**
     * 是否是开发环境
     *
     * @return boolean
     */
    protected boolean isDev() {
        return integrationConfiguration.environment() == RuntimeMode.DEVELOPMENT;
    }

}
