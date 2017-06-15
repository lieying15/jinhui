package com.thlh.jhmjmw.business.order.orderdetail;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.model.Order;
import com.thlh.baselib.utils.GoodsChangeUtils;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.baselib.utils.TimeUtils;
import com.thlh.baselib.view.DialogFrgCancelOrder;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.shopcart.ShopCartActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.goods.suit.GoodsSuitDetailActivity;
import com.thlh.jhmjmw.business.order.comment.OrderCommentActivity;
import com.thlh.jhmjmw.business.order.trace.OrderTraceActivity;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.business.pay.PayOrderActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.other.OrderUtils;
import com.thlh.jhmjmw.view.DialogPhone;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.sweetdialog.SweetAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *  订单详情页
 */
public class OrderDetailActivity extends BaseViewActivity {
    private final String TAG = "OrderDetailActivity";
    private final int ACTIVITY_CODE_PAY = 1;
    @BindView(R.id.order_detail_header)
    HeaderNormal orderDetailHeader;


    @BindView(R.id.order_detail_paytype_ll)
    LinearLayout paytypeLl;
    @BindView(R.id.order_detail_paytype_tv)
    TextView paytypeTv;
    @BindView(R.id.order_detail_paytype_iv)
    ImageView paytypeIv;
    /*
    *
    * 订单状态*/
    @BindView(R.id.order_detail_status_tv)
    TextView statusTv;

    @BindView(R.id.order_address_name_tv)
    TextView orderDetailNameTv;
    @BindView(R.id.order_address_addr_tv)
    TextView orderDetailAddrTv;
    @BindView(R.id.order_address_phone_tv)
    TextView orderDetailPhoneTv;
    @BindView(R.id.order_address_arrow_iv)
    ImageView orderDetailAddressArrowIv;
    @BindView(R.id.order_detail_number_tv)
    TextView orderDetailNumberTv;
    @BindView(R.id.order_detail_date_tv)
    TextView orderDetailDateTv;
    @BindView(R.id.order_detail_rv)
    EasyRecyclerView orderDetailRv;
    @BindView(R.id.order_detail_freight_tv)
    TextView orderDetailFreightTv;
    @BindView(R.id.order_detail_price_tv)
    TextView orderDetailPriceTv;
    @BindView(R.id.order_confirm_total_price_title)
    TextView totalPriceTitle;
    @BindView(R.id.order_confirm_total_price_tv)
    TextView totalPriceTv;
    @BindView(R.id.order_detail_mjz_tv)
    TextView mjzTv;


    @BindView(R.id.order_detail_cancel_tv)
    TextView cancelTv;
    @BindView(R.id.order_detail_gotopay_tv)
    TextView gotoPayTv;
    @BindView(R.id.order_detail_paytime_tv)
    TextView paytimeTv;

    @BindView(R.id.order_detail_scroll)
    ScrollView scrollView;
    @BindView(R.id.order_detail_trace_ll)
    LinearLayout traceLl;
    @BindView(R.id.order_detail_bottom_ll)
    LinearLayout bottomLl;

    /*
   * 备注
   * */
    @BindView(R.id.order_offer_beizhu_xq)
    TextView orderOfferRemarks;


    private BaseObserver<BaseResponse> confrimOrderObserver,cancelOrderObserver;

    private OrderDetailAdapter orderListAdapter;
    private Order order;
    private boolean showReturn = false;
    private DialogPhone.Builder phoneDialog ;
    private SweetAlertDialog sweetDialog;


    public static void activityStart(Activity context, Order order) {
        Intent intent = new Intent();
        intent.putExtra("order", order);
        intent.setClass(context, OrderDetailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initVariables() {
        order = getIntent().getParcelableExtra("order");
        L.e("hqt !!! id:"+ order.getOrder_id()+ " getPay_by_mjb:" + order.getPay_by_mjb());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        phoneDialog = new DialogPhone.Builder(this);
        sweetDialog = new SweetAlertDialog(OrderDetailActivity.this, SweetAlertDialog.SUCCESS_TYPE);
        //Tofix 订单地址信息，暂时用默认地址代替
        orderDetailNameTv.setText(getResources().getString(R.string.name)+ order.getName() );
        orderDetailAddrTv.setText(getResources().getString(R.string.address)+order.getAddress());
        orderDetailPhoneTv.setText(getResources().getString(R.string.phone)+ order.getTelephone());
        orderDetailNumberTv.setText(getResources().getString(R.string.order_num) + order.getOrder_no());
        orderDetailAddressArrowIv.setVisibility(View.GONE);
        orderDetailDateTv.setText(getResources().getString(R.string.order_time) + TimeUtils.stringToDateString(order.getOrder_time()));
        orderDetailPriceTv.setText(getResources().getString(R.string.money) + order.getGoods_amount());
        if(order.getSupplier_id().equals("1")){
            orderDetailFreightTv.setText(getResources().getString(R.string.goods_receive));
        }else {
            orderDetailFreightTv.setText(getResources().getString(R.string.money) + order.getExpress_fee());
        }

        double spendmjz = OrderUtils.getDetailSpendMjz(order);
        if(spendmjz == 0 ){
            mjzTv.setText(TextUtils.showPrice(spendmjz));
        }else {
            mjzTv.setText(" - "+TextUtils.showPrice(spendmjz));
        }

        totalPriceTv.setText(OrderUtils.getDetailTotalPrice(order,this,spendmjz));


        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                this,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );
        orderListAdapter = new OrderDetailAdapter(this,showReturn);

        orderListAdapter.setList(order.getOrder_items());
        orderListAdapter.setOnClickEvent(new OrderDetailItemAdapter.OnClickEvent() {
            @Override
            public void onReturn(int position) {
                String suppler_tel = order.getOrder_items().get(0).getItem().get(0).getSupplier_tel();
                String suppler_name = order.getOrder_items().get(0).getItem().get(0).getSupplier_name();
                showPhoneDialog(suppler_name,suppler_tel);
            }
        });
        orderDetailRv.setAdapter(orderListAdapter);
        orderDetailRv.setLayoutManager(new LinearLayoutManager(this));
        orderDetailRv.addItemDecoration(dataDecoration);
       /***/
       int status = OrderUtils.getOrderStatus(order);
        String orderStatusStr = OrderUtils.getOrderStatusStr(status);
        /**
        * 双数不乱码
        * 单数最后一个字乱码
        * questions*/
        try{
            orderStatusStr = new String(orderStatusStr.getBytes("GB2312"));
        }catch(Exception e){
        }
        /*
        * 订单状态
        * */
        statusTv.setText(orderStatusStr);
        try {
            String tempPaytype = getPaytype(order);
           //支付方式
            paytypeTv.setText(tempPaytype);

            if(tempPaytype.equals("美家钻支付")){
                paytypeTv.setTextColor(getResources().getColor(R.color.green));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        CountDownTimer timer = new CountDownTimer(getRemainTime(order.getOrder_time()), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                paytimeTv.setText( getResources().getString(R.string.sy_pay_time) + changeTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                paytimeTv.setText( getResources().getString(R.string.order_have_cannal));
            }
        };

        /*
        * 备注内容
        * */
        orderOfferRemarks.setText(order.getNote());

        switch (status) {
            case Constants.ORDER_TYPE_WAIT_PAY:
                cancelTv.setVisibility(View.VISIBLE);
                gotoPayTv.setVisibility(View.VISIBLE);
                paytimeTv.setVisibility(View.VISIBLE);
                totalPriceTitle.setText(getResources().getString(R.string.need_pay));
                gotoPayTv.setText(getResources().getString(R.string.shopcart_clearing));
                gotoPayTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PayOrderActivity.activityStart(OrderDetailActivity.this,ACTIVITY_CODE_PAY,order);
                    }
                });
                traceLl.setVisibility(View.GONE);
                timer.start();
                break;

            case Constants.ORDER_TYPE_WAIT_SENDOUT:
                bottomLl.setVisibility(View.GONE);
                cancelTv.setVisibility(View.GONE);
                gotoPayTv.setVisibility(View.GONE);
                paytimeTv.setVisibility(View.GONE);
                traceLl.setVisibility(View.VISIBLE);
                if(order.getSupplier_id().equals("1")){
                    orderListAdapter.setShowReturn(false);
                }else {
                    orderListAdapter.setShowReturn(true);
                }
                break;

            case Constants.ORDER_TYPE_WAIT_GAIN:
                gotoPayTv.setVisibility(View.VISIBLE);
                gotoPayTv.setText(getResources().getString(R.string.true_goods));
                gotoPayTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showConfrimGainDialog(order.getOrder_id());
                    }
                });
                traceLl.setVisibility(View.VISIBLE);
                break;

            case Constants.ORDER_TYPE_WAIT_COMMENT:
                cancelTv.setVisibility(View.VISIBLE);
                cancelTv.setText(getResources().getString(R.string.shop_evaluation));
                cancelTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OrderCommentActivity.activityStart(OrderDetailActivity.this);
                    }
                });
                gotoPayTv.setVisibility(View.GONE);
                traceLl.setVisibility(View.VISIBLE);
                break;

            case Constants.ORDER_TYPE_HAS_COMMENT:
                gotoPayTv.setText(getResources().getString(R.string.again_buy));
                gotoPayTv.setVisibility(View.VISIBLE);
                gotoPayTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addShopcart(order);
                        ShopCartActivity.activityStart(OrderDetailActivity.this);
                    }
                });
                break;

            case Constants.ORDER_TYPE_COMPLETE:
                if(order.getIs_comment().equals("0")){
                    cancelTv.setVisibility(View.VISIBLE);
                    cancelTv.setText(getResources().getString(R.string.shop_evaluation));
                    cancelTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            OrderCommentActivity.activityStart(OrderDetailActivity.this);
                        }
                    });
                }else {
                    cancelTv.setVisibility(View.GONE);
                }
                gotoPayTv.setText(getResources().getString(R.string.again_buy));
                gotoPayTv.setVisibility(View.VISIBLE);
                gotoPayTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addShopcart(order);
                        ShopCartActivity.activityStart(OrderDetailActivity.this);
//                        sweetDialog.setTitleText("已加入购物车").show();

                    }
                });
                traceLl.setVisibility(View.VISIBLE);
                break;

            case Constants.ORDER_TYPE_CANCEL:
                totalPriceTitle.setText(getResources().getString(R.string.no_pay_need_pay));
                gotoPayTv.setVisibility(View.VISIBLE);
                gotoPayTv.setText(getResources().getString(R.string.again_buy));
                gotoPayTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        addShopcart(order);
                        ShopCartActivity.activityStart(OrderDetailActivity.this);
//                        sweetDialog.setTitleText("已加入购物车").show();
                    }
                });
                traceLl.setVisibility(View.GONE);
                break;
        }

        confrimOrderObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse orderResponse) {
            }

            @Override
            public void onNextResponse(BaseResponse orderResponse) {
                SPUtils.put("order_need_update","2");//更改订单状态：0无变化，1付款，2确认收货，3取消订单,4评价
                OrderCommentActivity.activityStart(OrderDetailActivity.this);
                finish();
            }
        };

        cancelOrderObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse orderResponse) {
            }

            @Override
            public void onNextResponse(BaseResponse orderResponse) {
                SPUtils.put("order_need_update","3");//更改订单状态：0无变化，1付款，2确认收货，3取消订单,4评价
                finish();
            }
        };
        scrollView.fullScroll(ScrollView.FOCUS_UP);
    }


    @Override
    protected void loadData() {
    }

    private long  getRemainTime(String ordertime){
        return  Long.parseLong(ordertime)*1000 + 86400000 - System.currentTimeMillis();
    }

    private String  changeTime(long millisUntilFinished){
//        String timeStr;
        StringBuilder stringBuilder = new StringBuilder();
        long sec = millisUntilFinished/1000;
        int hour = 0;
        int minute = 0;
        long seocond =0;

        if (sec <= 0)
            return "00:00:00";
        else {
            minute = (int) (sec / 60);
            if (minute < 60) {
                stringBuilder.append("00:");

            } else {
                hour = minute / 60;
                stringBuilder.append(hour +":");
            }
            minute = minute % 60;
            stringBuilder.append(minute+":");
            seocond = sec % 60;
            stringBuilder.append(seocond);
        }
        return  stringBuilder.toString();
    }

    @OnClick({R.id.order_detail_trace_ll, R.id.order_detail_gotopay_tv,R.id.order_detail_cancel_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_detail_trace_ll:
                /**订单跟踪*/
                OrderTraceActivity.activityStart(this,order);
                break;
            case R.id.order_detail_gotopay_tv:

                break;
            case R.id.order_detail_cancel_tv:
                showCancerOrderDialog(order.getOrder_id());
                break;
        }
    }

    private void addShopcart(Order order){

        List<GoodsOrder> goodsorder = order.getOrder_items().get(0).getItem();
        for(GoodsOrder goods : goodsorder){
            try {
                Goods tempgoods = GoodsChangeUtils.changeGoods(goods);
                int size = Integer.parseInt(goods.getItem_num());
                if(tempgoods.getIs_part().equals("1")){
                    if(goods.getPart_is_bundling().equals("1")){
                        GoodsSuitDetailActivity.activityStart(OrderDetailActivity.this,goods.getPart_of_id(),0);
                    }else {
                        GoodsDetailV3Activity.activityStart(OrderDetailActivity.this,goods.getPart_of_id());
                    }
                    return;
                }
                for (int i = 0; i <size ; i++) {
                    if(tempgoods.getIs_pack().equals("1")){
                        int packNum  = tempgoods.getPack_num()==null?0:Integer.parseInt(tempgoods.getPack_num());
                        for (int n = 0; n < packNum; n++) {
                            DbManager.getInstance().insertCart(tempgoods);
                        }
                    }else {
                        DbManager.getInstance().insertCart(tempgoods);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void postConfrimOrder(String orderid) {
        NetworkManager.getOrderApi()
                .confrimOrder(SPUtils.getToken(), orderid)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(confrimOrderObserver);
    }

    public void showPhoneDialog(String suppler_name,final String suppler_tel){
        phoneDialog.setTitle(suppler_name + getResources().getString(R.string.Customer_service)).setContent(suppler_tel).setButtonListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Intent phoneIntent = new Intent();
                    phoneIntent.setAction(Intent.ACTION_DIAL);
                    phoneIntent.setData(Uri.parse("tel:" + suppler_tel));
                    startActivity(phoneIntent);
                } catch (Exception e) {
                    L.e("call phone error", e.getLocalizedMessage());
                }
            }
        }).create().show();
    }

    private String getPaytype(Order order){

        if(order.getSupplier_id().equals("1")){
            /**"冰箱兑换券"
             * getResources().getString(R.string.ice_exchage)
             * */
            return getResources().getString(R.string.ice_exchage);
        }
        StringBuilder paytype = new StringBuilder();
        for (int i = 0; i < order.getPay().size(); i++) {
            if(order.getPay().get(i).getIs_ok().equals("0"))continue;
            String methodid = order.getPay().get(i).getPayment_method_id();
            switch (methodid){
                /**"美家钻支付"
                 * getResources().getString(R.string.mjz_pay)
                 * */
                case "1": paytype.append(getResources().getString(R.string.mjz_pay));
                    paytypeIv.setBackgroundResource(R.drawable.img_pay_mjb);
//                    paytypeIv.setVisibility(View.VISIBLE);
                    break;
                /**"余额支付"
                 * getResources().getString(R.string.order_confirm_paytype_yue)
                 * */
                case "2": paytype.append(getResources().getString(R.string.order_confirm_paytype_yue));
                    paytypeIv.setBackgroundResource(R.drawable.img_pay_ali);
//                    paytypeIv.setVisibility(View.VISIBLE);
                    break;
                /**"微信支付"
                 * getResources().getString(R.string.order_confirm_paytype_weixin)
                 * */
                case "3": paytype.append(getResources().getString(R.string.order_confirm_paytype_weixin));
                    paytypeIv.setBackgroundResource(R.drawable.img_pay_wechat);
//                    paytypeIv.setVisibility(View.VISIBLE);
                    break;
                /**"支付宝支付"
                 * getResources().getString(R.string.zfb_pay)
                 * */
                case "4": paytype.append(getResources().getString(R.string.zfb_pay));
                    break;
                default :return "";
            }
            if(i< order.getPay().size() -1){
                paytype.append(",");
            }
        }

        return  paytype.toString();
    }

    private String getAddress(){
        StringBuilder tempaddress =  new StringBuilder();
        String province = (String) SPUtils.get("user_address_province", "").toString();
        String city = (String) SPUtils.get("user_address_city", "").toString();
        tempaddress.append( province);
        if(! province.equals(city)){
            tempaddress.append( city);
        }
        tempaddress.append(  (String) SPUtils.get("user_address_district", "").toString() );
        return  tempaddress.toString();
    }

    public void showConfrimGainDialog(final  String orderid){
        final NormalDialogFragment confirmDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        confirmDialog.setTitleIvRes(R.drawable.icon_dialog_warning);
        confirmDialog.setContentStr(getResources().getString(R.string.yes_no_goods));
        confirmDialog.setMiddleBtnVisible(View.VISIBLE);
        confirmDialog.setMiddleBtnStr(getResources().getString(R.string.shopcart_total_cannal));
        confirmDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
            }
        });
        confirmDialog.setFinalBtnStr(getResources().getString(R.string.shopcart_total_confirm));
        confirmDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postConfrimOrder(orderid);
                confirmDialog.dismiss();
            }
        });
        confirmDialog.show(ft,"confirmDialog");
    }


    public void showCancerOrderDialog(final String orderid){
        final DialogFrgCancelOrder cancelDialog = new DialogFrgCancelOrder();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        cancelDialog.setTitleIvRes(com.thlh.baselib.R.drawable.img_dialog_trash);
        cancelDialog.setMiddleBtnVisible(View.VISIBLE);
        cancelDialog.setMiddleBtnStr(getResources().getString(R.string.think_about));
        cancelDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelDialog.dismiss();
            }
        });
        cancelDialog.setFinalBtnStr(getResources().getString(R.string.order_cannal));
        cancelDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCancelOrder(orderid,cancelDialog.getRadioButtonStr());
                cancelDialog.dismiss();
            }
        });
        cancelDialog.show(ft,"cancelDialog");
    }

    public void postCancelOrder(String orderid,String reason) {
        NetworkManager.getOrderApi()
                .cancelOrder(SPUtils.getToken(), orderid,reason)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(cancelOrderObserver);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_PAY:

                    break;
                default:
                    break;
            }
        }
    }
}
