package com.thlh.jhmjmw.business.devices;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thlh.baselib.model.IceboxPhoto;
import com.thlh.baselib.utils.GlideRoundTransform;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

import java.util.List;

/**
 * Created by HQ on 2016/3/30.
 */
public class IceboxPhotoAdapter extends EasyRecyclerViewAdapter {
    private Context context;
//    private OnClickListener listener;
//    private boolean isShowDelete = false;
    private List<Boolean> checkStates ;

    public IceboxPhotoAdapter(Context context,List<Boolean> checkStates) {
        this.context = context;
        this.checkStates = checkStates;
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
        ImageView deleteIv = viewHolder.findViewById(R.id.item_icebox_img_delete_iv);

        if (position < mList.size()){
            IceboxPhoto goodspath = (IceboxPhoto)this.getItem(position);
            
            ImageLoader.display(goodspath.getPhoto(),imgsIv);

//            deleteIv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(listener != null){
//                        listener.onClickDelete(position);
//                    }
//                }
//            });
            
            if(checkStates.get(position)){
                deleteIv.setVisibility(View.VISIBLE);
            }else {
                deleteIv.setVisibility(View.INVISIBLE);
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

//    public void setDeleteListener(OnClickListener listener) {
//        this.listener = listener;
//    }


//
//    public interface OnClickListener {
//        void onClickDelete(int position);
//    }

//    public void setCancelVisible(boolean isShowDelete) {
//        this.isShowDelete =  isShowDelete;
//    }
//
//    public boolean isShowDelete() {
//        return isShowDelete;
//    }


}