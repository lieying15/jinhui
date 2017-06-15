package com.thlh.baselib.function.fileload;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.thlh.baselib.R;
import com.thlh.baselib.utils.BaseLog;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by zs on 2016/7/8.
 */
public class DownLoadService extends Service {
    private static final String TAG = "DownLoadService";
    /**
     * 目标文件存储的文件夹路径
     */
    private String  destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DEFAULT_DIR";
    /**
     * 目标文件存储的文件名
     */
    private String destFileName = "";

    private Context mContext;
    private int preProgress = 0;
    private int NOTIFY_ID = 1000;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    private Retrofit retrofit;
    private String  baseurl;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mContext = this;

        String tempurl = intent.getStringExtra("url");
//        baseurl = intent.getStringExtra("url");
        String[]  strs =  tempurl.split("/");
        destFileName = strs[strs.length-1];
        baseurl =  tempurl.replace(destFileName,"");
        BaseLog.e(TAG + "  update  " +  " baseurl " + baseurl);
        BaseLog.e(TAG + "  update DownLoadService  destFileName " + destFileName + " tempurl " + tempurl  + " baseurl " + baseurl);
        loadFile();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 下载文件
     */
    private void loadFile() {
        initNotification();
//        initDialog();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(initOkHttpClient())
                .build();
        retrofit.create(IFileLoad.class)
                .loadFile(destFileName)
                .enqueue(new FileCallback(destFileDir, destFileName) {
                    @Override
                    public void onSuccess(File file) {
                        BaseLog.e("版本更新 请求成功 ");
                        // 安装软件
                        cancelNotification();
                        installApk(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                        BaseLog.e("版本更新  " +progress + "----" + total);
                        updateNotification(progress * 100 / total);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        BaseLog.e("版本更新 请求失败 ");
                        cancelNotification();
                    }
                });
    }

    public interface IFileLoad {
//        @GET("mall_v20160801.apk")
//        Call<ResponseBody> loadFile();
        @GET
        Call<ResponseBody> loadFile(@Url String url);
    }

    /**
     * 安装软件
     *
     * @param file
     */
    private void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        // 执行意图进行安装
        mContext.startActivity(install);
    }

    /**
     * 初始化OkHttpClient
     *
     * @return
     */
    private OkHttpClient initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(100000, TimeUnit.SECONDS);
        builder.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse
                        .newBuilder()
                        .body(new FileResponseBody(originalResponse))
                        .build();
            }
        });
        return builder.build();
    }

    /**
     * 初始化Notification通知
     */
    public void initNotification() {
        builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("0%")
                .setContentTitle("每家美物更新")
                .setProgress(100, 0, false);
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    /**
     * 初始
     */
    public void initDialog() {
//        BaseLog.e("测试 更新窗口  initDialog");
//        AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
//        builder.setTitle("Title");
//        builder.setMessage("This is message");
//        builder.setNegativeButton("OK", null);
//        Dialog dialog=builder.create();
//        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        dialog.show();
//        BaseLog.e("测试 更新窗口 222 initDialog");
//        View view = View.inflate(DownLoadService.this, R.layout.view_dowload, null);
//        AlertDialog.Builder b = new AlertDialog.Builder(DownLoadService.this);
//        b.setView(view);
//        final AlertDialog d = b.create();
//        d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG); //系统中关机对话框就是这个属性
        //d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);  //窗口可以获得焦点，响应操作
        //d.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  //窗口不可以获得焦点，点击时响应窗口后面的界面点击事件
//        d.show();
    }

    /**
     * 更新通知
     */
    public void updateNotification(long progress) {
        int currProgress = (int) progress;
        if (preProgress < currProgress) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, (int) progress, false);
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
        preProgress = (int) progress;
    }

    /**
     * 取消通知
     */
    public void cancelNotification() {
        notificationManager.cancel(NOTIFY_ID);
    }
}
