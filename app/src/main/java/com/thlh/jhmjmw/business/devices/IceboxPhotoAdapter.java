package com.thlh.jhmjmw.business.devices;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.model.IceboxPhoto;
import com.thlh.baselib.utils.DisplayUtil;
import com.thlh.baselib.utils.GlideRoundTransform;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.L;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * Created by HQ on 2016/3/30.
 */
public class IceboxPhotoAdapter extends EasyRecyclerViewAdapter {
    private Context context;
    private OnClickListener listener;
    private boolean isShowDelete = false;

    public IceboxPhotoAdapter(Context context) {
        this.context = context;
    }



    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_icebox_img
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder, final int position) {
        ImageView imgsIv = viewHolder.findViewById(R.id.item_icebox_img_iv);
        ImageView cancelIv = viewHolder.findViewById(R.id.item_icebox_img_delete_iv);
        RelativeLayout imgRl = viewHolder.findViewById(R.id.item_icebox_img_rl);
        ViewGroup.LayoutParams layoutParams = imgsIv.getLayoutParams();
        layoutParams.width = (BaseApplication.width - DisplayUtil.dp2px(context,30))/3;
        layoutParams.height = (BaseApplication.width - DisplayUtil.dp2px(context,132))/3;

        if (position < mList.size()){
            IceboxPhoto goodspath = (IceboxPhoto)this.getItem(position);
            L.e("============" + goodspath.getPhoto());
            Glide.with(context)
                    .load(Deployment.IMAGE_PATH + goodspath.getPhoto())
                    .transform(new GlideRoundTransform(context))
                    .centerCrop()
                    .into(imgsIv);

            cancelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onClickCancel(position);
                    }
                }
            });
            if(isShowDelete){
                cancelIv.setVisibility(View.VISIBLE);
            }else {
                cancelIv.setVisibility(View.INVISIBLE);
            }
        }else {
            Glide.with(context)
                    .load(R.color.white)
                    .transform(new GlideRoundTransform(context))
                    .into(imgsIv);
        }

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setCancelListener(OnClickListener listener) {
        this.listener = listener;
    }



    public interface OnClickListener {
        void onClickCancel(int position);
    }

    public void setCancelVisible(boolean isShowDelete) {
        this.isShowDelete =  isShowDelete;
    }

    public boolean isShowDelete() {
        return isShowDelete;
    }

}