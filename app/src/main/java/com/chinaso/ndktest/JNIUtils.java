package com.chinaso.ndktest;

public class JNIUtils {
    static {
        System.loadLibrary("JNIHello");
    }
    public static native String  sayHelloFromJNI();

    public static native int[] getGrayImage(int[] pixels, int w, int h) ;


}
