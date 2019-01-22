package com.android.volley.okhttp.builder;

import com.android.volley.okhttp.OkHttpUtils;
import com.android.volley.okhttp.request.OtherRequest;
import com.android.volley.okhttp.request.RequestCall;

public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
