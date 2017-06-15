package com.thlh.jhmjmw.business.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.config.Constants;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.view.HeaderNormal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 充值协议页
 */
public class ProtocolRechargeActivity extends BaseViewActivity implements View.OnClickListener {
    private final String TAG = "ProtocolRechargeActivity";
    @BindView(R.id.protocol_header)
    HeaderNormal protocolHeader;
    @BindView(R.id.protocol_tv)
    TextView   protocolTv;

    public static void activityStart(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, ProtocolRechargeActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_protocol);
        ButterKnife.bind(this);


        protocolTv.setMinHeight(BaseApplication.height);

        String protocolStr = "1、本协议内容包括协议正文及所有每家美物已经发布的或将来可能发布的各类规则。所有规则为本协议不可分割的组成部分，与协议正文具有同等法律效力。除另行明确声明外，任何每家美物及其关联公司提供的服务均受本协议约束。\n\n" +
                "2、您应当在使用每家美物服务之前认真阅读全部协议内容，如您对协议有任何疑问的，应向每家美物咨询。但无论您事实上是否在使用每家美物服务之前认真阅读了本协议内容，只要您使用每家美物服务，则本协议即对您产生约束，届时您不应以未阅读本协议的内容或者未获得每家美物对您问询的解答等理由，主张本协议无效，或要求撤销本协议。\n\n" +
                "3、您承诺接受并遵守本协议的约定。如果您不同意本协议的约定，您应立即停止充值活动或停止使用每家美物服务。\n\n" +
                "4、一次性充值美家钻（每家美物商城）"+ Constants.RECHARGE_SENDICEBOX_PRICE+"元人民币用于商城商品选购，并赠送美的?每家美物智能双开门冰箱一台（指定机型BCD-533TH1）。\n\n" +
                "5、充值金额不再参加折扣、成长值及其它商城优惠。\n\n" +
                "6、充值金额只可在商城使用，充值金额不可退、不可取出且不可转让赠予。\n\n" +
                "7、每家美物有权根据需要不时地制订、修改本协议及/或各类规则，并以网站公示的方式进行公告，不再单独通知您。变更后的协议和规则一经在网站公布后，立即自动生效。如您不同意相关变更，应当立即停止使用每家美物服务。您继续使用每家美物商城服务，即表示您接受经修订的协议和规则。\n";
       String temp = ToDBC(protocolStr);
        protocolTv.setText(temp);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void onClick(View v) {

    }
    //
    public  String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }

        String  tempstr = new String(c);
        tempstr=tempstr.replaceAll("【","[").replaceAll("】","]").replaceAll("！","!");//替换中文标号
        String regEx="[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(tempstr);
        return m.replaceAll("").trim();
    }

}
