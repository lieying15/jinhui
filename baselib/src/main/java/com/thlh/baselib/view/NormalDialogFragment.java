package com.thlh.baselib.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.R;
import com.thlh.baselib.utils.DialogUtils;


/**
 * Created by huqiang on 2017/1/4.
 */

public class NormalDialogFragment extends DialogFragment {
    private Dialog builder;
    private TextView titleTv, contentTv, firstBtnTv, middleBtnTv, finalBtnTv;
    private ImageView titleIv, cancelIv;
    private LinearLayout firstBtnLl, middleBtnLl, finalBtnLl; //三个按钮，默认显示第三个

    private View.OnClickListener firstBtnClickListener,middleBtnClickListener,finalBtnClickListener;

    private String contentStr, titleStr, firstBtnStr, middleBtnStr, finalBtnStr = "返回";
    private CharSequence contentChar;

    private int titleIvRes;
    private int type = DialogUtils.TYPE_NORMAL_SUCCESS;
    private int textGravity = Gravity.CENTER;
    private int cancelVisible = View.GONE,firstBtnVisible =View.GONE, middleBtnVisible = View.GONE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new Dialog(getActivity(), R.style.dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_framentdialog_normal, null);

        titleTv = (TextView) view.findViewById(R.id.frgdialog_normal_title_tv);
        contentTv = (TextView) view.findViewById(R.id.frgdialog_normal_content_tv);

        titleIv = (ImageView) view.findViewById(R.id.frgdialog_normal_title_iv);
        cancelIv = (ImageView) view.findViewById(R.id.frgdialog_normal_cancel_iv);

        firstBtnLl = (LinearLayout) view.findViewById(R.id.frgdialog_normal_fristbtnll);
        firstBtnLl.setVisibility(firstBtnVisible);
        firstBtnLl.setOnClickListener(firstBtnClickListener);
        firstBtnTv = (TextView) view.findViewById(R.id.frgdialog_normal_fristbtntv);

        middleBtnLl = (LinearLayout) view.findViewById(R.id.frgdialog_normal_middlebtnll);
        middleBtnLl.setVisibility(middleBtnVisible);
        middleBtnLl.setOnClickListener(middleBtnClickListener);
        middleBtnTv = (TextView) view.findViewById(R.id.frgdialog_normal_middlebtntv);

        finalBtnLl = (LinearLayout) view.findViewById(R.id.frgdialog_normal_finalbtnll);
        finalBtnLl.setOnClickListener(finalBtnClickListener);
        finalBtnTv = (TextView) view.findViewById(R.id.frgdialog_normal_finalbtntv);

        titleTv.setText(titleStr);
        if (contentStr == null || contentStr.equals("")) {
            if (contentChar != null && !contentChar.equals("")) {
                contentTv.setText(contentChar);
            }
        } else {
            contentTv.setText(contentStr);
        }
        cancelIv.setVisibility(cancelVisible);
        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        titleTv.setGravity(textGravity);
        firstBtnTv.setText(firstBtnStr);
        middleBtnTv.setText(middleBtnStr);
        finalBtnTv.setText(finalBtnStr);

        if (titleIvRes == 0) {
            changeContentType();
        } else {
            titleIv.setImageResource(titleIvRes);
        }
        builder.setContentView(view);
        return builder;
    }


    public void setFirstBtnClickListener(View.OnClickListener listener) {
        this.firstBtnClickListener = listener;
    }

    public void setMiddleBtnClickListener(View.OnClickListener listener) {
        this.middleBtnClickListener = listener;
    }

    public void setFinalBtnClickListener(View.OnClickListener listener) {
        this.finalBtnClickListener = listener;
    }

    public void setContentStr(String contentStr) {
        this.contentStr = contentStr;
    }

    public void setContentStr(CharSequence contentStr) {
        this.contentChar = contentStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public void setFirstBtnStr(String firstBtnStr) {
        this.firstBtnStr = firstBtnStr;
    }

    public void setMiddleBtnStr(String middleBtnStr) {
        this.middleBtnStr = middleBtnStr;
    }

    public void setFinalBtnStr(String finalBtnStr) {
        this.finalBtnStr = finalBtnStr;
    }

    public void setContentType(int type) {
        this.type = type;
    }

    private void changeContentType() {
        switch (type) {
            case DialogUtils.TYPE_NORMAL_SUCCESS:
                titleIvRes = R.drawable.icon_dialog_success;
                titleIv.setImageResource(titleIvRes);
                titleTv.setVisibility(View.GONE);
                cancelIv.setVisibility(View.GONE);
                break;
            case DialogUtils.TYPE_NORMAL_ERROR:
                titleIvRes = R.drawable.icon_dialog_error;
                titleIv.setImageResource(titleIvRes);
                titleTv.setVisibility(View.GONE);
                cancelIv.setVisibility(View.GONE);
                break;
            case DialogUtils.TYPE_NORMAL_WARNING:
                titleIvRes = R.drawable.icon_dialog_warning;
                titleIv.setImageResource(titleIvRes);
                titleTv.setVisibility(View.GONE);
                cancelIv.setVisibility(View.GONE);
                break;
        }
    }


    public void setTitleIvRes(int imgres) {
        titleIvRes = imgres;
        if (titleIv != null)
            titleIv.setImageResource(titleIvRes);
    }


    public void setContentGravity(int textGravity) {
        this.textGravity = textGravity;
    }

    public void setFirstBtnVisible(int visible){
        firstBtnVisible = visible;
        if(firstBtnLl !=null)
        firstBtnLl.setVisibility(visible);
    }

    public void setMiddleBtnVisible(int visible){
        middleBtnVisible = visible;
        if(middleBtnLl!=null)
        middleBtnLl.setVisibility(visible);
    }

    public void setCancelVisible(int visible){
        cancelVisible = visible;
        if(cancelIv!= null)
        cancelIv.setVisibility(visible);
    }


}
   
