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

public class DialogCoupon extends Dialog {

    public DialogCoupon(Context context, int theme) {
        super(context, theme);
    }

    public DialogCoupon(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private String title ="",subtitle = "",leftStr  = "",rightStr  = "";
        private boolean canceloutside = true;

        private TextView topNextTv,bottomNextTv;
        private TextView title_tv ,subtitle_tv;
        private TextView coupontitleTv ,coupontimeTv;
        private ImageView couponIv;
        private OnClickListener leftClickListener,rightClickListener;


        public Builder(Context context) {
            this.context = context;
        }



        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setSubTitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public Builder setCancelOutside(boolean canceloutside) {
            this.canceloutside = canceloutside;
            return this;
        }
        public Builder setLeftBtnStr(String leftStr) {
            this.leftStr = leftStr;
            return this;
        }

        public Builder setRightBtnStr(String rightStr) {
            this.rightStr = rightStr;
            return this;
        }

        public Builder setLeftClickListener(OnClickListener listener) {
            this.leftClickListener = listener;
            return this;
        }
        public Builder setRightClickListener(OnClickListener listener) {
            this.rightClickListener = listener;
            return this;
        }



        public DialogCoupon create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogCoupon dialog = new DialogCoupon(context, R.style.dialog);
            dialog.getWindow().setBackgroundDrawable(null);

            View layout = inflater.inflate(R.layout.view_dialog_coupon, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            dialog.setCanceledOnTouchOutside(canceloutside);
            topNextTv = ((TextView) layout.findViewById(R.id.dialog_base_top_tv));
            bottomNextTv = ((TextView) layout.findViewById(R.id.dialog_base_bottom_tv));
            title_tv = ((TextView) layout.findViewById(R.id.dialog_base_title_tv));
            subtitle_tv = ((TextView) layout.findViewById(R.id.dialog_base_subtitle_tv));

            coupontitleTv = ((TextView) layout.findViewById(R.id.dialog_coupon_title_tv));
            coupontimeTv = ((TextView) layout.findViewById(R.id.dialog_coupon_time_tv));
            couponIv = ((ImageView) layout.findViewById(R.id.dialog_coupon_iv));


            if(! title.equals("")){
                title_tv.setText(title);
                title_tv.setVisibility(View.VISIBLE);
            }
            if(! subtitle.equals("")){
                subtitle_tv.setText(subtitle);
                subtitle_tv.setVisibility(View.VISIBLE);
            }


            if (leftClickListener != null) {
                /*
                * questions---null-->???
                * */
                topNextTv.setVisibility(View.VISIBLE);
                topNextTv.setText(leftStr);
                topNextTv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        leftClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }
            if (rightClickListener != null) {
                bottomNextTv.setVisibility(View.VISIBLE);
                bottomNextTv.setText(rightStr);
                bottomNextTv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        rightClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }

}