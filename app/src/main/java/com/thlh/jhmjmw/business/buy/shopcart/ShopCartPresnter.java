package com.thlh.jhmjmw.business.buy.shopcart;

import android.content.Context;

import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.CartSupplier;
import com.thlh.baselib.model.CartSupplierCheck;
import com.thlh.baselib.model.Cartgoods;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.model.GoodsDb;
import com.thlh.baselib.model.OnShelves;
import com.thlh.baselib.model.Storage;
import com.thlh.baselib.model.response.GoodsRecommendResponse;
import com.thlh.baselib.model.response.OnShelvesResponse;
import com.thlh.baselib.model.response.StorageResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by huqiang on 2017/2/6.
 */

public class ShopCartPresnter implements ShopCartContract.Presenter {
    private final ShopCartContract.View mMineView;
    private String TAG = "ShopCart ";
    private BaseObserver<OnShelvesResponse> getOnSelvesObserver;
    private BaseObserver<StorageResponse> storageObserver;
    private BaseObserver<GoodsRecommendResponse> recommendObserver;
    private List<CartSupplier> cartSupplierList = new ArrayList<CartSupplier>();
    private List<CartSupplierCheck> cartSupplierCheckList = new ArrayList<>();
    private Map<String,String> itemStatusMap = new HashMap<>();

    private Context context;
    public ShopCartPresnter(Context context,ShopCartContract.View mMineView) {
        this.context=context;
        this.mMineView = mMineView;
    }

    @Override
    public void loadRecommand() {
        recommendObserver  = new BaseObserver<GoodsRecommendResponse>() {
            @Override
            public void onErrorResponse(GoodsRecommendResponse recommendResponse) {
            }

            @Override
            public void onNextResponse(GoodsRecommendResponse recommendResponse) {
                List<Goods> goodsList = recommendResponse.getData().getItem();
                L.e(TAG + "查询推荐商品 goodsList.size "+ goodsList.size());

                mMineView.updateRecommandGoods(goodsList);
            }
        };
        L.e(TAG + " 查询推荐商品");
        NetworkManager.getGoodsDataApi()
                .getRecommendGoods()
                .compose(RxUtils.androidSchedulers(mMineView,false))
                .subscribe(recommendObserver);
    }

    @Override
    public void loadStorage(String itemid) {
        storageObserver  = new BaseObserver<StorageResponse>() {
            @Override
            public void onErrorResponse(StorageResponse storageResponse) {
                mMineView.hideLoadindBar();
            }

            @Override
            public void onNextResponse(StorageResponse storageResponse) {
                //报错支付订单号
                List<Storage> tempStorageList = storageResponse.getData().getStorage();
                for (Storage storage : tempStorageList) {
                    String goodid = storage.getItem_id();
                    int storageNum = storage.getStorage();
                    L.e(TAG + "商品库存 goodid:" + goodid + " storageNum:"+storageNum);
                    if (storage.getStorage() == 0) {
                        itemStatusMap.put(goodid,"storage");
                    }
                }
                mMineView.updateCartAdapter();
                loadOnShelves(itemid);
            }
        };
        L.e(TAG + "查询商品库存 itemid：" + itemid);
        NetworkManager.getItemApi()
                .getStorageInfo(SPUtils.getToken(),itemid)
                .compose(RxUtils.androidSchedulers(mMineView,false))
                .subscribe(storageObserver);
    }

    @Override
    public void loadOnShelves(String itemid) {
        getOnSelvesObserver = new BaseObserver<OnShelvesResponse>() {
            @Override
            public void onErrorResponse(OnShelvesResponse onShelvesResponse) {
                mMineView.hideLoadindBar();
            }

            @Override
            public void onNextResponse(OnShelvesResponse onShelvesResponse) {
                mMineView.hideLoadindBar();
                //报错支付订单号
                List<OnShelves> tempOnShelvesList = onShelvesResponse.getData().getOn_shelves();
                for (OnShelves onShelves : tempOnShelvesList) {
                    if (onShelves.getIs_shelves().equals("0")) { //下架
                        String goodid = onShelves.getItem_id();
                        L.e(TAG + "下架的商品号：" + goodid);
                        itemStatusMap.put(goodid,"onShelves");
                    }
                }
                mMineView.updateCartStatus(itemStatusMap);
                mMineView.updateCartAdapter();
            }
        };
        L.e(TAG + "查询商品下架 itemid：" + itemid);
        NetworkManager.getGoodsDataApi()
                .getOnShelvesInfo(SPUtils.getToken(),itemid)
                .compose(RxUtils.androidSchedulers(mMineView,false))
                .subscribe(getOnSelvesObserver);
    }


    @Override
    public void cartSelectSupplier(int position) {
        boolean select = cartSupplierCheckList.get(position).getSelect();
        cartSupplierCheckList.get(position).setSelect(!select);
        for (int i = 0; i < cartSupplierCheckList.get(position).getGoodsCheckStates().size(); i++) {
            cartSupplierCheckList.get(position).getGoodsCheckStates().set(i,!select);
        }
        DbManager.getInstance().setSupplierGoodsSelect(cartSupplierList.get(position).getSupplier_id(),!select);
    }

    @Override
    public void cartSelectGoods(int position, int itemposition) {
        List<Boolean> goodsCheckStates = cartSupplierCheckList.get(position).getGoodsCheckStates();
        boolean select = goodsCheckStates.get(itemposition);
        goodsCheckStates.set(itemposition, !select);

        Cartgoods cartgoods = cartSupplierList.get(position).getCartgoods().get(itemposition);
        DbManager.getInstance().setGoodsSelect(cartgoods.getGoodsdb(),!select);

        //判断供应商
        boolean isSelectSupplierOne = false;
        for (int n = 0; n < cartSupplierCheckList.get(position).getGoodsCheckStates().size(); n++) {
            if( cartSupplierCheckList.get(position).getGoodsCheckStates().get(n)){
                isSelectSupplierOne = true;
                break;
            }
        }

        if (isSelectSupplierOne) {
            cartSupplierCheckList.get(position).setSelect(true);
        } else {
            cartSupplierCheckList.get(position).setSelect(false);
        }
    }

    @Override
    public void cartAdd(int position, int itemposition) {
        Cartgoods tempcartgoods =  cartSupplierList.get(position).getCartgoods().get(itemposition);
        GoodsDb goods = tempcartgoods.getGoodsdb();
        tempcartgoods.setGoods_num(tempcartgoods.getGoods_num());
        if (goods.getIs_pack().equals("1")) {
            int packNum = goods.getPack_num() == null ? 0 : Integer.parseInt(goods.getPack_num());
            for (int i = 0; i < packNum; i++) {
                DbManager.getInstance().insertCart(goods);
            }
        } else {
            DbManager.getInstance().insertCart(goods);
        }
    }

    @Override
    public void cartSub(int position, int itemposition) {
        Cartgoods tempcartgoods =  cartSupplierList.get(position).getCartgoods().get(itemposition);
        GoodsDb goods = tempcartgoods.getGoodsdb();
        if (tempcartgoods.getGoods_num() == 1) {
            mMineView.showDeleteDialog(position, itemposition,false);
        } else {
            tempcartgoods.setGoods_num(tempcartgoods.getGoods_num());
            if (goods.getIs_pack().equals("1")) {
                int packNum = goods.getPack_num() == null ? 0 : Integer.parseInt(goods.getPack_num());
                if (tempcartgoods.getGoods_num() == packNum) {
                    mMineView.showDeleteDialog(position, itemposition,false);
                } else {
                    for (int i = 0; i < packNum; i++) {
                        DbManager.getInstance().deleteCartGoods(goods);
                    }
                }
            } else {
                DbManager.getInstance().deleteCartGoods(goods);
            }
            mMineView.updatePriceText();
            mMineView.updateNoInfoView(cartSupplierList.size());
        }
    }

    @Override
    public void cartDelete(int position, int itemposition) {
        Cartgoods tempcartgoods =  cartSupplierList.get(position).getCartgoods().get(itemposition);
        GoodsDb goods = tempcartgoods.getGoodsdb();
        if(itemStatusMap!=null)
            itemStatusMap.remove(goods.getItem_id());
        int num = tempcartgoods.getGoods_num();
        for (int n = 0; n < num; n++) {
            DbManager.getInstance().deleteCartGoods(goods);
        }
        cartSupplierCheckList.get(position).getGoodsCheckStates().remove(itemposition);
        cartSupplierList.get(position).getCartgoods().remove(itemposition);
        if (cartSupplierCheckList.get(position).getGoodsCheckStates().size() == 0) {
            cartSupplierCheckList.remove(position);
            cartSupplierList.remove(position);
        }
    }

    @Override
    public List<CartSupplier> initCartData() {
        cartSupplierList.clear();
        cartSupplierList = DbManager.getInstance().getCartSupplierList(false,false);
        return cartSupplierList;
    }


    @Override
    public List<CartSupplierCheck> initCartCheckStates() {
        cartSupplierCheckList.clear();
        for (int i = 0; i < cartSupplierList.size(); i++) {
            CartSupplierCheck supplierCheck = new CartSupplierCheck();
            int num = cartSupplierList.get(i).getCartgoods().size();
            List<Boolean> checkStates = new ArrayList<>();
            for (int n = 0; n < num; n++) {
                if( cartSupplierList.get(i).getCartgoods().get(n).getIsSelect()){
                    checkStates.add(n,true);
                }else {
                    checkStates.add(n,false);
                }
            }
            supplierCheck.setGoodsCheckStates(checkStates);
            cartSupplierCheckList.add(supplierCheck);
        }
//        DbManager.getInstance().setAllGoodsSelect(true);

        //判断供应商

        for (int i = 0; i < cartSupplierCheckList.size(); i ++) {
            boolean isSelectSupplierOne = false;

            for (int n = 0; n < cartSupplierCheckList.get(i).getGoodsCheckStates().size(); n++) {
                if( cartSupplierCheckList.get(i).getGoodsCheckStates().get(n)){
                    isSelectSupplierOne = true;
                    break;
                }
            }
            if (isSelectSupplierOne) {
                cartSupplierCheckList.get(i).setSelect(true);
            } else {
                cartSupplierCheckList.get(i).setSelect(false);
            }
        }
        return cartSupplierCheckList;
    }

    @Override
    public String getItemidStr() {
        StringBuilder shopCartInfo = new StringBuilder();
        List<String>  itemidList = new ArrayList<>();
        List<Cartgoods> cartgoods = DbManager.getInstance().getAllCartGoods();
        for (int i = 0; i < cartgoods.size(); i++) {
            String tempid = cartgoods.get(i).getGoodsdb().getItem_id();
            if (!itemidList.contains(tempid)) {
                itemidList.add(tempid);
            }
        }
        for (int i = 0; i < itemidList.size(); i++) {
            shopCartInfo.append(itemidList.get(i));
            if(i< itemidList.size() -1){
                shopCartInfo.append("|");
            }
        }
        return shopCartInfo.toString().trim();
    }

    @Override
    public boolean judgeCartCondition() {
        if(!SPUtils.getIsLogin()){
            mMineView.startLoginActiivty();
            return false;
        }
        //判断选择是否有选择商品
        boolean selectedgoods = false;
        for (int i = 0; i < cartSupplierCheckList.size(); i++) {
            for (int n = 0; n < cartSupplierCheckList.get(i).getGoodsCheckStates().size(); n++) {
                if( cartSupplierCheckList.get(i).getGoodsCheckStates().get(n)){
                    selectedgoods = true;
                    break;
                }
            }
        }
        if (! selectedgoods) {
            /*
            * String.valueOf(R.string.choose_pay_goods)
            * 购物车列表不选择
            * 请选中要结算的商品
            * questions
            * */
            mMineView.showHintDialog(context.getResources().getString(R.string.choose_pay_goods));
            return false;
        }
        //判断是否有无库存、下架商品
        if(itemStatusMap !=null){
            for(Map.Entry<String,String> entry : itemStatusMap.entrySet()){
                if(entry.getValue().equals("onShelves")){
                   /*
                   *
                   *
                   * */
                    mMineView.showHintDialog(context.getResources().getString(R.string.hava_shelves_shop));
                    return false;
                }
                if(entry.getValue().equals("storage")){
                    /*
                    *
                    *
                    * */
                    mMineView.showHintDialog(context.getResources().getString(R.string.shop_no_cun));
                    return false;
                }
            }
        }

        return  true;
    }


    @Override
    public boolean isSelectAll() {
        boolean isSelectall = true;
        for (int i = 0; i < cartSupplierCheckList.size(); i++) {
            for (int n = 0; n < cartSupplierCheckList.get(i).getGoodsCheckStates().size(); n++) {
                if( !cartSupplierCheckList.get(i).getGoodsCheckStates().get(n)){
                    isSelectall = false;
                    break;
                }
            }
        }
        return isSelectall;
    }


    @Override
    public void selectAll() {
        // 先判断是否已经全选
        boolean isSelectAll = true;
        for (int i = 0; i < cartSupplierCheckList.size(); i++) {
            for (int n = 0; n < cartSupplierCheckList.get(i).getGoodsCheckStates().size(); n++) {
                if( !cartSupplierCheckList.get(i).getGoodsCheckStates().get(n)){
                    isSelectAll = false;
                    break;
                }
            }
        }
        if(isSelectAll){
            for (int i = 0; i < cartSupplierCheckList.size(); i++) {
                cartSupplierCheckList.get(i).setSelect(false);
                for (int n = 0; n < cartSupplierCheckList.get(i).getGoodsCheckStates().size(); n++) {
                    cartSupplierCheckList.get(i).getGoodsCheckStates().set(n,false);
                }
            }
            DbManager.getInstance().setAllGoodsSelect(false);
        }else {
            for (int i = 0; i < cartSupplierCheckList.size(); i++) {
                cartSupplierCheckList.get(i).setSelect(true);
                for (int n = 0; n < cartSupplierCheckList.get(i).getGoodsCheckStates().size(); n++) {
                    cartSupplierCheckList.get(i).getGoodsCheckStates().set(n,true);
                }
            }
            DbManager.getInstance().setAllGoodsSelect(true);
        }
        mMineView.updateCartAdapter();
    }


    @Override
    public void cartDeleteAllSelect() {
        for (int i = cartSupplierCheckList.size() -1 ; i >=0; i--) {
            for (int n = cartSupplierCheckList.get(i).getGoodsCheckStates().size()-1; n >=0 ; n--) {
                if(cartSupplierCheckList.get(i).getGoodsCheckStates().get(n)){
                    cartDelete(i,n);
                }
            }
        }
    }

    @Override
    public boolean isSelectOne() {
        // 先判断是否已经全选
        boolean isSelectOne = false;
        for (int i = 0; i < cartSupplierCheckList.size(); i++) {
            for (int n = 0; n < cartSupplierCheckList.get(i).getGoodsCheckStates().size(); n++) {
                if( cartSupplierCheckList.get(i).getGoodsCheckStates().get(n)){
                    isSelectOne = true;
                    break;
                }
            }
        }
        return isSelectOne;
    }




}
