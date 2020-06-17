package com.basic.example.main.quartz;

import java.util.Map;

import org.quartz.Job;

/**
 * <p>
 * Quartz框架job定义的统一接口
 * </p>
 *
 * @author isaac 2020/6/16 17:26
 * @since 1.0
 */
public interface QuartzJob {

    /**
     * 是否启用
     * @return true 启用。false 禁用
     */
    boolean enable();


    /**
     * job 名称
     * @return String
     */
    String jobName();

    /**
     * 触发器名称
     * @return String
     */
    String triggerName();

    /**
     * cron 表达式
     * @return  cron 表达式
     */
    String cron();

    /**
     * 延迟执行秒数
     * @return 秒数
     */
    int delaySeconds();

    /**
     * job 执行类型
     * @return Job 实现类
     */
    Class<? extends Job>jobClass();

    /**
     * 传入到job中的数据
     * @return Map
     */
    Map<String, Object> jobData();

}
