package com.thlh.jhmjmw.business.entrance.regist;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.ProtocolRegisterActivity;


/**
 * Created by huqiang on 2017/1/4.
 */

public class RegistDialogFragment extends DialogFragment {
    private Dialog builder;
    private TextView  contentTv,nextTv,timeTv,protocoTv;
    private EditText  passwordEt, passwordReEt,verifycodeEt,phoneEt;
    private ImageView titleIv, cancelIv;
    private LinearLayout passwordLl, passwordReLl,verifycodeLl,timeLl,
            phoneLl,protocoLl,nextLl;
    private CheckBox protocoCb;


    private onStepListener listener;
    private int registStep;
    private boolean protocoSelect = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new Dialog(getActivity(), R.style.dialogNoBg);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_fragmentdialog_regist, null);


        titleIv = (ImageView) view.findViewById(R.id.regist_dialogfrg_title_iv);
        cancelIv = (ImageView) view.findViewById(R.id.regist_dialogfrg_cancel_iv);

        phoneLl = (LinearLayout) view.findViewById(R.id.regist_dialogfrg_phone_ll);
        phoneEt = (EditText) view.findViewById(R.id.regist_dialogfrg_phone_et);

        protocoLl = (LinearLayout) view.findViewById(R.id.regist_dialogfrg_protocol_ll);
        protocoTv = (TextView) view.findViewById(R.id.regist_dialogfrg_protocol_tv);
        protocoCb = (CheckBox) view.findViewById(R.id.regist_dialogfrg_protocol_cb);
        protocoCb.setChecked(protocoSelect);
        protocoCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    protocoSelect = true;
                } else {
                    protocoSelect = false;
                }
            }
        });
        protocoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProtocolRegisterActivity.activityStart(getActivity());
            }
        });

        verifycodeLl = (LinearLayout) view.findViewById(R.id.regist_verifycode_ll);
        verifycodeEt = (EditText) view.findViewById(R.id.regist_verifycode_et);

        timeLl = (LinearLayout) view.findViewById(R.id.regist_time_ll);
        timeTv = (TextView) view.findViewById(R.id.regist_time_tv);
        timeLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RegistDialogFragment.this.listener != null) {
                    RegistDialogFragment.this.listener.getVerifyCode();
                }
            }
        });
        passwordLl = (LinearLayout) view.findViewById(R.id.regist_dialogfrg_password_ll);
        passwordEt = (EditText) view.findViewById(R.id.regist_dialogfrg_password_et);

        passwordReLl = (LinearLayout) view.findViewById(R.id.regist_passwordsecond_ll);
        passwordReEt = (EditText) view.findViewById(R.id.regist_passwordsecond_et);

        contentTv = (TextView) view.findViewById(R.id.regist_dialogfrg_content_tv);

        nextLl = (LinearLayout) view.findViewById(R.id.regist_dialogfrg_next_ll);

        nextLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RegistDialogFragment.this.listener != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(phoneEt.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(verifycodeEt.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(passwordEt.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(passwordReEt.getWindowToken(), 0);
                    RegistDialogFragment.this.listener.next(registStep);
                }
            }
        });

        nextTv = (TextView) view.findViewById(R.id.regist_dialogfrg_next_tv);
        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistDialogFragment.this.listener.cancel(registStep);
            }
        });

        changeContentType();
        builder.setContentView(view);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    RegistDialogFragment.this.listener.cancel(registStep);
                    return true;
                }
                return false;
            }
        });
        return builder;
    }


    private void changeContentType() {
        phoneLl.setVisibility(View.GONE);
        protocoLl.setVisibility(View.GONE);
        verifycodeLl.setVisibility(View.GONE);
        timeLl.setVisibility(View.GONE);
        passwordLl.setVisibility(View.GONE);
        passwordReLl.setVisibility(View.GONE);
        contentTv.setVisibility(View.GONE);
        switch (registStep) {
            case 1:
                phoneLl.setVisibility(View.VISIBLE);
                protocoLl.setVisibility(View.VISIBLE);
                nextTv.setText(getResources().getString(R.string.next));
                break;
            case 2:
                verifycodeLl.setVisibility(View.VISIBLE);
                timeLl.setVisibility(View.VISIBLE);
                nextTv.setText(getResources().getString(R.string.trues));
                break;
            case 3:
                passwordLl.setVisibility(View.VISIBLE);
                passwordReLl.setVisibility(View.VISIBLE);
                nextTv.setText(getResources().getString(R.string.next));
                break;
            case 4:
                contentTv.setVisibility(View.VISIBLE);
                nextTv.setText(getResources().getString(R.string.trues));
                break;
        }
    }

    public int getRegistStep() {
        return registStep;
    }

    public void setRegistStep(int registStep) {
        this.registStep = registStep;
    }

    public void setNextEvent(onStepListener listener) {
        this.listener = listener;
    }

    public boolean isProtocoSelect() {
        return protocoSelect;
    }

    public void setProtocoSelect(boolean protocoSelect) {
        this.protocoSelect = protocoSelect;
    }

    public interface onStepListener {
        void next(int step);
        void cancel(int step);
        void getVerifyCode();
    }

    public TextView getTimeTv() {
        return timeTv;
    }

    public void setTimeTv(TextView timeTv) {
        this.timeTv = timeTv;
    }

    public LinearLayout getTimeLl() {
        return timeLl;
    }

    public void setTimeLl(LinearLayout timeLl) {
        this.timeLl = timeLl;
    }

    public EditText getPhoneEt() {
        return phoneEt;
    }

    public EditText getPasswordEt() {
        return passwordEt;
    }

    public EditText getPasswordReEt() {
        return passwordReEt;
    }

    public EditText getVerifycodeEt() {
        return verifycodeEt;
    }


}
   
