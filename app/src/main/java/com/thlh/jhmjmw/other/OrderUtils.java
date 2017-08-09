package com.thlh.jhmjmw.other;

import android.content.Context;
import android.text.SpannableString;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.model.OrderItem;
import com.thlh.baselib.model.OrderPay;
import com.thlh.baselib.utils.TextUtils;

import java.util.List;

/**
 * 用于判断订单状态
 */
public class OrderUtils {
    public static final int  ORDER_TYPE_WAITPAY = 0; //等待付款
    public static final int  ORDER_TYPE_COMPLETE = 1; //完成
    public static final int  ORDER_TYPE_WAITSEND = 2;//等待发货
    public static final int  ORDER_TYPE_WAITGET = 3;//等待收货
    public static final int  ORDER_TYPE_CANCEL = 4;//已取消

    public static int getOrderStatus(Order order){
        if(order.getIs_over().equals("1")){
            if(order.getIs_comment().equals("1")){
                return Constants.ORDER_TYPE_HAS_COMMENT;
            }else {
                return Constants.ORDER_TYPE_WAIT_COMMENT;
            }
        }
        if(order.getIs_over().equals("4")){ return Constants.ORDER_TYPE_CANCEL;}
        if(order.getIs_over().equals("0")){ //未完成
            if(order.getIs_delivery().equals("1")){ return Constants.ORDER_TYPE_WAIT_GAIN;}
            if(order.getIs_pay().equals("1")){ return Constants.ORDER_TYPE_WAIT_SENDOUT;}
            return Constants.ORDER_TYPE_WAIT_PAY;
        }
        return   Constants.ORDER_TYPE_WAIT_PAY;
    }

    public static  String  getOrderStatusStr(int states){
        switch (states){
            case Constants.ORDER_TYPE_WAIT_PAY :return Constants.ORDER_TEXT_WAIT_PAY;
            case Constants.ORDER_TYPE_COMPLETE : return  Constants.ORDER_TEXT_COMPLETE;
            case Constants.ORDER_TYPE_WAIT_SENDOUT : return  Constants.ORDER_TEXT_WAIT_SENDOUT;
            case Constants.ORDER_TYPE_WAIT_GAIN : return  Constants.ORDER_TEXT_WAIT_GAIN;
           /**"已取消"
            *  String.valueOf(R.string.cannal)
            * */
            case Constants.ORDER_TYPE_CANCEL : return Constants.ORDER_TEXT_CANCEL;
            case Constants.ORDER_TYPE_WAIT_COMMENT : return  Constants.ORDER_TEXT_WAIT_COMMENT;
            case Constants.ORDER_TYPE_HAS_COMMENT : return  Constants.ORDER_TEXT_HAS_COMMENT;
            default: return  Constants.ORDER_TEXT_WAIT_PAY;
        }
    }

    public static  String  getOrderStatusStr(Order order){
        int status = getOrderStatus(order);
        return getOrderStatusStr(status);
    }

    public static SpannableString getListTotalPrice(Order order, Context context){
        String finalPrice = "0";
        SpannableString spsFinalPrice = new SpannableString("0") ;
        String isPay = order.getIs_pay(); //0 未支付 1已支付 2部分支付
        double payprice = 0 ,paymjz = 0 ;

        if(isPay.equals("0")){ //未付款的
            return new SpannableString(TextUtils.showPrice(order.getShould_pay()) );
        }

        if(isPay.equals("1")){ //已付
            List<OrderPay> orderPayList =  order.getPay();
            if(orderPayList!=null&& orderPayList.size()>0){
                for (int i = 0; i <orderPayList.size() ; i++) {
                    if(orderPayList.get(i).getIs_ok().equals("1")){
                        if(orderPayList.get(i).getPayment_method_id().equals("1") ){
                            paymjz  = Double.parseDouble(orderPayList.get(i).getSum());
                        }else {
                            payprice  = Double.parseDouble(orderPayList.get(i).getSum());
                        }
                    }
                }
            }
            payprice += Double.parseDouble(order.getExpress_fee()) ;
        }

        if(isPay.equals("2")){ //部分付款
            return new SpannableString(TextUtils.showPrice(order.getShould_pay()) );
//            List<OrderPay> orderPayList =  order.getPay();
//            if(orderPayList ==null||orderPayList.size()==0){
//                payprice=0;
//            }else {
//                for (int i = 0; i <orderPayList.size() ; i++) {
//                    if((!orderPayList.get(i).getPayment_method_id().equals("1") )){
//                        payprice  = Double.parseDouble(orderPayList.get(i).getSum());
//                    }
//                }
//            }
        }
        boolean isMjz =false;
        if(payprice >0 &&paymjz>0){
            finalPrice = TextUtils.showPrice(payprice) +TextUtils.showSimpleMjz(context,TextUtils.showPrice(paymjz));
        }else {
            if(payprice>0){
                finalPrice = TextUtils.showPrice(payprice+order.getShould_pay());
            }
            if(paymjz>0){
                isMjz = true;
                spsFinalPrice = TextUtils.showSimpleMjz(context,TextUtils.showPrice(paymjz  ));
            }
        }
        if(!isMjz)
            spsFinalPrice = new SpannableString(finalPrice);
        return  spsFinalPrice;
    }


    public static SpannableString getDetailTotalPrice(Order order, Context context,double spendmjz){
        String finalPrice = "0";
        SpannableString spsFinalPrice = new SpannableString("0") ;
        String isPay = order.getIs_pay(); //0 未支付 1已支付 2部分支付
        double payprice = 0 ,paymjz = 0 ;

        if(isPay.equals("0")){ //未付款的
            return new SpannableString(TextUtils.showPrice(order.getShould_pay()) );
        }

        if(isPay.equals("1")){ //已付款
            List<OrderPay> orderPayList =  order.getPay();
            if(orderPayList!=null&& orderPayList.size()>0){
                for (int i = 0; i <orderPayList.size() ; i++) {
                    if(orderPayList.get(i).getIs_ok().equals("1")){
                        if(orderPayList.get(i).getPayment_method_id().equals("1") ){
                            paymjz  = Double.parseDouble(orderPayList.get(i).getSum());
                        }else {
                            payprice  = Double.parseDouble(orderPayList.get(i).getSum());
                        }
                    }

                }
            }
            payprice += Double.parseDouble(order.getExpress_fee()) ;
        }

        if(isPay.equals("2")){ //部分付款
            return new SpannableString(TextUtils.showPrice(order.getShould_pay()) );
//            List<OrderPay> orderPayList =  order.getPay();
//            if(orderPayList ==null||orderPayList.size()==0){
//                payprice=0;
//            }else {
//                for (int i = 0; i <orderPayList.size() ; i++) {
//                    if((!orderPayList.get(i).getPayment_method_id().equals("1") )){
//                        payprice  = Double.parseDouble(orderPayList.get(i).getSum());
//                    }
//                }
//            }
        }
        boolean isMjz =false;
        if(payprice >0 &&paymjz>0){
            finalPrice =  TextUtils.showPrice(payprice) +TextUtils.showSimpleMjz(context,TextUtils.showPrice(paymjz));
        }else {
            if(payprice>0){
                finalPrice = TextUtils.showPrice(payprice+order.getShould_pay());
            }
            if(paymjz>0){
                isMjz = true;
                spsFinalPrice = TextUtils.showSimpleMjz(context,TextUtils.showPrice(paymjz  ));
            }
        }
        if(!isMjz)
            spsFinalPrice = new SpannableString(finalPrice);
        return  spsFinalPrice;
    }

    public static  double getDetailSpendMjz(Order order){
        String isPay = order.getIs_pay(); //0 未支付 1已支付 2部分支付
        double paymjz = 0 ;
        List<OrderPay> orderPayList =  order.getPay();

        if(isPay.equals("1") ||isPay.equals("2")){ //已付款
            if(orderPayList!=null&& orderPayList.size()>0){
                for (int i = 0; i <orderPayList.size() ; i++) {
                    if(orderPayList.get(i).getIs_ok().equals("1")||orderPayList.get(i).getIs_ok().equals("2")){
                        if(orderPayList.get(i).getPayment_method_id().equals("1") ){
                            paymjz  = Double.parseDouble(orderPayList.get(i).getSum());
                        }
                    }
                }
            }
        }

        if(isPay.equals("0")){
            if(!order.getPay_by_mjb().equals("0.00")){
                for (OrderItem orderItem : order.getOrder_items()){
                    for(GoodsOrder goodsOrder : orderItem.getItem()){
                        if(goodsOrder.getIs_mjb().equals("1"))
                            paymjz += Double.parseDouble(goodsOrder.getItem_price());
                        if(goodsOrder.getIs_mjb().equals("2"))
                            paymjz += Double.parseDouble(goodsOrder.getMjb_value());
                    }
                }
            }
        }
        L.e("hqt bbbbb " + order.getPay_by_mjb());

        L.e("hqt aaaaa " + Double.parseDouble(order.getPay_by_mjb()));
        return Double.parseDouble(order.getPay_by_mjb());
    }

}
