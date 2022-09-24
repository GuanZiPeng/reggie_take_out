package com.guan.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 雷吉应用程序启动类
 *
 * @author GeZ
 * @date 2022/09/22
 */
@Slf4j //日志输出注解
@SpringBootApplication //SpringBoot启动类注解
@ServletComponentScan //加载过滤器
@EnableTransactionManagement
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class,args);
        log.info("项目启动成功！");
    }
}
