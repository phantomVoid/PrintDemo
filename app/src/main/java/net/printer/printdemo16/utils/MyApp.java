package net.printer.printdemo16.utils;

import android.app.Application;
import android.content.Context;

import org.xutils.x;

/**
 * 作者：  pyfysf
 * <p>
 * qq:  337081267
 * <p>
 * CSDN:    http://blog.csdn.net/pyfysf
 * <p>
 * 个人博客：    http://wintp.top
 * <p>
 * 时间： 2018/05/2018/5/10 8:09
 * <p>
 * 邮箱：  pyfysf@163.com
 */
public class MyApp extends Application {
    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        sContext = this;
    }
}
