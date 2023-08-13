package com.neutech;

import com.neutech.service.RedisService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import javax.annotation.PostConstruct;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class ESStater {
    @DubboReference(interfaceClass = RedisService.class,interfaceName = "${es.service.name}",version = "${es.service.version}")
    private RedisService redisService;
    public static void main(String[] args) {
        SpringApplication.run(ESStater.class,args);
    }
    @PostConstruct
    public void insertDataToRedis(){
//        向redis里批量插入数据
//        redisService.insertRedis();
    }
}
