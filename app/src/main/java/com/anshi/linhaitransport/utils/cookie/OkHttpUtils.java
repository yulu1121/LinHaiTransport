package com.anshi.linhaitransport.utils.cookie;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import me.jessyan.progressmanager.ProgressManager;
import okhttp3.OkHttpClient;

/**
 *
 * Created by yulu on 2018/4/27.
 */

public class OkHttpUtils {
    public static OkHttpClient okHttpClient;
    public static void initOkHttp(Context context) {
        okHttpClient =  ProgressManager.getInstance().with(new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS))
                .build();
    }
}
