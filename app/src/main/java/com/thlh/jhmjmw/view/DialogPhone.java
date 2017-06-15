package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.jhmjmw.R;

/**
 * Created by HQ on 2016/4/1.
 */
public class DialogPhone extends Dialog{


    public DialogPhone(Context context, int theme) {
        super(context, theme);
    }

    public DialogPhone(Context context) {
        super(context);
    }

    public static class Builder {
        private TextView dialogResponseTitleTv;
        private TextView dialogResponseContentTv;
        private ImageView canceliv;
        private FrameLayout dialogResponseBottomFl;
        private Context context;

        private String title = "",content = "";
        private Button  btn_call;
        
        private OnClickListener buttonClickListener;

        public Builder(Context context) {
            this.context = context;
        }



        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }
        

        public Builder setButtonListener(OnClickListener listener) {
            this.buttonClickListener = listener;
            return this;
        }

        public DialogPhone create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogPhone dialog = new DialogPhone(context, R.style.dialog);
            View layout = inflater.inflate(R.layout.view_dialog_phone, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            dialogResponseTitleTv = ((TextView) layout.findViewById(R.id.dialog_phone_title_tv));
            canceliv = ((ImageView) layout.findViewById(R.id.dialog_phone_cancel_iv));
            if(title == null||title.equals("")){
                dialogResponseTitleTv.setVisibility(View.GONE);
            }else {
                dialogResponseTitleTv.setText(title);
            }

            canceliv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialogResponseContentTv = ((TextView) layout.findViewById(R.id.dialog_phone_content_tv));
            if(content == null||content.equals("")){
                dialogResponseContentTv.setVisibility(View.GONE);
            }else {
                dialogResponseContentTv.setText(content);
            }
            

            dialogResponseBottomFl = ((FrameLayout) layout.findViewById(R.id.dialog_phone_bottom_ll));
            if (buttonClickListener != null) {
                dialogResponseBottomFl.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        buttonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }

            dialog.setContentView(layout);
            return dialog;
        }

    }

}

