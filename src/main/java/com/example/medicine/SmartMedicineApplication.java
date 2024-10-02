package com.example.medicine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * 应用启动类
 *
 * @author XUEW
 */
@SpringBootApplication
@MapperScan("com.example.medicine.dao")
//@ServletComponentScan(basePackages = "com.example.medicine.filter")
public class SmartMedicineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartMedicineApplication.class, args);
    }

}
