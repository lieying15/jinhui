package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.viewlib.materialprogressbar.MaterialProgressBar;


public class DialogDownload extends Dialog {

    public DialogDownload(Context context, int theme) {
        super(context, theme);
    }

    public DialogDownload(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private String title;

        private TextView title_tv,nowprogress_tv,allprogress_tv;
        private ImageView title_iv;
        private MaterialProgressBar progressBar;
        private DialogDownload dialog;


        public Builder(Context context) {
            this.context = context;
        }



        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setProgress(int progress) {
            if(progressBar != null && nowprogress_tv!=null){
                progressBar.setProgress(progress);
                nowprogress_tv.setText(progress + "");
                allprogress_tv.setText("100");
            }
            return this;
        }


        public DialogDownload create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dialog = new DialogDownload(context, R.style.dialog);
            dialog.getWindow().setBackgroundDrawable(null);
            View layout = inflater.inflate(R.layout.view_dowload, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            title_iv = ((ImageView) layout.findViewById(R.id.dialog_download_title_iv));
            title_tv = ((TextView) layout.findViewById(R.id.dialog_download_title_tv));
            allprogress_tv = ((TextView) layout.findViewById(R.id.dialog_download_allprogress_tv));
            nowprogress_tv = ((TextView) layout.findViewById(R.id.dialog_download_nowprogress_tv));
            progressBar = ((MaterialProgressBar) layout.findViewById(R.id.dialog_download_progressbar));
            if(! title.equals("")){
                title_tv.setText((title));
                title_tv.setVisibility(View.VISIBLE);
            }
            title_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://www.mjmw365.com/app_download/mall/index.html");
                    Intent intent = new  Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.setContentView(layout);
            return dialog;
        }


        public void cancle(){
            dialog.dismiss();
        }
    }


}

