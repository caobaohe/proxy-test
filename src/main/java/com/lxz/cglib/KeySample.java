package com.lxz.cglib;

import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.KeyFactory;

@Slf4j
public class KeySample {
    private interface MyFactory {
        Object newInstance(int a, char[] b, String d);
    }

    public static void main(String[] args) {
        MyFactory f = (MyFactory) KeyFactory.create(MyFactory.class);
        Object key1 = f.newInstance(20, new char[]{'a', 'b'}, "hello");
        Object key2 = f.newInstance(20, new char[]{'a', 'b'}, "hello");
        Object key3 = f.newInstance(20, new char[]{'a', '_'}, "hello");

        log.info("{}", key1.equals(key2));
        log.info("{}", key2.equals(key3));
    }
}