package com.thlh.jhmjmw.business.devices;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.jhmjmw.R;


/**
 * Created by huqiang on 2017/1/4.
 */

public class BindDevicesDialogFrg extends DialogFragment {
    private Dialog builder;
    private TextView codeTv ;
    private TextView nameTv ;
    private TextView addressTv;
    private TextView contactphoneTv ;
    private TextView serviceaddressTv ;
    private TextView  serviceaphoneTv ;

    private TextView backTv;
    private TextView nextTv;
    private Context context;
    private boolean canceloutside = false;
    private boolean cancelable = false;
    private String code ,name ,address,phone,serviceaddress, servicephone;
    private onBindListener bindlistener;
    private LinearLayout backLl;
    private LinearLayout nextLl;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        builder = new Dialog(getActivity(), R.style.dialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_dialog_bind_response, null);

        LinearLayout topLl = ((LinearLayout) view.findViewById(R.id.bind_dialog_top_ll));
        codeTv = ((TextView) view.findViewById(R.id.bind_dialog_code_tv));
        nameTv = ((TextView) view.findViewById(R.id.bind_dialog_name_tv));
        addressTv = ((TextView) view.findViewById(R.id.bind_dialog_address_tv));
        contactphoneTv = ((TextView) view.findViewById(R.id.bind_dialog_contact_phone_tv));
        serviceaddressTv = ((TextView) view.findViewById(R.id.bind_dialog_service_address_tv));
        serviceaphoneTv = ((TextView) view.findViewById(R.id.bind_dialog_service_phone_tv));
        backTv = ((TextView) view.findViewById(R.id.bind_devices_back_tv));
        backLl = ((LinearLayout) view.findViewById(R.id.bind_devices_back_ll));
        nextTv = ((TextView) view.findViewById(R.id.bind_devices_next_tv));
        nextLl = ((LinearLayout) view.findViewById(R.id.bind_devices_next_ll));

        if(code !=null){
            codeTv.setText(""+code);
        }else {
            codeTv.setVisibility(View.GONE);
        }

        if(name !=null){
            nameTv.setText(""+name);
        }else {
            nameTv.setVisibility(View.GONE);
        }

        if(address !=null){
            addressTv.setText(""+address);
        }else {
            addressTv.setVisibility(View.GONE);
        }

        if(phone !=null){
            contactphoneTv.setText(""+phone);
        }else {
            contactphoneTv.setVisibility(View.GONE);
        }

        if(serviceaddress !=null){
            serviceaddressTv.setText(""+serviceaddress);
        }else {
            serviceaddressTv.setVisibility(View.GONE);
        }

        if(servicephone !=null){
            serviceaphoneTv.setText(""+servicephone);
        }else {
            serviceaphoneTv.setVisibility(View.GONE);
        }

        backTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        if (bindlistener != null) {
            nextTv.setVisibility(View.VISIBLE);
            nextTv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    bindlistener.binddevices();
                    builder.dismiss();
                }
            });
        }
        builder.setContentView(view);
        return builder;
    }


    public interface onBindListener {
        void binddevices();
    }

    public void setBindEvent(onBindListener listener) {
        this.bindlistener = listener;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getServiceaddress() {
        return serviceaddress;
    }

    public void setServiceaddress(String serviceaddress) {
        this.serviceaddress = serviceaddress;
    }

    public String getServicephone() {
        return servicephone;
    }

    public void setServicephone(String servicephone) {
        this.servicephone = servicephone;
    }
}
   
