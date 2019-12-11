package com.anshi.linhaitransport.view.filemanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.base.BaseActivity;
import com.anshi.linhaitransport.entry.FileListEntry;
import com.anshi.linhaitransport.utils.Constants;
import com.anshi.linhaitransport.utils.DialogBuild;
import com.anshi.linhaitransport.utils.SharedPreferenceUtils;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.ToastUtils;
import com.anshi.linhaitransport.utils.Utils;
import com.anshi.linhaitransport.utils.filetool.LoadFileModel;
import com.anshi.linhaitransport.view.file.FileDisplayActivity;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class FileManagerActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private static final String DOWN_DIR = Environment.getExternalStorageDirectory()
            + File.separator+"Download";
    private KProgressHUD commonProgressDialog;
    private ProgressInfo mLastDownloadingInfo;
    private List<FileListEntry.DataBean> mList = new ArrayList<>();
    private CommonAdapter<FileListEntry.DataBean> commonAdapter;
    private SimpleDateFormat simpleDateFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flie_manager);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        initView();
        commonProgressDialog = DialogBuild.getBuild().createCommonProgressDialog(this, "下载中");
        getFileList();
    }


    private void getFileList(){
        final KProgressHUD kProgressHUD = DialogBuild.getBuild().createCommonLoadDialog(this,"正在加载");
        mService.getFileList()
                .map(new Func1<ResponseBody, ResponseBody>() {
                    @Override
                    public ResponseBody call(ResponseBody responseBody) {
                        return responseBody;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ResponseBody>() {
                    @Override
                    public void call(ResponseBody responseBody) {
                        if (null!=kProgressHUD){
                            kProgressHUD.dismiss();
                        }
                        try {
                            String string = responseBody.string();
                            if (Utils.isGoodJson(string)){
                                Gson gson = new Gson();
                                FileListEntry fileListEntry = gson.fromJson(string, FileListEntry.class);
                                if (fileListEntry.getCode()== Constants.SUCCESS_CODE){
                                    if (fileListEntry.getData()!=null&&fileListEntry.getData().size()>0){
                                        mList.clear();
                                        mList.addAll(fileListEntry.getData());
                                        commonAdapter.notifyDataSetChanged();
                                        for (int i = 0; i <mList.size() ; i++) {
                                            String profile = Constants.IMAGE_URL+mList.get(i).getUpFilePath();
                                            ProgressManager.getInstance().addResponseListener(profile, new ProgressListener() {
                                                @Override
                                                public void onProgress(ProgressInfo progressInfo) {
                                                    if (mLastDownloadingInfo == null) {
                                                        mLastDownloadingInfo = progressInfo;
                                                    }

                                                    //因为是以请求开始时的时间作为 Id ,所以值越大,说明该请求越新
                                                    if (progressInfo.getId() < mLastDownloadingInfo.getId()) {
                                                        return;
                                                    } else if (progressInfo.getId() > mLastDownloadingInfo.getId()) {
                                                        mLastDownloadingInfo = progressInfo;
                                                    }

                                                    int progress = mLastDownloadingInfo.getPercent();
                                                    commonProgressDialog.setProgress(progress);
                                                    Log.d("xxx", "--Download-- " + progress + " %  " + mLastDownloadingInfo.getSpeed() + " byte/s  " + mLastDownloadingInfo.toString());
                                                    if (mLastDownloadingInfo.isFinish()) {
                                                        //说明已经下载完成
                                                        commonProgressDialog.dismiss();
                                                        Log.d("xxx", "--Download-- finish");
                                                    }
                                                }

                                                @Override
                                                public void onError(long id, Exception e) {
                                                    e.printStackTrace();
                                                    new Handler().post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            commonProgressDialog.dismiss();
                                                        }
                                                    });

                                                }
                                            });
                                        }
                                    }
                                }else {
                                    ToastUtils.showShortToast(mContext,fileListEntry.getMsg());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (null!=kProgressHUD){
                            kProgressHUD.dismiss();
                        }
                        throwable.printStackTrace();
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorBlue);
    }

    private void initView() {
        TextView tilteTv = findViewById(R.id.title_tv);
        tilteTv.setText("文件管理");
        mRecyclerView = findViewById(R.id.file_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        completeRecycler();
    }
    private void completeRecycler(){
      commonAdapter = new CommonAdapter<FileListEntry.DataBean>(this,R.layout.item_file,mList) {
          @Override
          protected void convert(ViewHolder holder, final FileListEntry.DataBean dataBean, int position) {
                TextView nameTv = holder.getView(R.id.file_name_tv);
                TextView timeTv = holder.getView(R.id.time_tv);
              try {
                  Date parse = simpleDateFormat.parse(dataBean.getCreateDate());
                  String mintuteTime = Utils.getMintuteTime(parse);
                  timeTv.setText(mintuteTime);
              } catch (ParseException e) {
                  e.printStackTrace();
              }
                nameTv.setText(dataBean.getFileTitle());

                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openFile(Constants.IMAGE_URL+dataBean.getUpFilePath(),dataBean.getUpFileName());
                    }
                });
          }
      };
      mRecyclerView.setAdapter(commonAdapter);
    }



    //打开文件
    private void openFile(String filePath,String fileName) {
        boolean hasLoad = SharedPreferenceUtils.getBoolean(mContext, "hasLoad");
        if (hasLoad){
            Intent intentDoc = new Intent(this,FileDisplayActivity.class);
            intentDoc.putExtra("path",filePath);
            intentDoc.putExtra("fileName",fileName);
            startActivity(intentDoc);
        }else {
            if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                Toast.makeText(this,"当前SD不可用",Toast.LENGTH_LONG).show();
                return;
            }
            try {
                File file = new File(DOWN_DIR, fileName);
                if (file.exists()){
                    QbSdk.openFileReader(this, file.getAbsolutePath(), null, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            Log.e("xxx",s);
                        }
                    });
                } else {
                    downLoadFile(filePath,fileName);
                }

            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShortToast(mContext, "已下载过了");
            }

        }
    }

    private void downLoadFile(final String url, final String fileName) {
        commonProgressDialog.show();
        LoadFileModel.loadPdfFile(url, new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {
                        is = responseBody.byteStream();
                        final File file = new File(DOWN_DIR, fileName);
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.flush();
                        QbSdk.openFileReader(mContext, file.getAbsolutePath(), null, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String s) {
                                Log.e("xxx",s);
                            }
                        });
                        ToastUtils.showLongToast(FileManagerActivity.this, "下载成功,保存在Download文件夹下");
                    }
                } catch (Exception e) {
                    commonProgressDialog.dismiss();
                    ProgressManager.getInstance().notifyOnErorr(url,e);
                    ToastUtils.showLongToast(FileManagerActivity.this, "下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                ToastUtils.showShortToast(FileManagerActivity.this, "下载失败");
                commonProgressDialog.dismiss();
            }
        });

    }


}
