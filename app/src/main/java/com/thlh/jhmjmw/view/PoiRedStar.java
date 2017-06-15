package com.thlh.jhmjmw.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thlh.jhmjmw.R;


/**
 * 自定义评分控件
 *
 */
public class PoiRedStar extends LinearLayout implements View.OnClickListener{
    Context context;
    private static final String TAG = "PoiStar";

    private int icon_star_red = -1;
    private boolean canSelect = false;
    private ImageView poistar1,poistar2,poistar3,poistar4,poistar5;

    public PoiRedStar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_redstar, this, true);
        this.context = context;
        init();
    }

    public PoiRedStar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_redstar, this, true);
        this.context = context;
        init();
    }

    public PoiRedStar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.view_redstar, this, true);
        this.context = context;
        init();
    }

    public int getStar() {
        return this.icon_star_red;
    }

    private void init(){
        poistar1 = (ImageView) findViewById(R.id.poistar1);
        poistar2 = (ImageView) findViewById(R.id.poistar2);
        poistar3 = (ImageView) findViewById(R.id.poistar3);
        poistar4 = (ImageView) findViewById(R.id.poistar4);
        poistar5 = (ImageView) findViewById(R.id.poistar5);


    }
    public  void StarClear(){
        poistar1.setImageResource(R.drawable.icon_star_gray);
        poistar2.setImageResource(R.drawable.icon_star_gray);
        poistar3.setImageResource(R.drawable.icon_star_gray);
        poistar4.setImageResource(R.drawable.icon_star_gray);
        poistar5.setImageResource(R.drawable.icon_star_gray);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.poistar1:
                StarClear();
                setStar(1);
                break;
            case R.id.poistar2:
                StarClear();
                setStar(2);
                break;
            case R.id.poistar3:
                StarClear();
                setStar(3);
                break;
            case R.id.poistar4:
                StarClear();
                setStar(4);
                break;
            case R.id.poistar5:
                StarClear();
                setStar(5);
                break;
        }
    }

    /** 设置参数 含半星
     */
//    public void setStar(int count){
//        this.icon_star_red = count;
//
//        if(count>0&&count<6){
//            poistar1.setImageResource(R.drawable.icon_star_red_half);
//        }else if (count>5&&count<11){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//        }
//        if(count>10&&count<16){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//            poistar2.setImageResource(R.drawable.icon_star_red_half);
//        }else if (count>15&&count<21){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//            poistar2.setImageResource(R.drawable.icon_star_red);
//        }
//        if(count>20&&count<26){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//            poistar2.setImageResource(R.drawable.icon_star_red);
//            poistar3.setImageResource(R.drawable.icon_star_red_half);
//        }else if (count>25&&count<31){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//            poistar2.setImageResource(R.drawable.icon_star_red);
//            poistar3.setImageResource(R.drawable.icon_star_red);
//        }
//        if(count>30&&count<36){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//            poistar2.setImageResource(R.drawable.icon_star_red);
//            poistar3.setImageResource(R.drawable.icon_star_red);
//            poistar4.setImageResource(R.drawable.icon_star_red_half);
//        }else if (count>35&&count<41){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//            poistar2.setImageResource(R.drawable.icon_star_red);
//            poistar3.setImageResource(R.drawable.icon_star_red);
//            poistar4.setImageResource(R.drawable.icon_star_red);
//        }
//        if(count>40&&count<46){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//            poistar2.setImageResource(R.drawable.icon_star_red);
//            poistar3.setImageResource(R.drawable.icon_star_red);
//            poistar4.setImageResource(R.drawable.icon_star_red);
//            poistar5.setImageResource(R.drawable.icon_star_red_half);
//        }else if (count>45&&count<51){
//            poistar1.setImageResource(R.drawable.icon_star_red);
//            poistar2.setImageResource(R.drawable.icon_star_red);
//            poistar3.setImageResource(R.drawable.icon_star_red);
//            poistar4.setImageResource(R.drawable.icon_star_red);
//            poistar5.setImageResource(R.drawable.icon_star_red);
//        }
//    }

    public void setStar(int count){
        this.icon_star_red = count;
        if(count==0){
            return;
        }
        if(count ==1){
            poistar1.setImageResource(R.drawable.icon_star_red);
        }
        if(count==2){
            poistar1.setImageResource(R.drawable.icon_star_red);
            poistar2.setImageResource(R.drawable.icon_star_red);
        }
        if(count==3){
            poistar1.setImageResource(R.drawable.icon_star_red);
            poistar2.setImageResource(R.drawable.icon_star_red);
            poistar3.setImageResource(R.drawable.icon_star_red);
        }
        if(count ==4){
            poistar1.setImageResource(R.drawable.icon_star_red);
            poistar2.setImageResource(R.drawable.icon_star_red);
            poistar3.setImageResource(R.drawable.icon_star_red);
            poistar4.setImageResource(R.drawable.icon_star_red);
        }
        if(count ==5){
            poistar1.setImageResource(R.drawable.icon_star_red);
            poistar2.setImageResource(R.drawable.icon_star_red);
            poistar3.setImageResource(R.drawable.icon_star_red);
            poistar4.setImageResource(R.drawable.icon_star_red);
            poistar5.setImageResource(R.drawable.icon_star_red);
        }
    }

    public boolean isCanSelect() {
        return canSelect;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
        if(canSelect){
            poistar1.setOnClickListener(this);
            poistar2.setOnClickListener(this);
            poistar3.setOnClickListener(this);
            poistar4.setOnClickListener(this);
            poistar5.setOnClickListener(this);
        }else {
            poistar1.setOnClickListener(null);
            poistar2.setOnClickListener(null);
            poistar3.setOnClickListener(null);
            poistar4.setOnClickListener(null);
            poistar5.setOnClickListener(null);
        }
    }
}