package com.ed.demo.i18ndemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author ed
 */
@MapperScan("com.ed.demo.i18ndemo.mapper")
@SpringBootApplication
public class I18nDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(I18nDemoApplication.class, args);
    }

}
