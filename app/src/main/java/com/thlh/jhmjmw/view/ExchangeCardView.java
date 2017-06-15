package com.thlh.jhmjmw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.utils.Tos;
import com.thlh.jhmjmw.R;

/**
 * Created by HQ on 2016/3/30.
 */
public class ExchangeCardView extends LinearLayout {

    private Context context;
    private ImageView exchangeCardTitleIv;
    private ImageView exchangeCardCancelIv;
    private TextView exchangeCardContentTv;
    private TextView exchangeCardTitleTv;
    private EditText exchangeCardpasswordEt;
    private FrameLayout exchangeCardNextactionFl;

    private OnExchangeCardlistener exchangeCardlistener;

    public void setExchangeCardlistener(OnExchangeCardlistener exchangeCardlistener) {
        this.exchangeCardlistener = exchangeCardlistener;
    }

    public ExchangeCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    public ExchangeCardView(Context context) {
        super(context);
        this.context = context;
        initView();

    }

    public ExchangeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.view_exchange_card_layout,this, true);

        exchangeCardTitleIv = ((ImageView) view.findViewById(R.id.exchange_card_title_iv));
        exchangeCardCancelIv = ((ImageView) view.findViewById(R.id.exchange_card_cancel_iv));
        exchangeCardTitleTv = ((TextView) view.findViewById(R.id.exchange_card_title_tv));
        exchangeCardContentTv = ((TextView) view.findViewById(R.id.exchange_card_content_tv));
        exchangeCardpasswordEt = ((EditText) view.findViewById(R.id.exchange_card_password_et));
        exchangeCardNextactionFl = ((FrameLayout) view.findViewById(R.id.exchange_card_nextaction_fl));

        exchangeCardNextactionFl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = exchangeCardpasswordEt.getText().toString().trim();
                if (pass != null && pass.length() > 0){
                        exchangeCardlistener.getPassword(pass);
                }else {
                    Tos.show(getResources().getString(R.string.password_null));
                }
            }
        });

    }

    public interface OnExchangeCardlistener{
        void getPassword(String pass);
    }

    public void setNextactionListener(OnClickListener listener){

    }

}
