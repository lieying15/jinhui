package com.thlh.jhmjmw.other;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.AliPayResult;
import com.thlh.baselib.utils.Base64;
import com.thlh.baselib.utils.BaseLog;

import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;


/**
 * 支付宝订单支付帮助类，支付时传入构造方法的参数，再调用pay即可完成支付，回调方法
 */
public class AliPay {
    private  final String SIGN_ALGORITHM = "RSA";
    private  final String SIGN_ALGORITHMS = "SHA1WithRSA";
    private  final String SIGN_CHARSET = "UTF-8";

    private OnPayResultListener payResultListener;
    private Activity activity;
    private String subject ;//商品名称
    private String body ;//商品详情
    private String price;
    private String paytype;  //1订单支付 2钱包充值 3美家币充值
    private String out_trade_no;//订单号


    public AliPay(Activity activity, String subject, String body, String price, String out_trade_no,String paytype,OnPayResultListener listener) {
        this.activity = activity;
        this.subject = subject;
        this.body = body;
        this.price = price;
        this.out_trade_no = out_trade_no;

        this.payResultListener = listener;
    }

    public void pay() {
        if (activity == null || payResultListener == null || out_trade_no == null) {
            BaseLog.e( "AliPay  params error!");
            return;
        }
        String tempPayInfo = getPayInfo(subject,body,price,out_trade_no,paytype);
//        String tempPayInfo = getPayInfotest();
        String sign = getSign(tempPayInfo);
        BaseLog.e( "AliPay  params sign:" + sign);
        try {
            sign = URLEncoder.encode(sign, "UTF-8");
            BaseLog.e( "AliPay  params sign2:" + sign);
            final String payInfo = tempPayInfo + "&sign=\"" + sign + "\"&sign_type=\"RSA\"";
            new Thread() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(activity);
                    // 调用支付接口
                    String result = alipay.pay(payInfo,true);
                    final AliPayResult payResult = new AliPayResult(result);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                String resultStatus = payResult.resultStatus;
                            if(TextUtils.equals(resultStatus,"9000"))
                                payResultListener.onPaySucceed(payResult);
                            else{
                                payResultListener.onPayFailed(new Exception("pay failed!"));
                            }
                        }
                    });
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
            payResultListener.onPayFailed(e);
        }

    }


    /**
     * 创建支付信息
     */
    private String getPayInfo(String subject, String body, String price, String out_trade_no,String paytype) {

        // 参数编码， 固定值
        String payInfo = "_input_charset=\"utf-8\"";
        // 自定义参数 1订单支付 2钱包充值 3美家币充值
        payInfo += "&attach=" + "\"" +  paytype + "\"";
        // 商品详情
        payInfo += "&body=" + "\"" + body + "\"";
        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        payInfo += "&it_b_pay=\""+ Constants.ALI_CLOSETIME +"\"";
        // 服务器异步
        payInfo += "&notify_url=\"" +Deployment.BASE_URL + Constants.ALI_NOTIFY_URL + "\"";
        // 商户网站订单号
        payInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";
        // 合作者身份ID
        payInfo += "&partner=" + "\"" + Constants.ALI_PARTNER + "\"";
        // 支付类型， 固定值
        payInfo += "&payment_type=\"1\"";
        // 卖家支付宝账号
        payInfo += "&seller_id=" + "\"" + Constants.ALI_SELLER + "\"";
        // 接口名称， 固定值
        payInfo += "&service=\"mobile.securitypay.pay\"";
        // 商品名称
        payInfo += "&subject=" + "\"" + subject + "\"";
        // 商品金额
        payInfo += "&total_fee=" + "\"" + price + "\"";
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        // payInfo += "&return_url=\""+ Constants.ALI_NOTIFY_URL +"\"";
        // 调用银行卡支付，需配置此参数，参与签名， 固定值
        // orderInfo += "&paymethod=\"expressGateway\"";
        BaseLog.i("AliPayInfo ",payInfo);
        return payInfo;
    }


    /**
     *  对支付信息进行签名
     */
    private String getSign(String content) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(Constants.ALI_RSA_PRIVATE));
            KeyFactory keyf = KeyFactory.getInstance(SIGN_ALGORITHM, "BC");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes(SIGN_CHARSET));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public interface OnPayResultListener {
        public void onPaySucceed(AliPayResult payResult);
        public void onPayFailed(Exception e);
    }

    //测试签名
    private String getPayInfotest() {
        // 商品详情
        String payInfo = "body=每家美物商品";
        payInfo += "&buyer_email=55g14hq@sina.com";
        payInfo += "&buyer_id=2088002440355892";
        payInfo += "&discount=0.00";
        payInfo += "&gmt_create=2016-07-13 14:31:34";
        payInfo += "&gmt_payment=2016-07-13 14:31:34";
        payInfo += "&notify_id=37b1f3abc502b3804b182d547d3312bmva";
        payInfo += "&notify_time=2016-07-13 14:31:35";
        payInfo += "&notify_type=trade_status_sync";
        payInfo += "&out_trade_no=48";
        payInfo += "&payment_type=1";
        payInfo += "&price=0.01";
        payInfo += "&quantity=1";

        BaseLog.i("AliPayInfo ",payInfo);
        return payInfo;
    }
}
