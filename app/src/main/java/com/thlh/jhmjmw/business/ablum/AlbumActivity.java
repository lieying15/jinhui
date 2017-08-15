package com.thlh.jhmjmw.business.ablum;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.thlh.baselib.config.Constants;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.other.BaseViewActivity;
import com.thlh.jhmjmw.other.AlbumItem;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 自定义相册界面
 */
public class AlbumActivity extends BaseViewActivity {

    @BindView(R.id.album_header)
    HeaderNormal albumHeader;
    @BindView(R.id.album_rv)
    EasyRecyclerView albumRv;

    private ArrayList<AlbumItem> albumitems;
    private AlbumImageAdapter abumImageAdapter;

    private int select_type;
    private int selectNum = 0;

    @Override
    protected void initVariables() {
        albumitems = getIntent().getParcelableArrayListExtra("albumbucket");
        select_type = getIntent().getIntExtra("select_type", Constants.ALBUM_SELECT_SINGLE);
        selectNum = getIntent().getIntExtra("selectNum", 0);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_album);
        ButterKnife.bind(this);

        abumImageAdapter = new AlbumImageAdapter(AlbumActivity.this,select_type,selectNum);
        abumImageAdapter.setOnClickEvent(new AlbumImageAdapter.OnClickEvent() {
            @Override
            public void onSelect(int position) {
                abumImageAdapter.notifyDataSetChanged();
                L.e("hqt  setOnItemClickListener select_type" +select_type);
                if(select_type == Constants.ALBUM_SELECT_SINGLE){
                    complete();
                }
            }
        });
        abumImageAdapter.setList(albumitems);
        albumRv.setLayoutManager(new GridLayoutManager(this, 3));
        albumRv.setAdapter(abumImageAdapter);

        if(select_type == Constants.ALBUM_SELECT_MUTIPLE){
            albumHeader.setRightText((String) getResources().getText(R.string.nickname_finish));
            albumHeader.setRightListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    complete();
                }
            });
        }


    }

    private void complete(){
        L.e("hqt  complete");
        Collection<String> path = abumImageAdapter.getPathmap().values();
        Iterator<String> pathit = path.iterator();
        ArrayList<String> pathlist = new ArrayList<String>();
        for (; pathit.hasNext(); ) {
            pathlist.add(pathit.next());
        }
        SPUtils.saveStringList("photo_path",pathlist);

        Collection<String> url = abumImageAdapter.getUrlmap().values();
        Iterator<String> urlit = url.iterator();
        ArrayList<String> urllist = new ArrayList<String>();
        for (; urlit.hasNext(); ) {
            urllist.add(urlit.next());
        }
        SPUtils.saveStringList("photo_url",urllist);

//        EventBusUtils.post(new FirstEvent("FirstEvent"));
        setResult(Activity.RESULT_OK);
        finish();
    }

}
