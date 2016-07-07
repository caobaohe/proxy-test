package com.lxz.javassist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by xiaolezheng on 16/7/7.
 */

class Hello {
    public void say() {
        System.out.println("Hello");
    }

    public static void sayHello(){
        System.out.println("say Hello");
    }
}

@Slf4j
public class Test {
    public static void main(String[] args) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("com.lxz.javassist.Hello");
        CtMethod m = cc.getDeclaredMethod("say");
        m.insertBefore("{ System.out.println(\"Hello.say():\"); }");
        m.insertBefore("{ System.out.println(\"Hello.say():\"); }");
        m.insertAfter("{ System.out.println(\"Hello.say():\"); }");
        m.insertAfter("{ com.lxz.javassist.Hello.sayHello();}");
        Class c = cc.toClass();
        Hello h = (Hello)c.newInstance();
        h.say();

        Hello h1 = new Hello();
        log.info("h.class: {}", h.getClass());
        log.info("h1.class: {}", h1.getClass());
    }
}