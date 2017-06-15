package com.thlh.jhmjmw.other;

import android.util.Log;
import android.util.Xml;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.MD5;
import com.thlh.baselib.utils.SystemUtils;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2016/5/23.
 */
public class WeChatUtils {
    // appid 微信平台号 mch_id商户号 body商品说明 nonceStr 随机字符串  notify_url通知网址 sign签名 out_trade_no商户订单号  spbill_create_ip 终端IP  total_fee总金额 不能带小数。
//    private final String trade_type = "APP";
    private static int inner_member = 0;
    //  API密钥，在商户平台设置



    public static Map<String, String> setAheadOrderParam(String total_fee , String goodsbody, String orderid, String paytype , int innermember){
        Map<String, String> premapdata = new HashMap<String, String>();
        Log.e("WeChatUtils total_fee", total_fee);
        premapdata.put("appid", Constants.WECHAT_APP_ID);
        premapdata.put("attach",paytype);
        premapdata.put("body", goodsbody);
        premapdata.put("mch_id",Constants.WECHAT_MCH_ID);
        premapdata.put("nonce_str", genNonceStr());
        premapdata.put("notify_url",Deployment.BASE_URL + Constants.WECHAT_NOTIFY_URL);
        premapdata.put("out_trade_no",orderid);
//        premapdata.put("spbill_create_ip", "127.0.0.1");
        premapdata.put("spbill_create_ip", BaseApplication.getInstance().getPhoneIP());
        premapdata.put("total_fee", total_fee);
        premapdata.put("trade_type", "APP");
        String sign ;
        inner_member  = innermember;
        sign = genAheadOrderSign(premapdata);
        premapdata.put("sign", sign);
        return premapdata;
    }

    private static String genAheadOrderSign(Map<String, String> premapdata ) {
        StringBuilder sb = new StringBuilder();
        sb.append("appid="+premapdata.get("appid") +"&");
        sb.append("attach="+premapdata.get("attach") +"&");
        sb.append("body="+premapdata.get("body") +"&");
        sb.append("mch_id="+premapdata.get("mch_id") +"&");
        sb.append("nonce_str="+premapdata.get("nonce_str") +"&");
        sb.append("notify_url="+premapdata.get("notify_url") +"&");
        sb.append("out_trade_no="+premapdata.get("out_trade_no") +"&");
        sb.append("spbill_create_ip=" + premapdata.get("spbill_create_ip") + "&");
        if(inner_member ==1){
            sb.append("total_fee="+"1" +"&");
        }else {
            sb.append("total_fee="+ premapdata.get("total_fee")  +"&");
        }
        sb.append("trade_type="+premapdata.get("trade_type") +"&");
        sb.append("key=");
        sb.append(Constants.WECHAT_API_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("genAheadOrderSign",packageSign);
        return packageSign;
    }


    public static String genAheadOrderXML(Map<String, String> premapdata) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<appid>"+premapdata.get("appid") +"</appid>");
        sb.append("<attach>"+premapdata.get("attach") +"</attach>");
        sb.append("<body>"+premapdata.get("body") +"</body>");
        sb.append("<mch_id>" + premapdata.get("mch_id") + "</mch_id>");
        sb.append("<nonce_str>" + premapdata.get("nonce_str") + "</nonce_str>");
        sb.append("<notify_url>" + premapdata.get("notify_url") + "</notify_url>");
        sb.append("<out_trade_no>" + premapdata.get("out_trade_no") + "</out_trade_no>");
        sb.append("<spbill_create_ip>" + premapdata.get("spbill_create_ip") + "</spbill_create_ip>");
        if(inner_member ==1){
            sb.append("<total_fee>" + "1" + "</total_fee>");
        }else {
            sb.append("<total_fee>" + premapdata.get("total_fee") + "</total_fee>");
        }
        sb.append("<trade_type>" + premapdata.get("trade_type") + "</trade_type>");
        sb.append("<sign>" + premapdata.get("sign") + "</sign>");
        sb.append("</xml>");
        Log.e("genAheadOrderXML ", sb.toString());
        return sb.toString();
    }


    private static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
    

    public static PayReq postPayRequest(PayReq req,String prepay_id ,String nonceStr) {
        req.appId = Constants.WECHAT_APP_ID;
        req.partnerId = Constants.WECHAT_MCH_ID;
        req.prepayId = prepay_id;
        req.packageValue = "Sign=WXPay";
        req.nonceStr =  genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        Map<String, String> reqmapdata = new HashMap<String, String>();
        reqmapdata.put("appid", req.appId);
        reqmapdata.put("nonceStr", req.nonceStr);
        reqmapdata.put("package", req.packageValue);
        reqmapdata.put("partnerId", req.partnerId);
        reqmapdata.put("prepayId", req.prepayId);
        reqmapdata.put("timeStamp", req.timeStamp);
        String tempsign = genPaySign(reqmapdata);
        req.sign = tempsign;
        return  req ;
    }


    private static String genPaySign( Map<String, String> reqmapdata) {
        StringBuilder sb = new StringBuilder();
        sb.append("appid="+reqmapdata.get("appid") +"&");
        sb.append("noncestr="+reqmapdata.get("nonceStr") +"&");
        sb.append("package=" + reqmapdata.get("package") + "&");
        sb.append("partnerid=" + reqmapdata.get("partnerId") + "&");
        sb.append("prepayid=" + reqmapdata.get("prepayId") + "&");
        sb.append("timestamp=" + reqmapdata.get("timeStamp") + "&");
        sb.append("key=");
        sb.append(Constants.WECHAT_API_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("WechatUtils genPaySign",packageSign);
        return packageSign;
    }
    
    //提现
    public static Map<String, String> setCashParam(String total_fee , String openid,String orderid){
        Map<String, String> premapdata = new HashMap<String, String>();
        Log.e("WeChatUtils total_fee", total_fee +" openid"+openid +" orderid " + orderid);
        premapdata.put("amount",total_fee);
        premapdata.put("check_name","NO_CHECK");
        premapdata.put("desc","end");
        premapdata.put("mch_appid","wx4ede366da7ce7765");
        premapdata.put("mchid", "1364377802");
        premapdata.put("nonce_str", genNonceStr());
        premapdata.put("openid",openid);
        premapdata.put("partner_trade_no",orderid);
//        premapdata.put("re_user_name","李岩");
        premapdata.put("spbill_create_ip", BaseApplication.getInstance().getPhoneIP());
        String sign = genCashSign(premapdata);
        premapdata.put("sign", sign);
        return premapdata;
    }

    private static String genCashSign(Map<String, String> reqmapdata) {
        StringBuilder sb = new StringBuilder();
        sb.append("amount="+reqmapdata.get("amount") +"&");
        sb.append("check_name="+reqmapdata.get("check_name") +"&");
        sb.append("desc=" + reqmapdata.get("desc") + "&");
        sb.append("mch_appid=" + reqmapdata.get("mch_appid") + "&");
        sb.append("mchid=" + reqmapdata.get("mchid") + "&");
        sb.append("nonce_str=" + reqmapdata.get("nonce_str") + "&");
        sb.append("openid=" + reqmapdata.get("openid") + "&");
        sb.append("partner_trade_no=" + reqmapdata.get("partner_trade_no") + "&");
//        sb.append("re_user_name=" + reqmapdata.get("re_user_name") + "&");
        sb.append("spbill_create_ip=" + reqmapdata.get("spbill_create_ip") + "&");
        sb.append("key=");
        sb.append("E71D2BC53942E47BBF1CB87AD78ACECB");
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("WechatUtils genCashSign",packageSign);
        return packageSign;
    }

    public static String genCashXML(Map<String, String> premapdata) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        sb.append("<amount>"+premapdata.get("amount") +"</amount>");
        sb.append("<check_name>"+premapdata.get("check_name") +"</check_name>");
        sb.append("<desc>"+premapdata.get("desc") +"</desc>");
        sb.append("<mch_appid>" + premapdata.get("mch_appid") + "</mch_appid>");
        sb.append("<mchid>" + premapdata.get("mchid") + "</mchid>");
        sb.append("<nonce_str>" + premapdata.get("nonce_str") + "</nonce_str>");
        sb.append("<openid>" + premapdata.get("openid") + "</openid>");
        sb.append("<partner_trade_no>" + premapdata.get("partner_trade_no") + "</partner_trade_no>");
//        sb.append("<re_user_name>" + premapdata.get("re_user_name") + "</re_user_name>");
        sb.append("<spbill_create_ip>" + premapdata.get("spbill_create_ip") + "</spbill_create_ip>");
        sb.append("<sign>" + premapdata.get("sign") + "</sign>");
        sb.append("</xml>");
        Log.e("genAheadOrderXML ", sb.toString());
        return sb.toString();
    }

    public static String createCashXML(Map<String, String> premapdata) {
        boolean bFlag = false;
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String strTmpName = sDateFormat.format(new java.util.Date()) + ".xml";
        String path = SystemUtils.getDiskCacheDir() + strTmpName;
        FileOutputStream fileos = null;
        File newXmlFile = new File(path);
        try {
            if (newXmlFile.exists()) {
                bFlag = newXmlFile.delete();
            } else {
                bFlag = true;
            }
            if (bFlag) {
                if (newXmlFile.createNewFile()) {
                    fileos = new FileOutputStream(newXmlFile);
                    XmlSerializer serializer = Xml.newSerializer();
                    serializer.setOutput(fileos, "UTF-8");
                    serializer.startDocument("UTF-8", true);

                    serializer.startTag(null, "xml");

                    serializer.startTag(null, "amount");
                    serializer.text(premapdata.get("amount"));
                    serializer.endTag(null, "amount");

                    serializer.startTag(null, "check_name");
                    serializer.text(premapdata.get("check_name"));
                    serializer.endTag(null, "check_name");

                    serializer.startTag(null, "desc");
                    serializer.text(premapdata.get("desc"));
                    serializer.endTag(null, "desc");

                    serializer.startTag(null, "mch_appid");
                    serializer.text(premapdata.get("mch_appid"));
                    serializer.endTag(null, "mch_appid");

                    serializer.startTag(null, "mchid");
                    serializer.text(premapdata.get("mchid"));
                    serializer.endTag(null, "mchid");

                    serializer.startTag(null, "nonce_str");
                    serializer.text(premapdata.get("nonce_str"));
                    serializer.endTag(null, "nonce_str");

                    serializer.startTag(null, "openid");
                    serializer.text(premapdata.get("openid"));
                    serializer.endTag(null, "openid");

                    serializer.startTag(null, "partner_trade_no");
                    serializer.text(premapdata.get("partner_trade_no"));
                    serializer.endTag(null, "partner_trade_no");

//                    serializer.startTag(null, "re_user_name");
//                    serializer.text(premapdata.get("re_user_name"));
//                    serializer.endTag(null, "re_user_name");

                    serializer.startTag(null, "spbill_create_ip");
                    serializer.text(premapdata.get("spbill_create_ip"));
                    serializer.endTag(null, "spbill_create_ip");

                    serializer.startTag(null, "sign");
                    serializer.text(premapdata.get("sign"));
                    serializer.endTag(null, "sign");

                    serializer.endTag(null, "xml");
                    serializer.endDocument();

                    // write xml data into the FileOutputStream
                    serializer.flush();
                    // finally we close the file stream
                    fileos.close();
                }
            }
        } catch (Exception e) {
        }
        return path;
    }

}
