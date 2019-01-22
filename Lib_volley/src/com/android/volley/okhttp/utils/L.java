package com.android.volley.okhttp.utils;

import com.android.volley.okhttp.log.Constants;

import android.util.Log;

public class L
{
    public static void e(String msg)
    {
        if (Constants.isDebug)
        {
            Log.e("OkHttp", msg);
        }
    }

    public static void i(String tag,String msg)
    {
        if (Constants.isDebug)
        {
            Log.i(tag, msg);
        }
    }
    
    public static void e(String tag,String msg)
    {
        if (Constants.isDebug)
        {
            Log.e(tag, msg);
        }
    }
}

