package com.thlh.jhmjmw.business.ablum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.thlh.baselib.config.Constants;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.other.AlbumBucket;
import com.thlh.jhmjmw.other.AlbumHelper;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 自定义相册界面
 */
public class AlbumTopActivity extends BaseViewActivity {
    public final String TAG = getClass().getSimpleName();
    @BindView(R.id.album_header)
    HeaderNormal albumTopHeader;
    @BindView(R.id.album_rv)
    EasyRecyclerView albumTopRv;

    private List<AlbumBucket> albumbucketList;
    private AlbumBucketAdapter albumbucketAdapter;// 自定义的适配器
    private AlbumHelper albumhelper;

    private int select_type = 0;
    public int REQUEST_CODE_ALBUMGRID = 0;
    private int selectNum = 0;

    public static void activityStart(Activity context, int code, int select_type) {
        Intent intent = new Intent();
        intent.setClass(context, AlbumTopActivity.class);
        intent.putExtra("select_type",select_type);
        context.startActivityForResult(intent,code);
    }

    public static void activityStart(Activity context, int code, int select_type,int selectNum) {
        Intent intent = new Intent();
        intent.setClass(context, AlbumTopActivity.class);
        intent.putExtra("select_type",select_type);
        intent.putExtra("selectNum",selectNum);
        context.startActivityForResult(intent,code);
    }

    @Override
    protected void initVariables() {
        select_type = getIntent().getIntExtra("select_type", Constants.ALBUM_SELECT_SINGLE);
        selectNum = getIntent().getIntExtra("selectNum", 0);
        albumhelper = AlbumHelper.getHelper();
        albumhelper.init(getApplicationContext());
        albumbucketList = albumhelper.getImagesBucketList(false);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);
        albumbucketAdapter = new AlbumBucketAdapter(AlbumTopActivity.this);
        albumbucketAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                if (selectNum <= 0){
                    Intent intent = new Intent(AlbumTopActivity.this, AlbumActivity.class);
                    intent.putParcelableArrayListExtra("albumbucket", albumbucketList.get(position).getAlbumItems());
                    intent.putExtra("select_type",select_type);
                    startActivityForResult(intent, REQUEST_CODE_ALBUMGRID);
                }else {
                    Intent intent = new Intent(AlbumTopActivity.this, AlbumActivity.class);
                    intent.putParcelableArrayListExtra("albumbucket", albumbucketList.get(position).getAlbumItems());
                    intent.putExtra("select_type",select_type);
                    intent.putExtra("selectNum",selectNum);
                    startActivityForResult(intent, REQUEST_CODE_ALBUMGRID);
                }

            }
        });
        albumbucketAdapter.setList(albumbucketList);
        albumTopRv.setLayoutManager(new GridLayoutManager(this,3));
        albumTopRv.setAdapter(albumbucketAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.i(TAG, "REQUEST_CODE_ALBUMGRID :" + REQUEST_CODE_ALBUMGRID + "requestCode: " + requestCode);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_ALBUMGRID) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

}
