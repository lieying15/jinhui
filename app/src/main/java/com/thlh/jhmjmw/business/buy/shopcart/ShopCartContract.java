package com.thlh.jhmjmw.business.buy.shopcart;

import com.thlh.baselib.base.BaseView;
import com.thlh.baselib.model.CartSupplier;
import com.thlh.baselib.model.CartSupplierCheck;
import com.thlh.baselib.model.Goods;

import java.util.List;
import java.util.Map;

/**
 * Created by huqiang on 2017/2/6.
 */

public interface ShopCartContract {


//    interface View extends BaseView<ShopCartContract.Presenter> {
    interface View extends BaseView{

        void updateRecommandGoods(List<Goods> goodsList);

        void updatePriceText();

        void updateCartAdapter();

        void showDeleteDialog(final int position,final int itemposition,boolean isdeletall);

        void showHintDialog(String msg);

        void updateSelectIcon();

        void updateCartStatus(Map<String, String> itemStatusMap);

        void updateNoInfoView(int dataCount);

        void startLoginActiivty();
    }


    interface Presenter {

        void loadRecommand(); //查询推荐商品

        void loadStorage(String itemid);

        void loadOnShelves(String itemid);

        void cartSelectSupplier(int position);//判断供应商选择

        void cartSelectGoods(int position, int itemposition);

        void cartAdd(int position, int itemposition);

        void cartSub(int position, int itemposition);

        void cartDelete(int position, int itemposition);

        void cartDeleteAllSelect();


        List<CartSupplier> initCartData();//获取购物车数据

        List<CartSupplierCheck> initCartCheckStates();

        String getItemidStr();

        boolean judgeCartCondition();

        boolean isSelectAll();

        boolean isSelectOne();

        void selectAll();
    }

}
