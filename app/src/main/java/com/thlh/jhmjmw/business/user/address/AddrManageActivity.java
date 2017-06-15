package com.thlh.jhmjmw.business.user.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.model.Address;
import com.thlh.baselib.model.response.AddrListResponse;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogSimple;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.NoInfoView;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.ripple.RippleLinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HQ on 2016/4/6. 管理收获地址页
 */
public class AddrManageActivity extends BaseActivity {
    private final String TAG = "AddrManageActivity";
    private final int ACTIVITY_CODE_ADDADDE = 0; // 要打开的Activity
    private final int ACTIVITY_CODE_EDITADDR = 1;
    public static final int START_TYPE_MINE = 0; // 开启前的Activity
    public static final int START_TYPE_ORDERCONFIRM = 1;

    @BindView(R.id.addr_manage_rv)
    EasyRecyclerView addrManageRv;
    @BindView(R.id.addr_manage_addaddr_llrip)
    RippleLinearLayout addrManageAddaddrLlrip;
    @BindView(R.id.addr_manage_header)
    HeaderNormal addrManageHeader;
    @BindView(R.id.addr_manage_add_tv)
    TextView addrManageAddTv;

    @BindView(R.id.addr_manage_noinfoview)
    NoInfoView addrManageNoinfoView;
    
    private BaseObserver<AddrListResponse> addrListObserver;
    private BaseObserver<BaseResponse> deleteObserver;
    private Address selectAddress = new Address();
    
    private AddrManageAdapter addrManageAdapter;
    private List<Address> addressList = new ArrayList<>();
    private DialogSimple.Builder dialog;
    
    private int start_type = ACTIVITY_CODE_ADDADDE;
    private String select_addrid;

    public static void activityStart(Activity context, int start_type) {
        Intent intent = new Intent();
        intent.putExtra("start_type", start_type);
        intent.setClass(context, AddrManageActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {
        start_type = getIntent().getIntExtra("start_type", ACTIVITY_CODE_ADDADDE);
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addr_manage);
        ButterKnife.bind(this);
        initButtonView();
        dialog = new DialogSimple.Builder(this);
        addrManageNoinfoView.setTitle(getResources().getString(R.string.address_no_goods_));
        addrManageNoinfoView.setTitleIv(R.drawable.icon_dialog_error);
        addrManageNoinfoView.setNextactionStr(getResources().getString(R.string.address_add));
        addrManageNoinfoView.setNextactionListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("editType", AddrEditActivity.ADDR_EDITTYPE_ADD);
                intent.setClass(AddrManageActivity.this, AddrEditActivity.class);
                startActivityForResult(intent, ACTIVITY_CODE_ADDADDE);
            }
        });

        addrManageAdapter  = new AddrManageAdapter(this, addressList,start_type);
        addrManageAdapter.setOnClickEvent(new AddrManageAdapter.OnClickEvent() {
            @Override
            public void onDelete(View view, int position) {
                showDeleteDialog(position);
            }

            @Override
            public void onClickSelect(int position) {
                if (addressList == null) return;
                if (addressList.get(position) == null) return;
                if (addressList.get(position).getIs_on().equals("0")) {
                    for (Address address : addressList) {
                        address.setIs_on("0");
                    }
                    addressList.get(position).setIs_on("1");
                    addrManageAdapter.notifyDataSetChanged();
                    selectAddress = addressList.get(position);
                }
            }

            @Override
            public void onClickEdit(View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("editType", AddrEditActivity.ADDR_EDITTYPE_EDIT);
                intent.putExtra("address", (Address) addrManageAdapter.getItem(position));
                intent.setClass(AddrManageActivity.this, AddrEditActivity.class);
                startActivityForResult(intent, ACTIVITY_CODE_EDITADDR);
            }
        });
        

        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                this,
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_mainback
        );
        addrManageAdapter.setList(addressList);
        dataDecoration.bottomDivider = true;
        addrManageRv.addItemDecoration(dataDecoration);
        addrManageRv.setLayoutManager(new LinearLayoutManager(this));
        addrManageRv.setAdapter(addrManageAdapter);

        addrManageAddaddrLlrip.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                Intent intent = new Intent();
                intent.putExtra("editType", AddrEditActivity.ADDR_EDITTYPE_ADD);
                intent.setClass(AddrManageActivity.this, AddrEditActivity.class);
                startActivityForResult(intent, ACTIVITY_CODE_ADDADDE);
            }
        });

        addrListObserver = new BaseObserver<AddrListResponse>() {
            @Override
            public void onErrorResponse(AddrListResponse addrListResponse) {
                showErrorDialog(addrListResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(AddrListResponse addrListResponse) {
                addressList.clear();
                List<Address> templist = addrListResponse.getData().getAddress();
                Collections.sort(templist);
                if(templist == null ||templist.size() ==0){
                    addrManageNoinfoView.setVisibility(View.VISIBLE);
                    addrManageAddaddrLlrip.setVisibility(View.GONE);
                    addrManageHeader.setRightText("");
                    return;
                }else {
                    addrManageNoinfoView.setVisibility(View.GONE);
                    addrManageAddaddrLlrip.setVisibility(View.VISIBLE);
                    addressList.addAll(changeAddressData(templist));
                    addrManageAdapter.setList(addressList);
                    for (Address address : addressList) {
                        if(address.getIs_on().equals("1")){
                            selectAddress = address;
                            break;
                        }
                    }
                }
            }
        };

        deleteObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                showDeleteSuccesDialog();
                if(select_addrid.equals( SPUtils.get("user_address_id",""))){
                    SPUtils.put("user_address_id","");
                    SPUtils.put("user_address_name","");
                    SPUtils.put("user_address_address","");
                    SPUtils.put("user_address_phone","");
                    SPUtils.put("user_address_is_on","");
                }
                loadData();
            }
        };

    }

    @Override
    protected void loadData() {
        L.i(TAG + "loadData 读取地址信息 ");
        NetworkManager.getUserDataApi()
                .getAddrList(SPUtils.getToken())
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(addrListObserver);
    }

    private void postDeletaAddress(int position) {
        L.i(TAG + " postDeletaAddress 删除地址");
        select_addrid = ((Address) addrManageAdapter.getItem(position)).getId();
        NetworkManager.getUserDataApi()
                .deleteAddr(SPUtils.getToken(), select_addrid)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(deleteObserver);
    }

    private List<Address> changeAddressData(List<Address> addrlist){
        List<Address> tempList = new ArrayList<Address>();
        for (Address address : addrlist) {
            if(address.getId().equals("0")){
                tempList.add(address);
                addrlist.remove(address);
                break;
            }
        }

        Collections.sort(addrlist,new Comparator<Address>(){
            public int compare(Address arg0, Address arg1) {
                return arg1.getIs_on().compareTo(arg0.getIs_on());
            }
        });
        tempList.addAll(addrlist);
        return tempList;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_EDITADDR:
                case ACTIVITY_CODE_ADDADDE:
                    loadData();
                    break;
                default:
                    break;
            }
        }
    }


    public void showDeleteDialog(final int position){
        final NormalDialogFragment deleteDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        deleteDialog.setTitleIvRes(R.drawable.img_dialog_trash);
        deleteDialog.setContentStr(getResources().getString(R.string.address_delete));
        deleteDialog.setContentType(DialogUtils.TYPE_NORMAL_WARNING);
        deleteDialog.setMiddleBtnVisible(View.VISIBLE);
        deleteDialog.setMiddleBtnStr(getResources().getString(R.string.trues));
        deleteDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressList.get(position) != null) {
                    postDeletaAddress(position);
                    String locationid = (String) SPUtils.get("user_address_id","");
                    if( addressList.get(position).getId().equals(locationid)){
                        SPUtils.put("user_address_id", "");
                        SPUtils.put("user_address_name", "");
                        SPUtils.put("user_address_address", "");
                        SPUtils.put("user_address_phone", "");
                        SPUtils.put("user_address_is_on","");
                        SPUtils.put("user_address_province","");
                        SPUtils.put("user_address_city", "");
                        SPUtils.put("user_address_district", "");
                    }
                    addressList.remove(position);
                    addrManageAdapter.setList(addressList);
                    addrManageAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
                deleteDialog.dismiss();
            }
        });
        deleteDialog.setFinalBtnStr(getResources().getString(R.string.address_cannal));
        deleteDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        deleteDialog.show(ft, "deleteDialog");
    }

    public void showDeleteSuccesDialog(){
        final NormalDialogFragment deleteSuccessDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        deleteSuccessDialog.setTitleIvRes(R.drawable.img_dialog_trash);
        deleteSuccessDialog.setContentStr(getResources().getString(R.string.address_delete_success));
        deleteSuccessDialog.setMiddleBtnVisible(View.VISIBLE);
        deleteSuccessDialog.setMiddleBtnStr(getResources().getString(R.string.address_add));
        deleteSuccessDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSuccessDialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("editType", AddrEditActivity.ADDR_EDITTYPE_ADD);
                intent.setClass(AddrManageActivity.this, AddrEditActivity.class);
                startActivityForResult(intent, ACTIVITY_CODE_ADDADDE);
            }
        });
        deleteSuccessDialog.setFinalBtnStr(getResources().getString(R.string.back));
        deleteSuccessDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSuccessDialog.dismiss();
            }
        });
        deleteSuccessDialog.show(ft, "deleteSuccessDialog");
    }

    private void initButtonView(){
        switch (start_type){
            case START_TYPE_MINE:
                addrManageAddTv.setText(getResources().getString(R.string.address_new_add));

                break;
            case START_TYPE_ORDERCONFIRM:
                addrManageAddTv.setText(getResources().getString(R.string.address_add));
                addrManageHeader.setRightText(getResources().getString(R.string.trues));
                addrManageHeader.setRightTextColor(getResources().getColor(R.color.wine_light));
                addrManageHeader.setRightListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setResult(Activity.RESULT_OK);
                        if(selectAddress != null){
                            SPUtils.put("user_address_id", selectAddress.getId());
                            SPUtils.put("user_address_name", selectAddress.getName());
                            SPUtils.put("user_address_address", selectAddress.getAddress());
                            SPUtils.put("user_address_phone", selectAddress.getPhone());
                            SPUtils.put("user_address_is_on", selectAddress.getIs_on());
                            SPUtils.put("user_address_province", selectAddress.getProvince());
                            SPUtils.put("user_address_city", selectAddress.getCity());
                            SPUtils.put("user_address_district", selectAddress.getDistrict());
                        }
                        finish();
                    }
                });
                break;
        }
    }
    
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }
}
