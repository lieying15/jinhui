package com.thlh.jhmjmw.business.index.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.baselib.utils.GlideCircleTransform;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.TextUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.devices.BindDevicesActivity;
import com.thlh.jhmjmw.business.devices.IceboxActivity;
import com.thlh.jhmjmw.business.index.IndexActivity;
import com.thlh.jhmjmw.business.order.list.OrderListActivity;
import com.thlh.jhmjmw.business.recharge.RechargeActivity;
import com.thlh.jhmjmw.business.recharge.RechargeQRActivity;
import com.thlh.jhmjmw.business.user.SettingActivity;
import com.thlh.jhmjmw.business.user.UserInfoActivity;
import com.thlh.jhmjmw.business.user.UserSafeActivity;
import com.thlh.jhmjmw.business.user.address.AddrManageActivity;
import com.thlh.jhmjmw.business.user.collection.CollectActivity;
import com.thlh.jhmjmw.business.user.coupon.CouponActivity;
import com.thlh.jhmjmw.business.user.coupon.ExchangeCardActivity;
import com.thlh.jhmjmw.business.user.footprint.FootPrintActivity;
import com.thlh.jhmjmw.business.user.help.HelpActivity;
import com.thlh.jhmjmw.business.user.wallet.MjzActivity;
import com.thlh.jhmjmw.business.user.wallet.WalletActivity;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.BottomTabViewV3;
import com.thlh.jhmjmw.view.DialogPhone;
import com.thlh.viewlib.ripple.RippleLinearLayout;
import com.thlh.viewlib.ripple.RippleRelativeLayout;
import com.thlh.viewlib.tablayout.MsgView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment implements RippleLinearLayout.OnRippleCompleteListener, RippleRelativeLayout.OnRippleCompleteListener, MineContract.View {
    private static final String TAG = "MineFragment";
    private final int ACTIVITY_CODE_SERVICE = 1;
    public final int ACTIVITY_CODE_SETTING = 2;
    private final int ACTIVITY_CODE_SCANNIN = 3;
    private final int ACTIVITY_CODE_RECHARGE = 0;
    @BindView(R.id.mine_avatar_iv)
    ImageView mineAvatarIv;
    @BindView(R.id.mine_name_tv)
    TextView mineNameTv;
    @BindView(R.id.mine_mjz_tv)
    TextView mineMjzTv;
    @BindView(R.id.mine_mjz_ll)
    LinearLayout mineMjzRipLl;
    @BindView(R.id.mine_wallet_ripll)
    RippleLinearLayout mineWalletRipLl;
    /*
    * 充值*/
    @BindView(R.id.mine_recharge_ripll)
    RippleLinearLayout mineRechargeRipLl;
    @BindView(R.id.mine_wait_gain_msgtv)
    MsgView mineWaitGainMsgtv;
    @BindView(R.id.mine_wait_sendout_msgtv)
    MsgView mineWaitSendoutMsgtv;
    @BindView(R.id.mine_wait_comment_msgtv)
    MsgView mineWaitCommentMsgtv;
    @BindView(R.id.mine_wait_pay_msgtv)
    MsgView mineWaitPayMsgtv;
    @BindView(R.id.mine_goods_return_tv)
    TextView mineGoodsReturnMsgtv;
    @BindView(R.id.mine_collect_ripll)
    RippleLinearLayout mineCollectRipll;
    @BindView(R.id.mine_order_ripll)
    RippleLinearLayout mineOrderRipll;
    @BindView(R.id.mine_coupon_iv)
    ImageView mineCouponIv;
    @BindView(R.id.mine_coupon_msgtv)
    MsgView mineCouponMsgtv;
    @BindView(R.id.mine_coupon_ripll)
    RippleRelativeLayout mineCouponRipll;
    @BindView(R.id.mine_footmark_ripll)
    RippleLinearLayout mineFootmarkRipll;
    @BindView(R.id.mine_address_ripll)
    RippleLinearLayout mineAddressRipll;
    @BindView(R.id.mine_device_ripll)
    RippleLinearLayout mineDeviceRipll;
    @BindView(R.id.mine_help_ripll)
    RippleLinearLayout mineHelpRipll;
    @BindView(R.id.mine_setting_ripll)
    RippleLinearLayout mineSettingRipll;
    @BindView(R.id.mine_service_ripll)
    RippleLinearLayout mineServiceRipll;
    @BindView(R.id.mine_wechat_ripll)
    RippleLinearLayout mineWechatRipll;
    @BindView(R.id.mine_more_ripll)
    RippleLinearLayout mineMoreRipll;
    @BindView(R.id.mine_safe_ripll)
    RippleLinearLayout mineSafeRipll;
    @BindView(R.id.mine_wait_gain_tv)
    TextView mineWaitGainTv;
    @BindView(R.id.mine_wait_sendout_tv)
    TextView mineWaitSendoutTv;
    @BindView(R.id.mine_wait_comment_tv)
    TextView mineWaitCommentTv;
    @BindView(R.id.mine_wait_pay_tv)
    TextView mineWaitPayTv;
    @BindView(R.id.mine_orderall_tv)
    TextView mineOrderallTv;

    @BindView(R.id.mine_changecard_iv)
    ImageView mineChangecardIv;
    @BindView(R.id.mine_changecard_msgtv)
    MsgView mineChangecardMsgtv;
    @BindView(R.id.mine_changecard_ripll)
    RippleRelativeLayout mineChangecardRipll;
    Unbinder unbinder;


    private DialogPhone.Builder phoneDialog;
    public MineContract.Presenter mPresenter;
    private String url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //静态内部类方法创建单例
    public static class MineFragmentLoader {
        private static final MineFragment instance = new MineFragment();
    }


    public static MineFragment newInstance() {
        return MineFragmentLoader.instance;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mine_v3;
    }

    @Override
    protected void initVariables() {
    }

    @Override
    protected void initView() {
        mPresenter = new MinePresenter(this);
        initListener();
        updateMineNameTv();
        updateMineAvatarIv();
        phoneDialog = new DialogPhone.Builder(getActivity());
    }

    @Override
    protected void initData() {

    }


    @Override
    protected void loadData() {

    }


    @Override
    public void onStart() {
        super.onStart();
        if (SPUtils.getIsLogin()) {
            mPresenter.loadWallet();//依次读取钱包、优惠券、订单汇总
            updateMjzTv();
        }
    }


    @OnClick({R.id.mine_avatar_iv, R.id.mine_wait_gain_tv, R.id.mine_wait_sendout_tv, R.id.mine_wait_comment_tv,
            R.id.mine_wait_pay_tv, R.id.mine_orderall_tv, R.id.mine_goods_return_tv, R.id.mine_mjz_ll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mine_avatar_iv:
                UserInfoActivity.activityStart(getActivity());
                break;
            case R.id.mine_wait_gain_tv:
                OrderListActivity.activityStart(getActivity(), Constants.ORDER_TYPE_WAIT_GAIN);
                break;
            case R.id.mine_wait_sendout_tv:
                OrderListActivity.activityStart(getActivity(), Constants.ORDER_TYPE_WAIT_SENDOUT);
                break;
            case R.id.mine_wait_comment_tv:
                OrderListActivity.activityStart(getActivity(), Constants.ORDER_TYPE_COMPLETE);
                break;
            case R.id.mine_wait_pay_tv:
                OrderListActivity.activityStart(getActivity(), Constants.ORDER_TYPE_WAIT_PAY);
                break;
            case R.id.mine_orderall_tv:
                OrderListActivity.activityStart(getActivity(), Constants.ORDER_TYPE_ALL);
                break;
            case R.id.mine_goods_return_tv:
                showServiceDialog();
                break;
            case R.id.mine_mjz_ll:
                MjzActivity.activityStart(getActivity());
                break;
        }
    }

    @Override
    public void onComplete(RippleLinearLayout rippleLinearLayout) {
        switch (rippleLinearLayout.getId()) {
            case R.id.mine_collect_ripll:
                CollectActivity.activityStart(getActivity());
                break;
            case R.id.mine_order_ripll:
                OrderListActivity.activityStart(getActivity(), Constants.ORDER_TYPE_ALL);
                break;
            case R.id.mine_wallet_ripll:
                WalletActivity.activityStart(getActivity());
                break;
            /*
            * 美家钻充值
            * */
            case R.id.mine_recharge_ripll:
                //
                int isch = (int) SPUtils.get("user_isch", 0);
                L.e("isch1===============" + isch);
                if (isch > 0) {
                    RechargeActivity.activityStart(getActivity(), Constants.PAY_PURPOSE_MJB, ACTIVITY_CODE_RECHARGE);
                } else {
                    //二维码长按绑定
                    RechargeQRActivity.activityStart(getActivity());
                }
                break;


            case R.id.mine_footmark_ripll:
                FootPrintActivity.activityStart(getActivity());
                break;
            case R.id.mine_address_ripll:
                AddrManageActivity.activityStart(getActivity(), AddrManageActivity.START_TYPE_MINE);
                break;
            /**
             * 智能设备
             * */
            case R.id.mine_device_ripll:
                //
                String equipmentid = (String) SPUtils.get("user_equipmentid", "");
                L.e(TAG + " equipmentid " + equipmentid);
                if (equipmentid.equals("") || equipmentid.equals("0")) {
                    //
                    Intent intent = new Intent();
                    intent.putExtra("title", getResources().getString(R.string.bind_ice));
                    intent.putExtra("sacnContent", getResources().getString(R.string.bind_ice_zing));
                    intent.setClass(getActivity(), BindDevicesActivity.class);
                    //
                    startActivityForResult(intent, ACTIVITY_CODE_SCANNIN);
                } else {
                    IceboxActivity.activityStart(getActivity());
                }
                break;
            case R.id.mine_help_ripll:
                HelpActivity.activityStart(getActivity());
                break;
            case R.id.mine_setting_ripll:
                Intent intent = new Intent();
                intent.setClass(getActivity(), SettingActivity.class);
                startActivityForResult(intent, ACTIVITY_CODE_SETTING);
                break;
            case R.id.mine_service_ripll:
                showServiceDialog();
                break;
            /**
             * 关注公众号
             * */
            case R.id.mine_wechat_ripll:

                break;
            /**
             * 更多功能
             * */
            case R.id.mine_more_ripll:

                break;
            //账户安全
            case R.id.mine_safe_ripll:
                UserSafeActivity.activityStart(getActivity());
                break;
        }
    }


    @Override
    public void onComplete(RippleRelativeLayout rippleRelativeLayout) {
        switch (rippleRelativeLayout.getId()) {
            case R.id.mine_coupon_ripll:
                CouponActivity.activityStart(getActivity());
                break;
            case R.id.mine_changecard_ripll:
                ExchangeCardActivity.activityStart(getActivity());
                break;
        }
    }

    private void initListener() {
        mineCollectRipll.setLLRippleCompleteListener(this);
        mineFootmarkRipll.setLLRippleCompleteListener(this);
        mineAddressRipll.setLLRippleCompleteListener(this);
        mineDeviceRipll.setLLRippleCompleteListener(this);
        mineSettingRipll.setLLRippleCompleteListener(this);
        mineServiceRipll.setLLRippleCompleteListener(this);
        mineWechatRipll.setLLRippleCompleteListener(this);
        mineWalletRipLl.setLLRippleCompleteListener(this);
        mineCollectRipll.setLLRippleCompleteListener(this);
        mineRechargeRipLl.setLLRippleCompleteListener(this);
        mineMoreRipll.setLLRippleCompleteListener(this);
        mineSafeRipll.setLLRippleCompleteListener(this);
        mineHelpRipll.setLLRippleCompleteListener(this);
        mineOrderRipll.setLLRippleCompleteListener(this);
        mineCouponRipll.setRLRippleCompleteListener(this);
        mineChangecardRipll.setRLRippleCompleteListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            L.i("onActivityResult -> RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_SETTING:
                    ((IndexActivity) getActivity()).switchFragment(BottomTabViewV3.POSITON_HOMEPAGE);
                    ((IndexActivity) getActivity()).getBottomTabView().changeTabView(BottomTabViewV3.POSITON_HOMEPAGE, 0);
                    ((IndexActivity) getActivity()).getBottomTabView().recoverColor(BottomTabViewV3.POSITON_MINE);
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void updateMineAvatarIv() {
        String path = (String) SPUtils.get("user_avatar", "");
        if (path.contains("http")){
            url = path;
        }else {
            url = Deployment.IMAGE_PATH + path;
        }
        ImageLoader.display(url, mineAvatarIv, R.drawable.img_mine_avatar, new GlideCircleTransform(getActivity()));
    }

    @Override
    public void updateMineNameTv() {
        String nickname = (String) SPUtils.get("user_nickname", "");
        if (nickname.equals("")) {
            mineNameTv.setText(getResources().getString(R.string.minename));
        } else {
            mineNameTv.setText(nickname);
        }
    }

    @Override
    public void updateMjzTv() {
        mineMjzTv.setText("" + SPUtils.get("user_mjb", "0"));
    }

    @Override
    public void updateCouponTv(String num) {
        if (num.equals("0") || num.equals("")) {
            mineCouponMsgtv.setVisibility(View.INVISIBLE);
        } else {
//            mineCouponMsgtv.setText(num);
            TextUtils.showNum(mineCouponMsgtv, Integer.parseInt(num));
            mineCouponMsgtv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setWaitPayTv(String waitPay) {
        if (waitPay.equals("0") || waitPay.equals("")) {
            mineWaitPayMsgtv.setVisibility(View.INVISIBLE);
        } else {
//            mineWaitPayMsgtv.setText(waitPay);
            TextUtils.showNum(mineWaitPayMsgtv, Integer.parseInt(waitPay));

            mineWaitPayMsgtv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setWaitSendoutTv(String waitSendout) {
        if (waitSendout.equals("0") || waitSendout.equals("")) {
            mineWaitSendoutMsgtv.setVisibility(View.INVISIBLE);
        } else {
            TextUtils.showNum(mineWaitSendoutMsgtv, Integer.parseInt(waitSendout));
            mineWaitSendoutMsgtv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setWaitGainTv(String waitGain) {
        if (waitGain.equals("0") || waitGain.equals("")) {
            mineWaitGainMsgtv.setVisibility(View.INVISIBLE);
        } else {
            TextUtils.showNum(mineWaitGainMsgtv, Integer.parseInt(waitGain));
            mineWaitGainMsgtv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setWaitCommentTv(String waitComment) {
        if (waitComment.equals("0") || waitComment.equals("")) {
            mineWaitCommentMsgtv.setVisibility(View.INVISIBLE);
        } else {
            TextUtils.showNum(mineWaitCommentMsgtv, Integer.parseInt(waitComment));
            mineWaitCommentMsgtv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showServiceDialog() {
        DialogUtils.showPhone((RxAppCompatActivity) getActivity(), getResources().getString(R.string.call_return));
    }

}
