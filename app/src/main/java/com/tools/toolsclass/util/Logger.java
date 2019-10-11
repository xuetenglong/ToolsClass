package com.tools.toolsclass.util;

import android.util.Log;

import com.tools.toolsclass.BuildConfig;


/**
 * Created by sp on 17-6-6.
 */

public class Logger {

    public static void i(String tag, String msg){
        if(BuildConfig.DEBUG){
            if(tag!=null &&msg!=null ){
                Log.i(tag,msg);
            }

        }
    }

    public static void d(String tag, String msg){
        if(BuildConfig.DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void e(String tag, String msg){
        if(BuildConfig.DEBUG){
            Log.e(tag,msg);
        }
    }

    public static void v(String tag, String msg){
        if(BuildConfig.DEBUG){
            Log.v(tag,msg);
        }
    }

    public static void w(String tag, String msg){
        if(BuildConfig.DEBUG){
            Log.w(tag,msg);
        }
    }
}
