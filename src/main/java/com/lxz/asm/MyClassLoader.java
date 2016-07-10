package com.lxz.asm;

public class MyClassLoader extends ClassLoader {
    private static final MyClassLoader instance = new MyClassLoader();

    private MyClassLoader(){
    }

    public static MyClassLoader getInstance(){
        return instance;
    }

    public Class defineClass(String name, byte[] b) {
        return defineClass(name, b, 0, b.length);
    }
}