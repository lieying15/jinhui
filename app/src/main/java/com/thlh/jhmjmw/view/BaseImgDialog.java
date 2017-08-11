package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.jhmjmw.R;

public class BaseDialog extends Dialog {

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    public BaseDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private String title ="",address = "",phone= "",leftStr  = "";
        private boolean canceloutside = true;
        private boolean cancelable = true;
        private int titleIvRes;

        private TextView title_tv ,addressTv,phoneTv,btnll;
        private OnClickListener leftClickListener;
        private ImageView titleIv;


        public Builder(Context context) {
            this.context = context;
        }


        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitleIv(ImageView titleIv) {
            this.titleIv = titleIv;
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

        public Builder setTitleIvRes(int titleIvRes) {
            this.titleIvRes = titleIvRes;
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



        public Builder setLeftBtnStr(String leftStr) {
            this.leftStr = leftStr;
            return this;
        }

        public Builder setLeftClickListener(OnClickListener listener) {
            this.leftClickListener = listener;
            return this;
        }


        public BaseDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final BaseDialog dialog = new BaseDialog(context, R.style.dialog);
            dialog.getWindow().setBackgroundDrawable(null);
            View layout = inflater.inflate(R.layout.view_dialog_show, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            dialog.setCanceledOnTouchOutside(canceloutside);
            dialog.setCancelable(cancelable);
            titleIv = ((ImageView) layout.findViewById(R.id.dialog_base_title_iv));
            title_tv = ((TextView) layout.findViewById(R.id.dialog_base_title_tv));
            addressTv = ((TextView) layout.findViewById(R.id.dialog_base_address_tv));
            phoneTv = ((TextView) layout.findViewById(R.id.dialog_base_phone_tv));
            btnll = ((TextView) layout.findViewById(R.id.dialog_base_bottom_tv));

            if(! title.equals("")){
                title_tv.setText((title));
            }else {
                title_tv.setVisibility(View.GONE);
            }
            if (titleIvRes != 0){
                titleIv.setImageResource(titleIvRes);
            }
            addressTv.setText(address);
            phoneTv.setText(phone);

            if (leftClickListener != null) {
                btnll.setText((leftStr));
                btnll.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        leftClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }

            dialog.setContentView(layout);
            return dialog;
        }

    }

}

