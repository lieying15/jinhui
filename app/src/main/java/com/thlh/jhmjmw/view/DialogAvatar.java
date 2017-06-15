package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.jhmjmw.R;

public class DialogAvatar extends Dialog {

    public DialogAvatar(Context context, int theme) {
        super(context, theme);
    }

    public DialogAvatar(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private boolean canceloutside = true;

        private Button albumbtn,camerabtn,cancelbtn;
        private OnClickListener albumListener,camerListener,cancelListener;


        public Builder(Context context) {
            this.context = context;
        }

        public Builder setCancelOutside(boolean canceloutside) {
            this.canceloutside = canceloutside;
            return this;
        }


        public Builder setAlbumListener(OnClickListener listener) {
            this.albumListener = listener;
            return this;
        }
        public Builder setCameraListener(OnClickListener listener) {
            this.camerListener = listener;
            return this;
        }

        public Builder setCancelListener(OnClickListener listener) {
            this.cancelListener = listener;
            return this;
        }


        public DialogAvatar create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogAvatar dialog = new DialogAvatar(context, R.style.dialog);
            dialog.getWindow().setBackgroundDrawable(null);
            View layout = inflater.inflate(R.layout.view_dialog_avatar, null);
            layout.setMinimumWidth(BaseApplication.width);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            dialog.setCanceledOnTouchOutside(canceloutside);
            albumbtn = ((Button) layout.findViewById(R.id.dialog_avatar_album));
            camerabtn = ((Button) layout.findViewById(R.id.dialog_avatar_camera));
            cancelbtn = ((Button) layout.findViewById(R.id.dialog_avatar_cancel));

            if (albumListener != null) {
                albumbtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        albumListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }

            if (camerListener != null) {
                camerabtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        camerListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }

            if (cancelListener != null) {
                cancelbtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        cancelListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
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

