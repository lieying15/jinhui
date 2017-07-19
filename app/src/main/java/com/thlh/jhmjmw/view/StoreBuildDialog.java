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

public class StoreBuildDialog extends Dialog {

    public StoreBuildDialog(Context context, int theme) {
        super(context, theme);
    }

    public StoreBuildDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private boolean canceloutside = true;
        private boolean cancelable = true;


        private TextView btnll;
        private OnClickListener leftClickListener;
        private ImageView titleIv;


        public Builder(Context context) {
            this.context = context;
        }


        public Builder setCancelOutside(boolean canceloutside) {
            this.canceloutside = canceloutside;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setLeftClickListener(OnClickListener listener) {
            this.leftClickListener = listener;
            return this;
        }


        public StoreBuildDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final StoreBuildDialog dialog = new StoreBuildDialog(context, R.style.dialog);
            dialog.getWindow().setBackgroundDrawable(null);
            View layout = inflater.inflate(R.layout.view_dialog_build, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            dialog.setCanceledOnTouchOutside(canceloutside);
            dialog.setCancelable(cancelable);
            btnll = ((TextView) layout.findViewById(R.id.dialog_build_bottom_tv));

            if (leftClickListener != null) {
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

