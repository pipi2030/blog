package com.neutech;

import com.neutech.service.RedisService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan(basePackages = {"com.neutech.mapper"})
public class BootStater {
    @Autowired
    private RedisService redisService;
    public static void main(String[] args) {
        SpringApplication.run(BootStater.class,args);
    }
    @PostConstruct
    public void insertDataToRedis(){
//        向redis批量存入数据
//        redisService.insertRedis();
    }
}
