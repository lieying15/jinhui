package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.thlh.jhmjmw.R;

public class DialogFreightInfo extends Dialog {

    public DialogFreightInfo(Context context, int theme) {
        super(context, theme);
    }

    public DialogFreightInfo(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private boolean canceloutside = true;

        private OnClickListener colseClickListener;
        private FrameLayout colseFl;


        public Builder(Context context) {
            this.context = context;
        }




        public Builder setCancelOutside(boolean canceloutside) {
            this.canceloutside = canceloutside;
            return this;
        }

        public Builder setLeftClickListener(OnClickListener listener) {
            this.colseClickListener = listener;
            return this;
        }
        public Builder setRightClickListener(OnClickListener listener) {
            this.colseClickListener = listener;
            return this;
        }



        public DialogFreightInfo create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogFreightInfo dialog = new DialogFreightInfo(context, R.style.dialog);
            dialog.getWindow().setBackgroundDrawable(null);

            View layout = inflater.inflate(R.layout.view_dialog_freight_info, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            dialog.setCanceledOnTouchOutside(canceloutside);
            colseFl = ((FrameLayout) layout.findViewById(R.id.colse_fl));

            colseFl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setContentView(layout);
            return dialog;
        }
    }

}