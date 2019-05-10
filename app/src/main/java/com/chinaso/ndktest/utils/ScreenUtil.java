package com.chinaso.ndktest.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {
    public static final String WIDTH = "width";// 宽度常量
    public static final String HEIGHT = "height";// 高度常量

    private WindowManager windowManager;
    private DisplayMetrics metrics = new DisplayMetrics();

    public ScreenUtil(Context context) {
        this.windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    /**
     * 获取手机屏幕的宽度，单位px
     * @param s 宽或高类型
     * @return
     */
    public int getScreenSize(String s) {
        // 获取显示度量，该显示度量描述了显示的大小和密度
        windowManager.getDefaultDisplay().getMetrics(metrics);
        int w_or_h = 0;
        switch (s){
            case WIDTH://宽度
                w_or_h = metrics.widthPixels;
                break;
            case HEIGHT:// 高度
                w_or_h = metrics.heightPixels;
                break;
        }
        return w_or_h;// 返回手机屏幕的宽度或者高度
    }

}
