package com.example.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyClassloader extends ClassLoader {

    private String path;

    public MyClassloader(ClassLoader parent, String path) {
        super(parent);
        this.path = path;
    }

    @Override
    protected Class<?> findClass(String name) {

        Class hello = null;
        byte[] helloArr = new byte[0];
        try {
            helloArr = getHello(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        hello = defineClass(name, helloArr, 0, helloArr.length);

        return hello;
    }

    private byte[] getHello(String path) throws Exception {
        // todo load class file
        File file = new File(path);
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] bytes = new byte[1024];
        int size;
        while ((size = in.read(bytes)) != -1) {
            out.write(bytes, 0, size);
        }
        byte[] outByte = out.toByteArray();
        for (int i = 0; i < outByte.length; i++) {
            outByte[i] = (byte) (255 - outByte[i]);
        }
        return outByte;
    }


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        MyClassloader myClassloader = new MyClassloader(ClassLoader.getSystemClassLoader(), "C:\\Users\\CPIC\\Desktop\\jvm\\class\\Hello.xlass");
        Class<?> hello = Class.forName("Hello", true, myClassloader);
        Method method = hello.getMethod("hello");
        Object o = hello.newInstance();
        method.invoke(o);
    }

}
