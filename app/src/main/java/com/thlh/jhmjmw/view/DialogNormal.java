package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.jhmjmw.R;

public class DialogNormal extends Dialog {

    public DialogNormal(Context context, int theme) {
        super(context, theme);
    }

    public DialogNormal(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private String title ="",subtitle = "",leftStr  = "",rightStr  = "";
        private boolean canceloutside = true;
        private boolean cancelable = true;

        private Button left_btn,right_btn;
        private TextView title_tv ,subtitle_tv;
        private LinearLayout btnll;
        private OnClickListener leftClickListener,rightClickListener;


        public Builder(Context context) {
            this.context = context;
        }

        public String getSubtitle() {
            return subtitle;
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

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
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



        public DialogNormal create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogNormal dialog = new DialogNormal(context, R.style.dialog);
            dialog.getWindow().setBackgroundDrawable(null);
            View layout = inflater.inflate(R.layout.view_dialog_normal, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            dialog.setCanceledOnTouchOutside(canceloutside);
            dialog.setCancelable(cancelable);
            left_btn = ((Button) layout.findViewById(R.id.dialog_base_top_tv));
            right_btn = ((Button) layout.findViewById(R.id.dialog_base_bottom_tv));
            title_tv = ((TextView) layout.findViewById(R.id.dialog_base_title_tv));
            subtitle_tv = ((TextView) layout.findViewById(R.id.dialog_base_subtitle_tv));
            btnll = ((LinearLayout) layout.findViewById(R.id.dialog_base_btnll));
            if(! title.equals("")){
                title_tv.setText((title));
                title_tv.setVisibility(View.VISIBLE);
            }else {
                title_tv.setVisibility(View.GONE);
            }

            if(! subtitle.equals("")){
                subtitle_tv.setText((getSubtitle()));
                subtitle_tv.setVisibility(View.VISIBLE);
            }


            if(leftClickListener!=null ||rightClickListener !=null){
                btnll.setVisibility(View.VISIBLE);
            }

            if (leftClickListener != null) {
                left_btn.setVisibility(View.VISIBLE);
                left_btn.setText((leftStr));
                left_btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        leftClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }
            if (rightClickListener != null) {
                right_btn.setVisibility(View.VISIBLE);
                right_btn.setText((rightStr));
                right_btn.setOnClickListener(new View.OnClickListener() {
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

