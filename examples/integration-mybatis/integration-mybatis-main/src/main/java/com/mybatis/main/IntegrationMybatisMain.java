package com.mybatis.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/6/19 11:59
 * @since 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.mybatis.main"})
@MapperScan("com.mybatis.main.mapper")
public class IntegrationMybatisMain {

    public static void main(String[] args) {

        SpringApplication.run(IntegrationMybatisMain.class, args);
    }

}
