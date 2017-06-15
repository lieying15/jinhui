package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.jhmjmw.R;

public class DialogPromotion extends Dialog {

    public DialogPromotion(Context context, int theme) {
        super(context, theme);
    }

    public DialogPromotion(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private boolean canceloutside = true;
        private ImageView cancelIv;
        private TextView priceTv,numTv;
        private LinearLayout suitLl;
        private OnClickListener selectListener;
        private String price ="",num ="";
        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCancelOutside(boolean canceloutside) {
            this.canceloutside = canceloutside;
            return this;
        }


        public Builder setSelectListener(OnClickListener listener) {
            this.selectListener = listener;
            return this;
        }

        public Builder setPrice(String  price) {
            this.price = price;
            return this;
        }

        public Builder setNum(String  num) {
            this.num = num;
            return this;
        }



        public DialogPromotion create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogPromotion dialog = new DialogPromotion(context, R.style.dialog);
            dialog.getWindow().setBackgroundDrawable(null);
            View layout = inflater.inflate(R.layout.view_dialog_promotion, null);
            layout.setMinimumWidth(BaseApplication.width);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            dialog.setCanceledOnTouchOutside(canceloutside);
            suitLl = ((LinearLayout) layout.findViewById(R.id.dialog_promotion_suit_ll));
            cancelIv = ((ImageView) layout.findViewById(R.id.dialog_promotion_cancel_iv));
            priceTv = ((TextView) layout.findViewById(R.id.dialog_promotion_price_tv));
            numTv = ((TextView) layout.findViewById(R.id.dialog_promotion_num_tv));
            priceTv.setText(price);
            numTv.setText(num);
            if (selectListener != null) {
                suitLl.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        selectListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }

            cancelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.getWindow().setWindowAnimations(R.style.dialogPopAnim);  //添加动画
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.setContentView(layout);
            return dialog;
        }

    }

}

