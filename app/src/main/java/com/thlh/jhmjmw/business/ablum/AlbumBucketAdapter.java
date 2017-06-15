package com.thlh.jhmjmw.business.ablum;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.AlbumBucket;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

/**
 * 相册页适配器
 */
public class AlbumBucketAdapter extends EasyRecyclerViewAdapter {
    private Context context;

    public AlbumBucketAdapter(Context context){
        this.context = context;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_album_bucket
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {

        ImageView albumBucketIv = (ImageView) viewHolder.findViewById(R.id.album_bucket_iv);
        ImageView albumSelectIv = (ImageView) viewHolder.findViewById(R.id.album_bucket_select_iv);
        TextView bucketNameTv = (TextView) viewHolder.findViewById(R.id.album_bucket_name);
        TextView bucketCountTv = (TextView) viewHolder.findViewById(R.id.album_bucket_count);

        ViewGroup.LayoutParams layoutParams = albumBucketIv.getLayoutParams();
        layoutParams.width = (BaseApplication.width /3  -50);
        layoutParams.height =(BaseApplication.width /3  -100);

        AlbumBucket albumBucket = getItem(position);
        bucketNameTv.setText(albumBucket.getAlbumName());
        bucketCountTv.setText(albumBucket.getCount() + "");

//        Glide.with(context)
//                .load(albumBucket.getAlbumItems().get(0).getImagePath())
//                .into(albumBucketIv);
        ImageLoader.display( albumBucket.getAlbumItems().get(0).getImagePath(),albumBucketIv,true);

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}