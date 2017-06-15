package com.thlh.jhmjmw.business.recharge;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.AgencyResponse;
import com.thlh.baselib.model.response.ScanResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.moudle.zxing.decoding.RGBLuminanceSource;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogNormal;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 充值界面 无未知来源时
 */
public class RechargeQRActivity extends BaseActivity {
    private final String TAG = "RechargeQRActivity";

    @BindView(R.id.rechargeqr_location_iv)
    ImageView rechargeqrLocationIv;
    @BindView(R.id.rechargeqr_location_tv)
    TextView rechargeqrLocationTv;
    @BindView(R.id.rechargeqr_qr_iv)
    ImageView rechargeqrQrIv;

    private BaseObserver<ScanResponse> bandstoreobserver;
    private BaseObserver<AgencyResponse> agencyobserver;
    private DialogNormal.Builder dialogHint;

    //定位
    private LocationClient mLocClient;
    private MyLocationListenner myListener = new MyLocationListenner();
    //
    private String qrPath;

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, RechargeQRActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initVariables() {
        qrPath = (String) SPUtils.get("qr_code","").toString();
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_recharge_qr);
        ButterKnife.bind(this);
        dialogHint = new DialogNormal.Builder(this);
        initLocation();

//        String inputStr = "1234567"; 生成二维码
//        Bitmap qrCode = EncodingUtils.createQRCode(inputStr, 500, 500, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        Glide.with(getApplication().getBaseContext())
                .load(Deployment.IMAGE_PATH +qrPath)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        rechargeqrQrIv.setImageBitmap(resource);
                    }
                });
        //二维码绑定充值
        /*
        * 长按绑定
        * */
        rechargeqrQrIv.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
              String result = readImage(rechargeqrQrIv);
                if(result.contains("http://v2.mjmw365.com/ch/?ch=")){
                    String ch = result.replace("http://v2.mjmw365.com/ch/?ch=","").trim();
                    SPUtils.put("user_ch",ch);
                    bindAgency(ch);
                }
                return false;
            }
        });
    }



    @Override
    protected void loadData() {
    }


    private void showBindStoreSuccessDialog(String msg){
        SPUtils.put("user_isch",1);
        RechargeActivity.activityStart(RechargeQRActivity.this, Constants.PAY_PURPOSE_MJB);
    }

    private void showBindFaildDialog(String result){
        dialogHint.setTitle(result)
                .setSubTitle("")
                .setRightBtnStr(getResources().getString(R.string.back))
                .setRightClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create().show();
    }


    protected void bindAgency(String ch) {
        bandstoreobserver = new BaseObserver<ScanResponse>() {
            @Override
            public void onErrorResponse(ScanResponse baseResponse) {
                L.e(TAG + " 问题：" + baseResponse.getErr_code() + ": "+baseResponse.getErr_msg());
                showBindFaildDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(ScanResponse scanResponse) {
                showBindStoreSuccessDialog(scanResponse.getData().getMsg());
            }
        };

        NetworkManager.getGoodsDataApi()
                .scanQR(SPUtils.getToken(),ch)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(bandstoreobserver);
    }

    protected void loadAgency(double lat,double lng) {
        agencyobserver = new BaseObserver<AgencyResponse>() {
            @Override
            public void onErrorResponse(AgencyResponse baseResponse) {
                L.e(TAG + " 问题：" + baseResponse.getErr_code() + ":   "+baseResponse.getErr_msg());
                showBindFaildDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(AgencyResponse scanResponse) {
               SPUtils.put("qrPath",scanResponse.getData().getQr_code());
                SPUtils.put("user_agency_id",scanResponse.getData().getAgency_id());
                Glide.with(getApplication().getBaseContext())
                        .load(Deployment.IMAGE_PATH +scanResponse.getData().getQr_code())
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                rechargeqrQrIv.setImageBitmap(resource);
                            }
                        });

            }
        };

        NetworkManager.getUserDataApi()
                .getAgency(SPUtils.getToken(),lat,lng)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(agencyobserver);
    }


    private void initLocation(){
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        option.setAddrType("all");
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null ) {
                return;
            }
            L.i(TAG + " 定位  " + location.getAddrStr()+ "  etLatitude "  +location.getLatitude() +"  getLongitude "  +location.getLongitude() +"  getProvince "  + location.getProvince() +"  getCity " + location.getCity()+"  getDistrict "  +  location.getDistrict() );

            rechargeqrLocationTv.setText(location.getAddress().address);
            loadAgency(location.getLatitude(),location.getLongitude());
            mLocClient.stop();
        }
    }


    /**
     * 解析QR图内容
     *
     * @param imageView
     * @return
     */
    private String readImage(ImageView imageView) {
        String content = null;
        Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        // 获得待解析的图片
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        RGBLuminanceSource source = new RGBLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result result = reader.decode(bitmap1, hints);
            // 得到解析后的文字
            content = result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    protected void onStop() {
        mLocClient.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLocClient.stop();
        super.onDestroy();
    }
}
