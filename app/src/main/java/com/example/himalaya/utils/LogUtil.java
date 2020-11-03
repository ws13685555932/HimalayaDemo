package com.example.himalaya.utils;

import android.util.Log;

public class LogUtil {
    public static String sTAG = "LogUtil";

    public static boolean sIsDebug = true;

    public static void init(String baseTag, boolean isDebug) {
        sTAG = baseTag;
        sIsDebug = isDebug;
    }

    public static void d(String TAG, String content) {
        if (sIsDebug) {
            Log.d(sTAG, content);
        }
    }

    public static void v(String TAG, String content) {
        if (sIsDebug) {
            Log.v(TAG, content);
        }
    }

    public static void e(String TAG, String content) {
        if (sIsDebug) {
            Log.e(TAG, content);
        }
    }

    public static void i(String TAG, String content) {
        if (sIsDebug) {
            Log.i(TAG, content);
        }
    }

    public static void w(String TAG, String content) {
        if (sIsDebug) {
            Log.w(TAG, content);
        }
    }

    public static void d(String content) {
        d(sTAG, content);
    }

    public static void i(String content) {
        i(sTAG, content);
    }

    public static void w(String content) {
        w(sTAG, content);
    }

    public static void e(String content) {
        e(sTAG, content);
    }

    public static void v(String content) {
        v(sTAG, content);
    }
}
