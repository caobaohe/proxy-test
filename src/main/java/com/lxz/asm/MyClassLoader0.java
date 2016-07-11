package com.lxz.asm;

public class MyClassLoader0 extends ClassLoader {
    private static final MyClassLoader0 instance = new MyClassLoader0();

    private MyClassLoader0(){
    }

    public static MyClassLoader0 getInstance(){
        return instance;
    }

    public Class defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}