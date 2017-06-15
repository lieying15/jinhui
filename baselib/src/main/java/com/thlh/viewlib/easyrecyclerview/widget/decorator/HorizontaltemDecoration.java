package com.thlh.viewlib.easyrecyclerview.widget.decorator;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by huqiang on 2017/2/6.
 */

public class HorizontaltemDecoration extends RecyclerView.ItemDecoration{

    private int space;

    public HorizontaltemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

//        if(parent.getChildPosition(view) != 0)
            outRect.right = space;
    }
}