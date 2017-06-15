package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.jhmjmw.R;

/**
 *绑定冰箱、小店返回的信息窗口
 */
public class DialogBindInfo extends Dialog{


    public DialogBindInfo(Context context, int theme) {
        super(context, theme);
    }

    public DialogBindInfo(Context context) {
        super(context);
    }

    public static class Builder {
        private TextView codeTv ;
        private TextView nameTv ;
        private TextView addressTv;
        private TextView contactphoneTv ;
        private TextView serviceaddressTv ;
        private TextView  serviceaphoneTv ;
        
        private TextView responseViewLeftTv;
        private TextView responseViewRightTv;
        private Context context;
        private boolean canceloutside = false;
        private boolean cancelable = false;
        private OnClickListener leftButtonClickListener;
        private OnClickListener rightButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        private String code ,name ,address,phone,serviceaddress, servicephone;

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddress(String address) {
            this.address = address;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setServiceAddress(String serviceaddress) {
            this.serviceaddress = serviceaddress;
            return this;
        }

        public Builder setServicePhone(String  servicephone) {
            this. servicephone =  servicephone;
            return this;
        }
        
        public Builder setLeftButtonListener(OnClickListener listener) {
            this.leftButtonClickListener = listener;
            return this;
        }

        public Builder setRifhtButtonListener(OnClickListener listener) {
            this.rightButtonClickListener = listener;
            return this;
        }

        public Builder setCancelOutside(boolean canceloutside) {
            this.canceloutside = canceloutside;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public DialogBindInfo create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogBindInfo dialog = new DialogBindInfo(context, R.style.dialog);

            View layout = inflater.inflate(R.layout.view_dialog_bind_response, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            dialog.setCanceledOnTouchOutside(canceloutside);
            dialog.setCancelable(cancelable);
            LinearLayout topLl = ((LinearLayout) layout.findViewById(R.id.bind_dialog_top_ll));
            topLl.setMinimumWidth(BaseApplication.width);
            codeTv = ((TextView) layout.findViewById(R.id.bind_dialog_code_tv));
            nameTv = ((TextView) layout.findViewById(R.id.bind_dialog_name_tv));
            addressTv = ((TextView) layout.findViewById(R.id.bind_dialog_address_tv));
            contactphoneTv = ((TextView) layout.findViewById(R.id.bind_dialog_contact_phone_tv));
            serviceaddressTv = ((TextView) layout.findViewById(R.id.bind_dialog_service_address_tv));
             serviceaphoneTv = ((TextView) layout.findViewById(R.id.bind_dialog_service_phone_tv));
            
            if(code !=null){
                codeTv.setText("冰箱编码："+code);
            }else {
                codeTv.setVisibility(View.GONE);
            }

            if(name !=null){
                nameTv.setText("姓名："+name);
            }else {
                nameTv.setVisibility(View.GONE);
            }

            if(address !=null){
                addressTv.setText("家庭地址："+address);
            }else {
                addressTv.setVisibility(View.GONE);
            }

            if(phone !=null){
                contactphoneTv.setText("联系电话："+phone);
            }else {
                contactphoneTv.setVisibility(View.GONE);
            }

            if(serviceaddress !=null){
                serviceaddressTv.setText("专属服务店地址："+serviceaddress);
            }else {
                serviceaddressTv.setVisibility(View.GONE);
            }

            if(servicephone !=null){
                 serviceaphoneTv.setText("专属服务店电话："+servicephone);
            }else {
                 serviceaphoneTv.setVisibility(View.GONE);
            }

//            左按钮及监听
            responseViewLeftTv = ((TextView) layout.findViewById(R.id.bind_devices_back_tv));

            if (leftButtonClickListener != null) {
                responseViewLeftTv.setVisibility(View.VISIBLE);
                responseViewLeftTv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        leftButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }
//          右按钮及监听
            responseViewRightTv = ((TextView) layout.findViewById(R.id.bind_devices_next_tv));
            if (rightButtonClickListener != null) {
                responseViewRightTv.setVisibility(View.VISIBLE);
                responseViewRightTv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        rightButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }

            dialog.getWindow().setWindowAnimations(R.style.dialogPopAnim);  //添加动画
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.setContentView(layout);
            return dialog;
        }

    }

}

