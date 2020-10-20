package com.mybatis.main.rest;

import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.github.thestyleofme.plugin.framework.integration.application.PluginApplication;
import com.github.thestyleofme.plugin.framework.integration.operator.PluginOperator;
import com.github.thestyleofme.plugin.framework.integration.operator.module.PluginInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 插件jar 包测试功能
 * </p>
 *
 * @author isaac 2020/6/19 11:51
 * @since 1.0
 */
@RestController
@RequestMapping("/plugin")
public class PluginResource {

    private static final Logger LOG = LoggerFactory.getLogger(PluginResource.class);

    private final PluginOperator pluginOperator;

    public PluginResource(PluginApplication pluginApplication) {
        this.pluginOperator = pluginApplication.getPluginOperator();
    }

    /**
     * 获取插件信息
     *
     * @return 返回插件信息
     */
    @GetMapping
    public List<PluginInfo> getPluginInfo() {
        return pluginOperator.getPluginInfo();
    }

    /**
     * 获取插件jar文件名
     *
     * @return 获取插件文件名。只在生产环境显示
     */
    @GetMapping("/files")
    public Set<String> getPluginFilePaths() {
        try {
            return pluginOperator.getPluginFilePaths();
        } catch (Exception e) {
            LOG.error("getPluginFilePaths error", e);
            return Collections.emptySet();
        }
    }


    /**
     * 根据插件id停止插件
     *
     * @param id 插件id
     * @return 返回操作结果
     */
    @PostMapping("/stop/{id}")
    public String stop(@PathVariable("id") String id) {
        try {
            if (pluginOperator.stop(id)) {
                return String.format("plugin '%s' stop success", id);
            } else {
                return String.format("plugin '%s' stop failure", id);
            }
        } catch (Exception e) {
            LOG.error("stop error", e);
            return String.format("plugin '%s' stop failure, %s", id, e.getMessage());
        }
    }

    /**
     * 根据插件id启动插件
     *
     * @param id 插件id
     * @return 返回操作结果
     */
    @PostMapping("/start/{id}")
    public String start(@PathVariable("id") String id) {
        try {
            if (pluginOperator.start(id)) {
                return String.format("plugin '%s' start success", id);
            } else {
                return String.format("plugin '%s' start failure", id);
            }
        } catch (Exception e) {
            LOG.error("start error", e);
            return String.format("plugin '%s' start failure, %s", id, e.getMessage());
        }
    }


    /**
     * 根据插件id卸载插件
     *
     * @param id 插件id
     * @return 返回操作结果
     */
    @PostMapping("/uninstall/{id}")
    public String uninstall(@PathVariable("id") String id,
                            @RequestParam(value = "isBack", required = false) Boolean isBack) {
        try {
            if (isBack == null) {
                isBack = true;
            }
            if (pluginOperator.uninstall(id, isBack)) {
                return String.format("plugin '%s' uninstall success", id);
            } else {
                return String.format("plugin '%s' uninstall failure", id);
            }
        } catch (Exception e) {
            LOG.error("uninstall error", e);
            return String.format("plugin '%s' uninstall failure. %s", id, e.getMessage());
        }
    }


    /**
     * 根据插件路径安装插件。该插件jar必须在服务器上存在。注意: 该操作只适用于生产环境
     *
     * @param path 插件路径名称
     * @return 操作结果
     */
    @PostMapping("/installByPath")
    public String install(@RequestParam("path") String path) {
        try {
            if (pluginOperator.install(Paths.get(path))) {
                return "installByPath success";
            } else {
                return "installByPath failure";
            }
        } catch (Exception e) {
            LOG.error("installByPath error", e);
            return "installByPath failure : " + e.getMessage();
        }
    }


    /**
     * 上传并安装插件。注意: 该操作只适用于生产环境
     *
     * @param multipartFile 上传文件 multipartFile
     * @return 操作结果
     */
    @PostMapping("/uploadInstallPluginJar")
    public String install(@RequestParam("jarFile") MultipartFile multipartFile) {
        try {
            if (pluginOperator.uploadPluginAndStart(multipartFile)) {
                return "install success";
            } else {
                return "install failure";
            }
        } catch (Exception e) {
            LOG.error("install error", e);
            return "install failure : " + e.getMessage();
        }
    }


    /**
     * 上传插件的配置文件。注意: 该操作只适用于生产环境
     *
     * @param multipartFile 上传文件 multipartFile
     * @return 操作结果
     */
    @PostMapping("/uploadPluginConfigFile")
    public String uploadConfig(@RequestParam("configFile") MultipartFile multipartFile) {
        try {
            if (pluginOperator.uploadConfigFile(multipartFile)) {
                return "uploadConfig success";
            } else {
                return "uploadConfig failure";
            }
        } catch (Exception e) {
            LOG.error("uploadConfig error", e);
            return "uploadConfig failure : " + e.getMessage();
        }
    }


    /**
     * 备份插件。注意: 该操作只适用于生产环境
     *
     * @param pluginId 插件id
     * @return 操作结果
     */
    @PostMapping("/back/{pluginId}")
    public String backupPlugin(@PathVariable("pluginId") String pluginId) {
        try {
            if (pluginOperator.backupPlugin(pluginId, "testBack")) {
                return "backupPlugin success";
            } else {
                return "backupPlugin failure";
            }
        } catch (Exception e) {
            LOG.error("backup plugin error", e);
            return "backupPlugin failure : " + e.getMessage();
        }
    }

}
