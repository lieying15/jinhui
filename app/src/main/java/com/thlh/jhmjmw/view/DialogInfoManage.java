package com.thlh.jhmjmw.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thlh.jhmjmw.R;
import com.thlh.viewlib.PickerView;
import com.thlh.viewlib.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息中心窗口
 */
public class DialogInfoManage extends Dialog{


    public DialogInfoManage(Context context, int theme) {
        super(context, theme);
    }

    public DialogInfoManage(Context context) {
        super(context);
    }

    public static class Builder {
        private List<String> mindata = new ArrayList<String>();
        private List<String> hourdata = new ArrayList<String>();

        private ToggleButton remindBtn ;
        private PickerView openHourPv ;
        private PickerView openMinutePv ;
        private PickerView closeHourPv;
        private PickerView closeMinutePv ;
        private TextView clearTv ;
        private Context context;
        
        private OnClickListener btnClickListener;
        private String openhour = "21",openmin = "30",closehour = "08",closemin = "30";

        public Builder(Context context) {
            this.context = context;
        }

        
        public Builder setButtonListener(OnClickListener listener) {
            this.btnClickListener = listener;
            return this;
        }


        public DialogInfoManage create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final DialogInfoManage dialog = new DialogInfoManage(context, R.style.dialog);

            View layout = inflater.inflate(R.layout.view_dialog_info_manage, null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            for (int i = 0; i < 10; i++) {
                hourdata.add("0" + i);
            }
            for (int i = 10; i < 24; i++) {
                hourdata.add("" + i);
            }
            for (int i = 0; i < 10; i++) {
                mindata.add("0" + i);
            }
            for (int i = 10; i < 60; i++) {
                mindata.add("" + i);
            }

            remindBtn = ((ToggleButton) layout.findViewById(R.id.dialog_info_manage_remind_btn));
            openHourPv = ((PickerView) layout.findViewById(R.id.open_hour_pv));
            openMinutePv = ((PickerView) layout.findViewById(R.id.open_minute_pv));
            closeHourPv = ((PickerView) layout.findViewById(R.id.close_hour_pv));
            closeMinutePv = ((PickerView) layout.findViewById(R.id.close_minute_pv));
            clearTv = ((TextView) layout.findViewById(R.id.dialog_info_manage_clear_tv));

            openMinutePv.setData(mindata);
            openMinutePv.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    openmin = text;
                }
            });

            openHourPv.setData(hourdata,8);
            openHourPv.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    openmin = text;
                }
            });

            closeMinutePv.setData(mindata);
            closeMinutePv.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    closemin = text;
                }
            });

            closeHourPv.setData(hourdata,18);
            closeHourPv.setOnSelectListener(new PickerView.onSelectListener() {
                @Override
                public void onSelect(String text) {
                    closehour = text;
                }
            });

            if (btnClickListener != null) {
                clearTv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        btnClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
            }


            dialog.setContentView(layout);
            return dialog;
        }

    }

}

