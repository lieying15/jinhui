package com.thlh.jhmjmw.business.buy.buyconfirm.selectmjz;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.GoodsOrder;
import com.thlh.baselib.utils.GoodsChangeUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogNormal;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 支付-选择美家币界面
 */

public class SelectPayMjbActivity extends BaseViewActivity {
    private final String TAG = "SelectPayMjbActivity";

    @BindView(R.id.paymjb_header)
    HeaderNormal paymjbHeader;
    @BindView(R.id.paymjb_usemjb_tv)
    TextView paymjbUsemjbTv;
    @BindView(R.id.paymjb_canpay_rv)
    EasyRecyclerView paymjbCanpayRv;
    @BindView(R.id.paymjb_uncanpay_ll)
    LinearLayout paymjbUncanpayLl;
    @BindView(R.id.paymjb_uncanpay_rv)
    EasyRecyclerView paymjbUncanpayRv;

    private PayMjbAdapter payMjbAdapter,notPayMjbAdapter;
    private EasyDividerItemDecoration dataDecoration;

    private List<Cartgoods> canPayList = new ArrayList<Cartgoods>();
    private List<Cartgoods> notCanPayList = new ArrayList<Cartgoods>();
    private List<Cartgoods> orderCartList = new ArrayList<Cartgoods>();
    private ArrayList<GoodsOrder> goodsOrderList;

    private double useMjb =0;
    private ArrayList<String > useMjbItemId = new ArrayList<>(); //需要每家钻支付的商品id数组

    private int selectMjbType = Constants.MJBSELECT_TYPE_ORDER_CONFIRM;
    private boolean isBuyImmediately;


    public static void activityStart(Activity context,boolean isBuyImmediately) {
        Intent intent = new Intent();
        intent.setClass(context, SelectPayMjbActivity.class);
        intent.putExtra("isBuyImmediately",isBuyImmediately);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    public static void activityStart(Activity context, int type , ArrayList<GoodsOrder> orderList,ArrayList<String > useMjbItemId) {
        Intent intent = new Intent();
        intent.setClass(context, SelectPayMjbActivity.class);
        intent.putExtra("selectMjbType", type);
        intent.putParcelableArrayListExtra("orderList",orderList);
        intent.putStringArrayListExtra("useMjbItemId",useMjbItemId);



        context.startActivityForResult(intent,type);
    }

    @Override
    protected void initVariables() {
        selectMjbType = getIntent().getIntExtra("selectMjbType",Constants.MJBSELECT_TYPE_ORDER_CONFIRM);
        if(selectMjbType == Constants.MJBSELECT_TYPE_ORDER_PAY){
            goodsOrderList = getIntent().getParcelableArrayListExtra("orderList");
            useMjbItemId = getIntent().getStringArrayListExtra("useMjbItemId");
            if(goodsOrderList == null ){
                showErrorDialog();
            }else {
                orderCartList = getCartGoodsData(goodsOrderList);
                getMjbCartList(orderCartList);
            }
        }else {
            isBuyImmediately = getIntent().getBooleanExtra("isBuyImmediately",false);
            canPayList = DbManager.getInstance().getAllPayMjbCartGoods(true,isBuyImmediately);
            notCanPayList = DbManager.getInstance().getAllPayMjbCartGoods(false,isBuyImmediately);
        }
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_paymjb);
        ButterKnife.bind(this);
        if(selectMjbType == Constants.MJBSELECT_TYPE_ORDER_PAY){
            paymjbHeader.setLeftListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prpareBack();
                    finish();
                }
            });
        }

        paymjbUsemjbTv.setText(getResources().getString(R.string.ch_mjz_sy)+ SPUtils.get("user_mjb","")+")");
        dataDecoration = new EasyDividerItemDecoration(
                this,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mianback
        );

        payMjbAdapter = new PayMjbAdapter(this);
        payMjbAdapter.setOnClickEvent(new PayMjbAdapter.OnClickListener() {
            @Override
            public void changeBotton(boolean on, int position) {
               long dbGoodsId =  ((Cartgoods)payMjbAdapter.getItem(position)).getDb_goods_id();
                L.e(" changeBotton :" +on );
                if(selectMjbType == Constants.MJBSELECT_TYPE_ORDER_PAY){
                    for (int i = 0; i < canPayList.size(); i++) {
                        if(canPayList.get(i).getDb_goods_id() ==dbGoodsId ){
                            canPayList.get(i).setIsPayMjb(on);
                            break;
                        }
                    }
                }else {
                    DbManager.getInstance().setCartGoodsPayMjb(dbGoodsId,on);
                }
            }
        });
        paymjbCanpayRv.setLayoutManager(new LinearLayoutManager(this));
        paymjbCanpayRv.setAdapter(payMjbAdapter);
        payMjbAdapter.setList(canPayList);
        paymjbCanpayRv.addItemDecoration(dataDecoration);
        if(notCanPayList == null || notCanPayList.size()==0){
            paymjbUncanpayLl.setVisibility(View.GONE);
            paymjbUncanpayRv.setVisibility(View.GONE);
        }else {
            paymjbUncanpayLl.setVisibility(View.VISIBLE);
            paymjbUncanpayRv.setVisibility(View.VISIBLE);
            notPayMjbAdapter = new PayMjbAdapter(this);
            notPayMjbAdapter.setList(notCanPayList);
            paymjbUncanpayRv.setLayoutManager(new LinearLayoutManager(this));
            paymjbUncanpayRv.setAdapter(notPayMjbAdapter);
            paymjbUncanpayRv.addItemDecoration(dataDecoration);
        }

        if( canPayList == null || canPayList.size() == 0 ){
            paymjbCanpayRv.setVisibility(View.GONE);
        }else {
            paymjbCanpayRv.setVisibility(View.VISIBLE);
        }

    }

    private List<Cartgoods> getCartGoodsData(ArrayList<GoodsOrder> goodsOrderList) {
        List<Cartgoods> tempCartList = new ArrayList<Cartgoods>();
        for(GoodsOrder goodsOrder : goodsOrderList){

            GoodsDb goodsDb = GoodsChangeUtils.changeGoodsDb(goodsOrder);
            String itemid = goodsDb.getItem_id();
            long goodsDbid = DbManager.getInstance().getGoodsDBid(itemid);
            if(goodsDbid == -1){
                goodsDbid = DbManager.getInstance().getGoodsSize() + 1;
            }
            goodsDb.setDbid(goodsDbid);
            DbManager.getInstance().insertGoods(goodsDb);


            Cartgoods cartgoods = new Cartgoods();
            cartgoods.setGoodsdb(goodsDb);
            cartgoods.setIsPayMjb(true);
            cartgoods.setDb_goods_id(goodsDbid);
            cartgoods.setGoods_num(Integer.parseInt(goodsOrder.getItem_num()));
            if(useMjbItemId.contains(itemid)){
                cartgoods.setIsPayMjb(true);
            }else {
                cartgoods.setIsPayMjb(false);
            }
            boolean hasGoods = false;
            for (int i = 0; i < tempCartList.size(); i++) {
                if( tempCartList.get(i).getGoodsdb().getItem_id().equals(itemid)){
                    tempCartList.get(i).setGoods_num(tempCartList.get(i).getGoods_num() +1);
                    hasGoods =true;
                    break;
                }
            }
            if(!hasGoods){
                tempCartList.add(cartgoods);
            }
        }
        return tempCartList;
    }

    private void  getMjbCartList(List<Cartgoods> carts){
        for(Cartgoods cartgoods : carts){
            if(cartgoods.getGoodsdb().getIs_mjb().equals("0")){
                notCanPayList.add(cartgoods);
            }else {
                canPayList.add(cartgoods);
            }
        }
        L.e( "notCanPayList " +notCanPayList.size() + " canPayList "+canPayList.size());
    }

    private double getUseMjb(){
        double tempMjb = 0;
        useMjbItemId.clear();
        for(Cartgoods cartgoods:canPayList){
            if(cartgoods.getIsPayMjb()){
                if(cartgoods.getGoodsdb().getIs_mjb().equals("1")){
                    tempMjb += Double.parseDouble(cartgoods.getGoodsdb().getItem_price()) * cartgoods.getGoods_num();
                    useMjbItemId.add(cartgoods.getGoodsdb().getItem_id());
                }
                if(cartgoods.getGoodsdb().getIs_mjb().equals("2")){
                    tempMjb += Double.parseDouble(cartgoods.getGoodsdb().getMjb_value()) * cartgoods.getGoods_num();
                    useMjbItemId.add(cartgoods.getGoodsdb().getItem_id());
                }
            }
        }
        return   tempMjb ;
    }

    private void showErrorDialog() {
        DialogNormal.Builder dialogError = new DialogNormal.Builder(this);
        dialogError.setTitle("")
                .setCancelOutside(false)
                .setSubTitle(getResources().getString(R.string.shop_information_wrong))
                .setRightBtnStr(getResources().getString(R.string.back))
                .setRightClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void loadData() {}


    // 回退键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            if(selectMjbType == Constants.MJBSELECT_TYPE_ORDER_PAY){
                prpareBack();
            }
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }


    private void prpareBack(){
        Intent intent = new Intent();
        double tempUseMjb = getUseMjb();
        intent.putExtra("useMjb",tempUseMjb);
        intent.putStringArrayListExtra("useMjbItemId",useMjbItemId);
        setResult(Activity.RESULT_OK,intent );
        L.e(" prpareBack useMjbItemId.size: " +useMjbItemId.size() + " useMjb:"+ tempUseMjb);
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }


}
