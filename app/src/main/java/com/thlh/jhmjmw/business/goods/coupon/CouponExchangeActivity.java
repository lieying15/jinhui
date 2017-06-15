package com.thlh.jhmjmw.business.goods.coupon;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.ActionResponse;
import com.thlh.baselib.model.Address;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.response.OrderGenerateResponse;
import com.thlh.baselib.model.response.OrderPayResponse;
import com.thlh.baselib.utils.GoodsChangeUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.goods.goodsdetail.GoodsDetailV3Activity;
import com.thlh.jhmjmw.business.other.ResponseActivity;
import com.thlh.jhmjmw.business.user.address.AddrManageActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogFreightInfo;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.progress.ProgressMaterial;
import com.thlh.viewlib.recyclerview.LinearLayoutInScrollManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 兑换冰箱界面
 */
public class CouponExchangeActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "CouponExchangeActivity";
    private final int ACTIVITY_CODE_ADDR = 1;

    @BindView(R.id.order_address_name_tv)
    TextView orderConfirmNameTv;
    @BindView(R.id.order_address_phone_tv)
    TextView orderConfirmPhoneTv;
    @BindView(R.id.order_address_addr_tv)
    TextView orderConfirmAddrTv;
    @BindView(R.id.order_confirm_addr_rl)
    RelativeLayout orderConfirmAddrRl;
    @BindView(R.id.order_confirm_goods_rv)
    EasyRecyclerView orderConfirmGoodsRv;
    @BindView(R.id.order_confirm_goods_num_tv)
    TextView orderConfirmGoodsNumTv;
    @BindView(R.id.order_confirm_goods_total_price_tv)
    TextView orderConfirmGoodsTotalPriceTv;
    @BindView(R.id.order_confirm_goods_ll)
    LinearLayout orderConfirmGoodsLl;
    @BindView(R.id.order_confirm_info_freight_ll)
    LinearLayout orderConfirmInfoFreightLl;
    @BindView(R.id.order_confirm_topll)
    LinearLayout orderConfirmTopLl;
    @BindView(R.id.order_confirm_bottom_gopay_ll)
    LinearLayout orderConfirmBottomGopayLl;
    @BindView(R.id.order_confirm_goodslist_tv)
    TextView goodslistTv;
    @BindView(R.id.order_confirm_goodslist_arrow_iv)
    ImageView goodslistArrowIv;
    @BindView(R.id.order_confirm_info_freight_iv)
    ImageView infoFreightIv;

    private Address selectAddress;
    private List<Cartgoods> cartgoods = new ArrayList<>();
    private List<Goods> goodslist = new ArrayList<>();
    private CouponExchangeImgAdapter orderConfirmImgAdapter;
    private BaseObserver<OrderGenerateResponse> generateOrderObserver;
    private BaseObserver<OrderPayResponse> payOrderObserver;

    private DialogNormal.Builder dialogPay;

    //    微信支付 end
    private String paytype = Constants.PAY_TYPE_BALANCE;

    private int getPacktype = 1;
    private String deliverytime = "任何时间";
    private String itemid;
    private String coupon_id;
    private DialogFreightInfo.Builder builder;


    public static void activityStart(Activity context, int code, String couponid) {
        Intent intent = new Intent();
        intent.putExtra("coupon_id", couponid);
        intent.setClass(context, CouponExchangeActivity.class);
        context.startActivityForResult(intent, code);
    }

    @Override
    protected void initVariables() {
        coupon_id = getIntent().getStringExtra("coupon_id");
        Goods goods = new Goods();

        goods.setItem_id("1");
        goods.setItem_name(getResources().getString(R.string.smart_ice));
        goods.setItem_price("0");
        goodslist.add(goods);

        GoodsDb goodsDb = GoodsChangeUtils.changeGoodsDb(goods);
        long goodsid = (long) (DbManager.getInstance().getGoodsSize() + 1);
        goodsDb.setDbid(goodsid);
        Cartgoods cartgood = new Cartgoods();
        cartgood.setDb_goods_id(goodsid);
        cartgood.setCartgoods_id(1l);
        cartgood.setIsSelect(true);
        cartgood.setGoods_num(1);
        cartgood.setGoodsdb(goodsDb);

        cartgoods.add(cartgood);
        selectAddress = new Address();
        selectAddress.setId((String) SPUtils.get("user_address_id", "").toString());
        selectAddress.setName((String) SPUtils.get("user_address_name", "").toString());
        selectAddress.setPhone((String) SPUtils.get("user_address_phone", "").toString());
        selectAddress.setAddress((String) SPUtils.get("user_address_address", "").toString());
        selectAddress.setIs_on((String) SPUtils.get("user_address_is_on", "0").toString());
        selectAddress.setProvince((String) SPUtils.get("user_address_province", "").toString());
        selectAddress.setCity((String) SPUtils.get("user_address_city", "").toString());
        selectAddress.setDistrict((String) SPUtils.get("user_address_district", "").toString());
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coupon_exchange);
        ButterKnife.bind(this);
        progressMaterial = ProgressMaterial.create(this, false, null);
        dialogPay = new DialogNormal.Builder(this);
        builder = new DialogFreightInfo.Builder(this);
        itemid = changeShopCartInfo();
        goodslistTv.setVisibility(View.INVISIBLE);
        goodslistArrowIv.setVisibility(View.INVISIBLE);
        infoFreightIv.setVisibility(View.INVISIBLE);
        L.e(TAG + " item: " + itemid);
        orderConfirmImgAdapter = new CouponExchangeImgAdapter(this);
        orderConfirmImgAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                GoodsDetailV3Activity.activityStart(CouponExchangeActivity.this, ((Goods) orderConfirmImgAdapter.getItem(position)).getItem_id());
            }
        });
        orderConfirmImgAdapter.setList(goodslist);

        final LinearLayoutInScrollManager starProcuctLM = new LinearLayoutInScrollManager(this, LinearLayoutManager.HORIZONTAL, false);
        orderConfirmGoodsRv.setLayoutManager(starProcuctLM);
        orderConfirmGoodsRv.setNestedScrollingEnabled(false);
        orderConfirmGoodsRv.setAdapter(orderConfirmImgAdapter);

        // 生成订单号的返回信息
        generateOrderObserver = new BaseObserver<OrderGenerateResponse>() {
            @Override
            public void onErrorResponse(OrderGenerateResponse generateResponse) {
                showErrorDialog( generateResponse.getErr_msg());
                progressMaterial.dismiss();
            }

            @Override
            public void onNextResponse(OrderGenerateResponse generateResponse) {
                String orderid = generateResponse.getData().getOrder_id();
                L.e(TAG + "  generateOrderObserver orderid " + orderid);
                postOrderPay(orderid);
            }
        };

        payOrderObserver = new BaseObserver<OrderPayResponse>() {
            @Override
            public void onErrorResponse(OrderPayResponse payResponse) {
                showErrorDialog(payResponse.getErr_msg());
                progressMaterial.dismiss();
            }

            @Override
            public void onNextResponse(OrderPayResponse payResponse) {
                //保存支付订单号
                String pay_no = payResponse.getData().getPay_no();
                SPUtils.put("pay_no", pay_no);
                String tempprice = Double.toString(payResponse.getData().getAmount());
                SPUtils.put("pay_price", tempprice);
                L.i(TAG + " 生成 orderid " + pay_no + "amont " + tempprice);
                //保存支付参数
                SPUtils.setPayParam(tempprice, paytype, Constants.PAY_PURPOSE_ORDER, pay_no);
                showPaySuccessDialog();
            }
        };
        getAddressInfo();
    }


    @OnClick({R.id.order_confirm_goods_ll, R.id.order_confirm_addr_rl, R.id.order_confirm_bottom_gopay_ll,
            R.id.order_confirm_info_freight_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_confirm_goods_ll:
                //物品清单
                break;
            case R.id.order_confirm_addr_rl:
                if (SPUtils.getIsLogin()) {
                    Intent intent = new Intent();
                    intent.putExtra("start_type", AddrManageActivity.START_TYPE_ORDERCONFIRM);
                    intent.setClass(CouponExchangeActivity.this, AddrManageActivity.class);
                    startActivityForResult(intent, ACTIVITY_CODE_ADDR);
                } else {
                    LoginActivity.activityStart(this);
                }
                break;
            case R.id.order_confirm_bottom_gopay_ll:
                if (jugePayCondition()) {
                    //将购物车信息转化成string.
                    paytype = "2";
                    L.e(TAG + " paytype " + paytype);
                    postGenerateOrder();
                }
                break;
            case R.id.order_confirm_info_freight_ll:
                showExpressDialog();
                break;
        }
    }

    private boolean jugePayCondition() {
        if (selectAddress.getId().equals("")) {
            showWaringDialog(getResources().getString(R.string.please_choose_goods_adress));
            return false;
        }

        if (selectAddress.getId().equals("0")) {
            showWaringDialog(getResources().getString(R.string.choose_person_address));
            return false;
        }
        return true;
    }



    @Override
    protected void loadData() {
    }

    private void postGenerateOrder() {
        L.e(TAG + " postGenerateOrder");
        L.e("coupon_id============" + coupon_id  + "itemid====" + itemid +"paytype=======" + paytype);
        L.e("address_id============" + selectAddress.getId()  + "getPacktype====" + getPacktype +"deliverytime=======" + deliverytime);
        Subscription subscription = NetworkManager.getOrderApi()
                .generateOrderV2(SPUtils.getToken(), selectAddress.getId(), itemid, getPacktype + "", paytype, deliverytime, "0", coupon_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generateOrderObserver);
        subscriptionList.add(subscription);
    }

    private void postOrderPay(String orderid) {
        Subscription subscription = NetworkManager.getOrderApi()
                .payOrder(SPUtils.getToken(), orderid, paytype)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(payOrderObserver);
        subscriptionList.add(subscription);
    }

    private String changeShopCartInfo() {
        StringBuilder shopCartInfo = new StringBuilder();
        int selectCount = 0;
        for (int i = 0; i < cartgoods.size(); i++) {
            if (cartgoods.get(i).getIsSelect()) {
                selectCount++;
            }
        }
        int selectCounttemp = 0;
        for (int i = 0; i < cartgoods.size(); i++) {
            if (cartgoods.get(i).getIsSelect()) {
                shopCartInfo.append(cartgoods.get(i).getGoodsdb().getItem_id() + "|" + cartgoods.get(i).getGoods_num() + "|0");
                selectCounttemp++;
                if (selectCounttemp < selectCount) {
                    shopCartInfo.append(",");
                }
            }
        }
        return shopCartInfo.toString().trim();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void showPaySuccessDialog() {
        dialogPay.setTitle(getResources().getString(R.string.dh_success))
                .setCancelOutside(false)
                .setRightBtnStr(getResources().getString(R.string.back))
                .setRightClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(Activity.RESULT_OK);
                        finish();
                    }
                }).create().show();
        ActionResponse response = new ActionResponse();
        response.setHeadertitle(getResources().getString(R.string.coupon_dh));
        response.setTitle(getResources().getString(R.string.congratulation));
        response.setContent(getResources().getString(R.string.dh_success));
        ResponseActivity.activityStart(CouponExchangeActivity.this, response);
        finish();
    }


    private void getAddressInfo() {
        selectAddress.setId((String) SPUtils.get("user_address_id", "").toString());
        selectAddress.setName((String) SPUtils.get("user_address_name", "").toString());
        selectAddress.setPhone((String) SPUtils.get("user_address_phone", "").toString());
        selectAddress.setAddress((String) SPUtils.get("user_address_address", "").toString());
        selectAddress.setIs_on((String) SPUtils.get("user_address_is_on", "0").toString());
        String province = (String) SPUtils.get("user_address_province", "0").toString();
        String city = (String) SPUtils.get("user_address_city", "0").toString();
        String district = (String) SPUtils.get("user_address_district", "0").toString();
        selectAddress.setProvince(province);
        selectAddress.setCity(city);
        selectAddress.setDistrict(district);

        L.e(TAG + " 地址数据 id " + selectAddress.getId() + " Address " + selectAddress.getAddress());
        if (selectAddress.getId().equals("")) {
            orderConfirmNameTv.setText(getResources().getString(R.string.please_choose_goods_adress));
            orderConfirmAddrTv.setText("");
        } else {
            orderConfirmNameTv.setText(getResources().getString(R.string.name)+selectAddress.getName());
            orderConfirmPhoneTv.setText(getResources().getString(R.string.phone)+selectAddress.getPhone());
            if (province.equals(city)) {
                orderConfirmAddrTv.setText(getResources().getString(R.string.address) + province + district + selectAddress.getAddress());
            } else {
                orderConfirmAddrTv.setText(getResources().getString(R.string.address) + province + city + district + selectAddress.getAddress());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_ADDR:
                    getAddressInfo();
                    break;
                default:
                    break;
            }
        }
    }

    public void showExpressDialog() {
        builder.create().show();
//        String tempstr = "运费说明：\n" +
//                "为保障商品安全配送，冰箱到货后，由物流公司向您收取相应运费。根据您填写的配送地址，您的预估运费为￥600，此预估费用仅作参考，实际收费以物流公司货到收取费用为准。\n" +
//                "\n" +
//                "注：\n" +
//                "1、保险费率按货物价值的\n" +
//                "2、以上为运输费用，如需送货单加每台 \t \t      200元送货费（包含卸货费）\n" +
//                "3、如需送货上楼单加上楼费。\n" +
//                "4、如收货方不能及时收货，免费为收货方\t      留仓2天，超过两天托盘收费标准为20\t      元／天／件。";
//        Spannable ariveSpan = new SpannableString(getResources().getString(R.string.yunfei));
//        ariveSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.wine_light)), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ariveSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.wine_light)), 160, ariveSpan.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        DialogUtils.showExprnse(this,ariveSpan);
    }
}
