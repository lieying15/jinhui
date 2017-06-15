package com.thlh.baselib.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.thlh.baselib.R;
import com.thlh.baselib.base.BaseApplication;


/**
 * Snackbar
 */
public class S {

    private S(){}

    public static class Builder {


        private Context context;
        private static View mView;

        private  String mContent;
        private  Context mContext;
        private  int mTextColor  =  Color.parseColor("#ffffff");
        private  int mBackColor =  Color.parseColor("#a40000");
        private  int mDuration = Snackbar.LENGTH_LONG;

        public  Builder(View view,String content) {
//        public  Builder(Context context,View view,String content) {
            mContext = BaseApplication.getInstance().getBaseContext();
            mView = view;
            mContent = content;
        }



        public Builder setContent(String content) {
            mContent = content;
            return this;
        }

        public Builder setmTextColor(int mTextColor) {
            this.mTextColor = mTextColor;
            return this;
        }

        public Builder setmBackColor(int mBackColor) {
            this.mBackColor = mBackColor;
            return this;
        }

        public Snackbar create() {
            Snackbar snackbar = Snackbar.make(mView,mContent, mDuration);

            if(mBackColor != 0){
                snackbar.getView().setBackgroundColor(mBackColor);
            }
            if(mTextColor!=0){
                View view = snackbar.getView();
                TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
                textView.setTextColor(mTextColor);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
            }
            return snackbar;
        }

    }
}

