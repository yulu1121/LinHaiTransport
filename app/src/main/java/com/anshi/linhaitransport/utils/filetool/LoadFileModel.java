package com.anshi.linhaitransport.utils.filetool;

import android.text.TextUtils;

import me.jessyan.progressmanager.ProgressManager;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by 12457 on 2017/8/21.
 */

public class LoadFileModel {

    //使用该方法来下载文件
    public static void loadPdfFile(String url, Callback<ResponseBody> callback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                .client(ProgressManager.getInstance().with(new OkHttpClient.Builder())
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        LoadFileApi mLoadFileApi = retrofit.create(LoadFileApi.class);
        if (!TextUtils.isEmpty(url)) {
            Call<ResponseBody> call = mLoadFileApi.loadPdfFile(url);
            call.enqueue(callback);
        }

    }
}
