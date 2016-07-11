package com.lxz.asm;

import lombok.extern.slf4j.Slf4j;

/**
 * 要修改的方法中，准备添加的我们自己的代码逻辑
 */
@Slf4j
public class AopInteceptor {

    public static void before() {
        log.info(".......before().......");
    }

    public static void after() {
        log.info(".......after().......");
    }


}