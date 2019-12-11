package com.anshi.linhaitransport.view.file;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.anshi.linhaitransport.R;
import com.anshi.linhaitransport.utils.DialogBuild;
import com.anshi.linhaitransport.utils.StatusBarUtils;
import com.anshi.linhaitransport.utils.WeakHandler;
import com.anshi.linhaitransport.utils.filetool.LoadFileModel;
import com.anshi.linhaitransport.utils.filetool.SuperFileView2;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import me.jessyan.progressmanager.ProgressListener;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.progressmanager.body.ProgressInfo;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FileDisplayActivity extends AppCompatActivity {


    private String TAG = "FileDisplayActivity";
    //Tencent 提供的TBS阅读浏览功能，不借助第三方软件打开office和pdf文件
    private SuperFileView2 mSuperFileView;

    private String filePath;
    private TextView mTextView;
    private static final String DOWN_DIR = Environment.getExternalStorageDirectory()
            + File.separator+"Download";
    private String fileName;
    private KProgressHUD commonProgressDialog;
    private WeakHandler weakHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int progress = msg.arg1;
            if (progress==100){
                commonProgressDialog.setProgress(progress);
                commonProgressDialog.dismiss();
            }else {
                commonProgressDialog.setProgress(progress);
            }
            return true;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|WindowManager.LayoutParams.FLAG_FULLSCREEN);
        commonProgressDialog = DialogBuild.getBuild().createCommonProgressDialog(this, "下载中");
        init();
    }


    /**
     * 初始化
     */
    public void init() {
        mTextView = (TextView) findViewById(R.id.file_album_name);
        fileName = getIntent().getStringExtra("fileName");
        int lastIndexOf = fileName.lastIndexOf(".");
        String substring = fileName.substring(0, lastIndexOf);
        mTextView.setText(substring);
        mSuperFileView = (SuperFileView2) findViewById(R.id.mSuperFileView);
        mSuperFileView.setOnGetFilePathListener(new SuperFileView2.OnGetFilePathListener() {
            @Override
            public void onGetFilePath(SuperFileView2 mSuperFileView2) {
                getFilePathAndShowFile(mSuperFileView);
            }
        });

        Intent intent = this.getIntent();
        String path = (String) intent.getSerializableExtra("path");
        if (!TextUtils.isEmpty(path)) {
            Log.d(TAG, "文件path:" + path);
            setFilePath(path);
        }
        mSuperFileView.show();
        ProgressManager.getInstance().addResponseListener(path, new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                int percent = progressInfo.getPercent();
                Message obtain = Message.obtain();
                obtain.arg1 = percent;
                weakHandler.sendMessage(obtain);
            }

            @Override
            public void onError(long id, Exception e) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuperFileView.onStopDisplay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorBlue);


    }

    /**
     * 显示文件
     * @param mSuperFileView2 控件
     */
    private void getFilePathAndShowFile(SuperFileView2 mSuperFileView2) {
        if (getFilePath().contains("http")&&!new File(DOWN_DIR, fileName).exists()) {//网络地址要先下载
            downLoadFile(getFilePath(),fileName,mSuperFileView2);
        } else {
            try {
                mSuperFileView2.displayFile(new File(DOWN_DIR, fileName));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }




    public void setFilePath(String fileUrl) {
        this.filePath = fileUrl;
    }

    private String getFilePath() {
        return filePath;
    }


    /**
     * 回退上一级菜单
     * @param view 控件
     */
    public void onClick(View view) {
        finish();
    }


    private void downLoadFile(final String url, final String fileName,final SuperFileView2 mSuperFileView2) {
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
                     is = responseBody.byteStream();
                    final File file = new File(DOWN_DIR, fileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    mSuperFileView2.displayFile(file);
                    Toast.makeText(FileDisplayActivity.this, "下载成功,保存在Download文件夹下", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    commonProgressDialog.dismiss();
                    Toast.makeText(FileDisplayActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(FileDisplayActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
                commonProgressDialog.dismiss();
            }
        });

      }
}
