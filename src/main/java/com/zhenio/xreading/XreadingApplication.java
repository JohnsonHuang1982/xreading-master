package com.zhenio.xreading;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


/***
 * 啟動springBoot
 * 
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan("com.zhenio.xreading.mapper")
public class XreadingApplication {

    public static void main(String[] args) {
        SpringApplication.run(XreadingApplication.class, args);
    }

}
