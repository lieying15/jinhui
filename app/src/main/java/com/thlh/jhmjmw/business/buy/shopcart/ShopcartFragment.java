package com.thlh.jhmjmw.business.buy.shopcart;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.db.DbManager;
import com.thlh.baselib.model.CartSupplier;
import com.thlh.baselib.model.CartSupplierCheck;
import com.thlh.baselib.model.Goods;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.buy.buyconfirm.BuyConfirmActivity;
import com.thlh.jhmjmw.business.buy.shopcart.adapter.ShopcartAdapter;
import com.thlh.jhmjmw.business.entrance.login.LoginActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.VerticalltemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huqiang on 2016/12/30.
 */

public class ShopcartFragment extends BaseFragment implements View.OnClickListener, ShopCartContract.View {
    private final String TAG = "ShopCartActivity";

    @BindView(R.id.shopcart_bottom_clearing_ll)
    LinearLayout shopcartBottomClearingLl;
    @BindView(R.id.shopcart_bottom_selectall_iv)
    ImageView shopcartBottomSelectallIv;
    @BindView(R.id.shopcart_total_price_tv)
    TextView totalPriceTv;
    //
    @BindView(R.id.shopcart_mjb_price_tv)
    TextView mjblPriceTv;
    @BindView(R.id.shopcart_goods_rv)
    EasyRecyclerView shopcartGoodsRv;
    @BindView(R.id.shopcart_goods_noinfoview)
    NoInfoView shopcartNoInfoView;
    @BindView(R.id.shopcart_header)
    HeaderNormal shopcartHeader;
    @BindView(R.id.shopcart_bottom_select_tv)
    TextView selectTv;
    @BindView(R.id.shopcart_bottom_price_ll)
    LinearLayout bottomPriceLl;
    @BindView(R.id.shopcart_bottom_delete_ll)
    LinearLayout bottomDeleteLl;
    @BindView(R.id.shopcart_bottom_delete_tv)
    TextView bottomDeleteTv;

    private ShopCartContract.Presenter mPresenter;
    private ShopcartAdapter shopcartAdapter;

    private List<CartSupplier> cartSupplierList = new ArrayList<CartSupplier>();
    private List<CartSupplierCheck> cartSupplierCheckList = new ArrayList<>();
    private Map<String, String> itemStatusMap = new HashMap<>();


    private NormalDialogFragment deleteDialog ;
    private UpdateCartInfo updatelistener;



    private String itemid;
    private boolean isEditState = false ;
    private boolean hasInit;

    //静态内部类方法创建单例
    public static class CartFragmentLoader {
        private static final ShopcartFragment instance = new ShopcartFragment();
    }

    public static ShopcartFragment newInstance() {
        return ShopcartFragment.CartFragmentLoader.instance;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_shopcart;
    }

    @Override
    protected void initVariables() {
        mPresenter = new ShopCartPresnter(getActivity(),this);
        itemid = mPresenter.getItemidStr();
        cartSupplierList.clear();
		cartSupplierList = mPresenter.initCartData();
        cartSupplierCheckList = mPresenter.initCartCheckStates();
        L.e(TAG + " itemid :" + itemid);

    }

    @Override
    protected void initView() {
        deleteDialog = new NormalDialogFragment();
        shopcartHeader.setRightText(getResources().getString(R.string.shopcart_total_editor));
        shopcartHeader.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEditState){
                    bottomPriceLl.setVisibility(View.VISIBLE);
                    bottomDeleteLl.setVisibility(View.GONE);
                    isEditState = false;
                    shopcartHeader.setRightText(getResources().getString(R.string.shopcart_total_editor));
                }else {
                    bottomPriceLl.setVisibility(View.GONE);
                    bottomDeleteLl.setVisibility(View.VISIBLE);
                    isEditState = true;
                    shopcartHeader.setRightText(getResources().getString(R.string.shopcart_total_finish));
                }
                updateSelectIcon();
            }
        });

        if(getActivity() instanceof IndexActivity){
            shopcartHeader.setLeftVisible(View.INVISIBLE);
            shopcartHeader.setLeftListener(null);
        }
        shopcartNoInfoView.setTitle(getResources().getString(R.string.shopcart_total_noshop));
        shopcartNoInfoView.setTitleIv(R.drawable.img_dialog_cart);
        shopcartNoInfoView.setNextactionStr(getResources().getString(R.string.shopcart_total_golook));
        shopcartNoInfoView.setNextactionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexActivity.activityStart(getActivity(), IndexActivity.POSITON_HOMEPAGE);

            }
        });

        shopcartAdapter = new ShopcartAdapter(getActivity(), cartSupplierCheckList, itemStatusMap);
        shopcartAdapter.setOnClickEvent(new ShopcartAdapter.OnClickListener() {
            @Override
            public void onClickSelectSupplier(int position) {
                mPresenter.cartSelectSupplier(position);
                updateSelectIcon();
                updatePriceText();
                updateCartData();
            }

            @Override
            public void onClickSelectGoods(int position, int itemposition) {
                mPresenter.cartSelectGoods(position,itemposition);
                updateSelectIcon();
                updatePriceText();
                updateCartData();
            }

            @Override
            public void onClickAdd(View view, int position, int itemposition) {
                mPresenter.cartAdd(position,itemposition);
                updatePriceText();
                updateCartData();
            }

            @Override
            public void onClickSub(View view, int position, int itemposition) {
                mPresenter.cartSub(position,itemposition);
                updateCartData();
            }

            @Override
            public void onClickDelete(View view, int position, int itemposition) {
                showDeleteDialog(position, itemposition,false);
                updateCartData();
            }
        });

        shopcartAdapter.setList(cartSupplierList);
        shopcartGoodsRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        shopcartGoodsRv.setAdapter(shopcartAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.y10);
        shopcartGoodsRv.addItemDecoration(new VerticalltemDecoration(spacingInPixels));
        updateNoInfoView(shopcartAdapter.getItemCount());
        updateSelectIcon();
        updatePriceText();
    }

    @Override
    protected void initData() {


    }

    @Override
    protected void loadData() {
        mPresenter.loadStorage(itemid);
    }

    @OnClick({ R.id.shopcart_bottom_clearing_ll,R.id.shopcart_bottom_selectall_iv,R.id.shopcart_bottom_delete_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shopcart_bottom_clearing_ll:
                if (mPresenter.judgeCartCondition()) {
                    BuyConfirmActivity.activityStart(getActivity(),false);
                }
                break;
            case R.id.shopcart_bottom_selectall_iv:
                mPresenter.selectAll();
                updatePriceText();
                updateSelectIcon();
                break;
            case R.id.shopcart_bottom_delete_tv:
                //删除选中商品
                if (mPresenter.judgeCartConditionDelete()) {
                    showDeleteDialog(0, 0, true);
                }
                break;
        }
    }


    @Override
    public void updateNoInfoView(int dataCount) {
        if (shopcartAdapter.getItemCount() == 0) {
            shopcartNoInfoView.setVisibility(View.VISIBLE);
            shopcartHeader.setRightVisible(View.INVISIBLE);
            mPresenter.loadRecommand();
        } else {
            shopcartNoInfoView.setVisibility(View.GONE);
            shopcartHeader.setRightVisible(View.VISIBLE);
        }
    }

    @Override
    public void updatePriceText() {
        String totalprice = DbManager.getInstance().getSelectGoodsPrice(cartSupplierCheckList);
        mjblPriceTv.setVisibility(View.GONE);
        totalPriceTv.setVisibility(View.VISIBLE);

        totalPriceTv.setText(getResources().getString(R.string.money) + totalprice);
        if (cartSupplierList == null || cartSupplierList.size() == 0) {
            shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_gray);
        }
        selectTv.setText(getResources().getString(R.string.shopcart_total_choosee)+ DbManager.getInstance().getSelectGoodsNum() + ")");
        if(updatelistener!=null){
            updatelistener.update();
        }
    }

    @Override
    public void updateCartAdapter() {
        shopcartAdapter.notifyDataSetChanged();
    }

    @Override
    public void showDeleteDialog(int position, int itemposition,boolean isdeleteall) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        deleteDialog.setContentStr(getResources().getString(R.string.shopcart_total_deleteshop));
        deleteDialog.setTitleIvRes(com.thlh.baselib.R.drawable.img_dialog_trash);
        deleteDialog.setMiddleBtnVisible(View.VISIBLE);
        deleteDialog.setMiddleBtnStr(getResources().getString(R.string.shopcart_total_cannal));
        deleteDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setFinalBtnStr(getResources().getString(R.string.shopcart_total_confirm));
        deleteDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isdeleteall){
                    mPresenter.cartDeleteAllSelect();
                }else {
                    mPresenter.cartDelete(position,itemposition);
                    if (shopcartAdapter.itemAdapter != null) {
                        shopcartAdapter.itemAdapter.closeOpenedSwipeItemLayoutWithAnim();
                    }
                }
                bottomPriceLl.setVisibility(View.VISIBLE);
                bottomDeleteLl.setVisibility(View.GONE);
                isEditState = false;
                shopcartHeader.setRightText(getResources().getString(R.string.shopcart_total_editor));
                updateCartData();
                shopcartAdapter.notifyDataSetChanged();
                updateSelectIcon();
                updatePriceText();
                updateNoInfoView(shopcartAdapter.getItemCount());
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show(ft, "deleteDialog");
    }

    @Override
    public void showHintDialog(String msg) {
        showWaringDialog(msg);
    }

    @Override
    public void updateSelectIcon() {
        L.e(TAG + " updateSel;ectIcon");
        if(isEditState){
            if(mPresenter.isSelectOne()){
                shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_wine_select);
            }else {
                shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_wine);
            }
        }else {
            if (mPresenter.isSelectAll()) {
                shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_wine_select);
            } else {
                shopcartBottomSelectallIv.setImageResource(R.drawable.icon_check_wine);
            }
        }
    }

    @Override
    public void updateCartStatus(Map<String, String> itemStatusMap) {
        for (String key : itemStatusMap.keySet()) {
            this.itemStatusMap.put(key,itemStatusMap.get(key));
        }
    }

    @Override
    public void startLoginActiivty() {
        LoginActivity.activityStart(getActivity());
    }

    @Override
    public void updateRecommandGoods(List<Goods> goodsList) {
        shopcartNoInfoView.setList(goodsList);
    }

    @Override
    public void onStart() {
        super.onStart();
        L.e(TAG + " onStart");
        hasInit = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        L.e(TAG + " onResume");
    }

    public void updateCartData(){
        //更新购物车数据
        L.e(TAG + " updateCartData");
        itemid = mPresenter.getItemidStr();
        cartSupplierList.clear();
        cartSupplierList = mPresenter.initCartData();
        L.e("cartSupplierList=====shop=========" +  cartSupplierList.size());
        cartSupplierCheckList = mPresenter.initCartCheckStates();
        shopcartAdapter.setList(cartSupplierList);
        updateNoInfoView(shopcartAdapter.getItemCount());
        updateSelectIcon();
        updatePriceText();
        mPresenter.loadStorage(itemid);
    }

    public interface UpdateCartInfo {
        void update();
    }

    public void setUpdatelistener(UpdateCartInfo updatelistener) {
        this.updatelistener = updatelistener;
    }

    public boolean isHasInit() {
        return hasInit;
    }


}
