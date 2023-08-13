package com.neutech.service.impl;

import com.neutech.service.RedisService;
import com.neutech.service.TestService;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(interfaceClass = TestService.class,interfaceName = "${db.service.name}",version = "${db.service.version}")
public class TestServiceImpl implements TestService {
    @Override
    public void printHello() {
        System.out.println("Hello World");
    }
}
