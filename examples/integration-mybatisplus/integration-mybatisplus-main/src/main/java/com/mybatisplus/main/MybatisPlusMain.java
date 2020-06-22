package com.mybatisplus.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * 主启动程序
 * </p>
 *
 * @author isaac 2020/6/22 13:48
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.mybatisplus.main"})
public class MybatisPlusMain {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusMain.class, args);
    }

}
