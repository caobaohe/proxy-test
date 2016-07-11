package com.lxz.asm;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
public class Hello {

    // 声明 一个常量
    public static final String FLAG = "我是常量";

    // 普通方法
    public void display(){
        for (int i = 0; i < 8; i++) {
            log.info(">>>>>>>>>>" + FLAG);
        }
    }

    // 带有List返回值
    public List<String> testList(){
        List<String> list = new ArrayList<>();
        list.add("Tome");
        list.add("Jack");
        list.add("Lily");
        log.info(">>>>>>>>>>testList > list.size = " + list.size());
        return list;
    }

    // 泛型返回值，包含List和Map
    // 两个参数，参数为Map动参
    public List<Map<String, String>> testMapList(boolean val, Map<String, String>... map){
        List<Map<String, String>> list = new ArrayList<>();
        if(val){
            for (Map<String, String> m : map) {
                list.add(m);
            }
        }
        log.info(">>>>>>>>>>testMapList > list.size = " + list.size());
        return list;
    }

}