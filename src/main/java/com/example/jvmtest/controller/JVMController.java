package com.example.jvmtest.controller;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: XueWeiDong
 * @Description:
 * @Date: 14:42 2020/1/14
 */
@RestController
@RequestMapping("/jvm")
public class JVMController {

    @RequestMapping("/args")
    public HttpEntity<?> jvmArgs(){
        List<String> paramters = ManagementFactory.getRuntimeMXBean().getInputArguments();
        return new HttpEntity(paramters);
    }

    @RequestMapping("/outOfMem")
    public HttpEntity<?> outOfMem(){
        List<Object> list = new ArrayList<>();
        for (;;){
            list.add(new Object());
        }
    }
}
