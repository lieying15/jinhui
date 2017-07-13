package com.thlh.jhmjmw.other;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.thlh.baselib.function.fileload.DownLoadService;
import com.thlh.baselib.function.fileload.FileCallback;
import com.thlh.baselib.function.fileload.FileResponseBody;
import com.thlh.baselib.utils.AppUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.Tos;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.view.DialogDownload;
import com.thlh.jhmjmw.view.DialogNormal;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by zs on 2016/7/7.
 */
public class UpdateManager {

    private String mContent;
    private String mUrl;
    private int mVersionCode;
    private Context mContext;

    private Retrofit retrofit;
    private String destFileName = "";
    private String baseurl = "";
    private DialogDownload.Builder downloadDialog;
    private DialogNormal.Builder dialog;

    /**
     * 目标文件存储的文件夹路径
     */
    private String  destFileDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DEFAULT_DIR";

    public UpdateManager(Context context,String content,String url ,int mVersionCode) {
        this.mContext = context;
        this.mContent = content;
        this.mVersionCode = mVersionCode;
        this.mUrl = url;
        downloadDialog = new DialogDownload.Builder(mContext);
        dialog = new DialogNormal.Builder(mContext);
    }

    public void showDialog() {
        int currentCode = AppUtils.getVersionCode(mContext);
        L.e("UpdateManager  updataCode" +mVersionCode + " currentCode" +currentCode + " mUrl" + mUrl);
        if(mVersionCode > currentCode ){

            dialog.setTitle((mContext.getResources().getString(R.string.updatatitle))).setSubTitle(mContent).setRightBtnStr((mContext.getResources().getString(R.string.updata)))
                    .setRightClickListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateDownload();
                            showDownloadDialog();
//                            Intent intent = new Intent(mContext, DownLoadService.class);
//                            intent.putExtra("url", mUrl);
//                            mContext.startService(intent);
                        }
                    })
            .setCancelable(false)
            .setCancelOutside(false)
            .create().show();
        }

    }

    public void showDownloadDialog() {
        downloadDialog.setTitle((mContext.getResources().getString(R.string.updating))) .create().show();
    }


    public void updateDownload(){
        String[]  strs =  mUrl.split("/");
        destFileName = strs[strs.length-1];
        baseurl =  mUrl.replace(destFileName,"");
        retrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .client(initOkHttpClient())
                .build();

        retrofit.create(DownLoadService.IFileLoad.class)
                .loadFile(destFileName)
                .enqueue(new FileCallback(destFileDir, destFileName) {
                    @Override
                    public void onSuccess(File file) {
                        L.e("版本更新 请求成功 ");
                        downloadDialog.cancle();
                        // 安装软件
    //                        cancelNotification();
                        installApk(file);
                    }

                    @Override
                    public void onLoading(long progress, long total) {
                            L.e("版本更新  " +progress + "----" + total);
    //                        updateNotification(progress * 100 / total);
                        if(downloadDialog!=null){
                            int temprogress = (int) (progress * 100 / total);
                            downloadDialog.setProgress(temprogress);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        L.e("版本更新 请求失败 ");
    //                        cancelNotification();
    //                        if(downloadDialog !=null){
    //                            downloadDialog.
    //                        }
                        downloadDialog.cancle();
                        Tos.show(("下载安装失败，请重新下载！"));
                    }
                });
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
     * 安装软件
     * @param file
     */
    private void installApk(File file) {
        Uri uri = Uri.fromFile(file);
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(install);
        try {
            SPUtils.clearUserInfo();
        }catch (Exception e){

        }
    }


}
