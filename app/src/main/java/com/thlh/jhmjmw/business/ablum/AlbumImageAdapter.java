package com.thlh.jhmjmw.business.ablum;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.thlh.baselib.base.BaseApplication;
import com.thlh.baselib.config.Constants;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.other.AlbumItem;
import com.thlh.viewlib.easyrecyclerview.adapter.EasyRecyclerViewAdapter;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * 相册页适配器
 */
public class AlbumImageAdapter extends EasyRecyclerViewAdapter {
    private Context context;
    private OnClickEvent listener;

    private Map<String, String> urlmap = new HashMap<String, String>();
    private Map<String, String> pathmap = new HashMap<String, String>();

    private int select_type;
    public AlbumImageAdapter(Context context,int select_type){
        this.context = context;
        this.select_type = select_type;
    }


    @Override
    public int[] getItemLayouts() {
        return new int[]{
                R.layout.item_album_image
        };
    }

    @Override
    public void onBindRecycleViewHolder(EasyRecyclerViewHolder viewHolder,final int position) {
        final AlbumItem albumItem = getItem(position);
        ImageView albumImageIv = (ImageView) viewHolder.findViewById(R.id.album_image_iv);
        final ImageView albumSelectIv = (ImageView) viewHolder.findViewById(R.id.album_image_select_iv);
        final ImageView albumBackIv = (ImageView) viewHolder.findViewById(R.id.album_image_back_iv);

        ViewGroup.LayoutParams layoutParams = albumImageIv.getLayoutParams();
        layoutParams.width = (BaseApplication.width /3  -50);
        layoutParams.height =(BaseApplication.width /3  -100);

        ImageLoader.display( albumItem.getImagePath(),albumImageIv,true);
        if (albumItem.isSelected()) {
            albumSelectIv.setVisibility(View.VISIBLE);
            albumBackIv.setVisibility(View.VISIBLE);
        } else {
            albumSelectIv.setVisibility(View.INVISIBLE);
            albumBackIv.setVisibility(View.INVISIBLE);
        }

        albumImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = albumItem.getImagePath();
                String url = albumItem.getImageId();
                if(select_type == Constants.ALBUM_SELECT_SINGLE ){
                    switch (pathmap.size()){
                        case 0 :
                            if (!albumItem.isSelected()) {
                                albumItem.setSelected(true);
                                pathmap.put(path, path);
                                urlmap.put(url, Constants.LOCAL_IMAGE_PATH + url);
                            }
                            break;
                        case 1 :
                            if (albumItem.isSelected()) {
                                albumItem.setSelected(false);
                                pathmap.remove(path);
                                urlmap.remove(url);
                            }
                            break;
                    }
                }else {
                    albumItem.setSelected(!albumItem.isSelected());
                    if (albumItem.isSelected()) {
                        albumSelectIv.setVisibility(View.VISIBLE);
                        albumBackIv.setVisibility(View.VISIBLE);
                        pathmap.put(path, path);
                        urlmap.put(url, Constants.LOCAL_IMAGE_PATH + url);
                    } else if (!albumItem.isSelected()) {
                        albumSelectIv.setVisibility(View.INVISIBLE);
                        albumBackIv.setVisibility(View.INVISIBLE);
                        pathmap.remove(path);
                        urlmap.remove(url);
                    }
                }
                if(AlbumImageAdapter.this.listener != null){
                    AlbumImageAdapter.this.listener.onSelect(position);
                }
            }
        });


    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public Map<String, String> getPathmap() {
        return pathmap;
    }

    public Map<String, String> getUrlmap() {
        return urlmap;
    }

    public void setOnClickEvent(OnClickEvent listener) {
        this.listener = listener;
    }



    public interface OnClickEvent {
        void onSelect(int position);
    }
}