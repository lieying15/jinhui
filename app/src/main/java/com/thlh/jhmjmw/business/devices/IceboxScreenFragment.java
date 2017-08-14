package com.thlh.jhmjmw.business.devices;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thlh.baselib.base.BaseFragment;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.IceboxPhoto;
import com.thlh.baselib.model.response.AlbumResponse;
import com.thlh.baselib.utils.DisplayUtil;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.SystemUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.ablum.AlbumTopActivity;
import com.thlh.jhmjmw.business.other.PhotoPagerActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.PtorFooterLayout;
import com.thlh.jhmjmw.view.PtorHeaderLayout;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.easyrecyclerview.widget.decorator.EasyDividerItemDecoration;
import com.thlh.viewlib.pulltorefresh.PullToRefreshBase;
import com.thlh.viewlib.pulltorefresh.PullToRefreshRecyclerView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class IceboxScreenFragment extends BaseFragment {
    private static final String TAG = "IceboxScreenFragment";
    private final int ACTIVITY_CODE_ALBUM = 0;
    private String PATH_CACHE_IMAGE = SystemUtils.getDiskCacheDir() + "icebox.png";
    private final int SCREEN_SAVER_TYPE_CLOSE = 0;
    private final int SCREEN_SAVER_TYPE_PHOTO = 1;
    private final int SCREEN_SAVER_TYPE_CLOCK = 2;

    private static final int REQUECT_CODE_SDCARD = 2;
    private static final int REQUECT_CODE_CALL_PHONE = 3;

    @BindView(R.id.icebox_screen_ptpr_rv)
    PullToRefreshRecyclerView screenPtprRv;
    @BindView(R.id.icebox_screen_action_clock_iv)
    ImageView iceboxScreenActionClockIv;
    @BindView(R.id.icebox_screen_action_clock_tv)
    TextView iceboxScreenActionClockTv;
    @BindView(R.id.icebox_screen_action_paly_iv)
    ImageView iceboxScreenActionPalyIv;
    @BindView(R.id.icebox_screen_action_paly_tv)
    TextView iceboxScreenActionPalyTv;
    @BindView(R.id.icebox_screen_action_addimg_iv)
    ImageView iceboxScreenActionAddimgIv;
    @BindView(R.id.icebox_screen_action_addimg_tv)
    TextView iceboxScreenActionAddimgTv;
    @BindView(R.id.icebox_screen_action_clock_ll)
    LinearLayout iceboxScreenActionClockLl;
    @BindView(R.id.icebox_screen_action_paly_ll)
    LinearLayout iceboxScreenActionPalyLl;
    @BindView(R.id.icebox_screen_action_addimg_ll)
    LinearLayout iceboxScreenActionAddimgLl;
    @BindView(R.id.icebox_screen_bg_v)
    View screenBgView;


    private int current_page = 1;
    private int total_page = 1;
    private boolean isLoadingMore = false;
    private int cotent_type;
    private int screen_saver;

    private EasyRecyclerView screenimgRv;
    private GridLayoutManager mLayoutManager;

    private IceboxPhotoAdapter photoAdapter;
    private BaseObserver<BaseResponse> deleteObserver, addObserver, saveObserver;
    private BaseObserver<AlbumResponse> indexObserver;
    private List<IceboxPhoto> photoList = new ArrayList<>();
    private ArrayList<String> photoStrList = new ArrayList<>();
    public List<Uri> picsUrl = new ArrayList<>();
    private List<Boolean> deleteStates = new ArrayList<>() ;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_icebox_screen;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initView() {

//        EventBusUtils.register(this);
        screenPtprRv.setHasPullUpFriction(false); // 设置没有上拉阻力
        screenimgRv = screenPtprRv.getRefreshableView();
        photoAdapter = new IceboxPhotoAdapter(getActivity());


        photoAdapter.setOnItemLongClickListener(new EasyRecyclerViewHolder.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View convertView, int position) {
                photoAdapter.setCancelVisible(true);
                photoAdapter.notifyDataSetChanged();
                return false;
            }
        });
        photoAdapter.setCancelListener(new IceboxPhotoAdapter.OnClickListener() {
            @Override
            public void onClickCancel(int position) {
                String photoid = ((IceboxPhoto)photoAdapter.getItem(position)).getId();
                postDeletaPhoto(photoid);
                photoAdapter.setCancelVisible(false);
                photoAdapter.notifyDataSetChanged();
            }
        });

        EasyDividerItemDecoration dataDecoration = new EasyDividerItemDecoration(
                getActivity(),
                EasyDividerItemDecoration.VERTICAL_LIST,
                R.drawable.divider_wide_mainback
        );
        screenimgRv.addItemDecoration(dataDecoration);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        screenimgRv.setLayoutManager(mLayoutManager);
        int marging = DisplayUtil.dp2px(getActivity(),10);
        screenimgRv.setPadding(marging, marging, 10, 15);
        screenimgRv.setAdapter(photoAdapter);

        screenPtprRv.setMode(PullToRefreshBase.Mode.BOTH);
        screenPtprRv.setHeaderLayout(new PtorHeaderLayout(getActivity()));
        screenPtprRv.setFooterLayout(new PtorFooterLayout(getActivity()));
        screenPtprRv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<EasyRecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                current_page = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<EasyRecyclerView> refreshView) {
                screenPtprRv.onRefreshComplete();
            }
        });

        screenimgRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoadingMore) return;
                if (current_page >= total_page) return;
                int lastVisibleItem = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载， dy>0 表示向下滑动
                if (totalItemCount >= Constants.IceBoxScreenPageCount && lastVisibleItem >= totalItemCount - 3 && dy > 0) {
                    isLoadingMore = true;
                    current_page++;
                    L.e("totalItemCount:" + totalItemCount + "  lastVisibleItem" + lastVisibleItem + " dy" + dy);
                    L.e("加载更多数据 current_page" + current_page);
                    loadData();//这里多线程也要手动控制isLoadingMore
                }
            }
        });
    }

    @Override
    protected void initData() {
        indexObserver = new BaseObserver<AlbumResponse>() {
            @Override
            public void onErrorResponse(AlbumResponse albumResponse) {
                screenPtprRv.onRefreshComplete();
                isLoadingMore = false;
                showErrorDialog(albumResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(AlbumResponse albumResponse) {
                screenPtprRv.onRefreshComplete();
                total_page = albumResponse.getData().getTotal_page();
                screen_saver = albumResponse.getData().getScreen_saver();
                adjustTabView();
                photoList = albumResponse.getData().getPhotos();
                L.e("加载数据 photoList size:" + photoList.size());


                if (isLoadingMore) {
                    photoAdapter.setList(photoList, true);
                    isLoadingMore = false;
                    for (int i = 0; i < photoList.size(); i++) {
                        photoStrList.add(photoList.get(i).getPhoto());
                        deleteStates.add(false);
                    }
                } else {
                    photoAdapter.setList(photoList);
                    photoStrList.clear();
                    for (int i = 0; i < photoList.size(); i++) {
                        photoStrList.add(photoList.get(i).getPhoto());
                        deleteStates.add(false);
                    }
                }

                photoAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
                    @Override
                    public void onItemClick(View convertView, int position) {
                        if (deleteStates.get(position)) {
//                            photoAdapter.setCancelVisible(false);
//                            photoAdapter.notifyDataSetChanged();
                            String photoid = ((IceboxPhoto) photoAdapter.getItem(position)).getId();
                            postDeletaPhoto(photoid);
                            deleteStates.set(position,false);
//                            photoAdapter.setCancelVisible(false);
                            photoAdapter.notifyDataSetChanged();
                        } else {
                            if(!isDeleteStatus()){
                                PhotoPagerActivity.activityStart(getActivity(), photoStrList, position);
                            }

                        }
                    }
                });

                int imgheight = (int) ((photoStrList.size()/3 +1)* getResources().getDimension(R.dimen.y240));
                ViewGroup.LayoutParams  layoutParams = screenBgView.getLayoutParams();
                layoutParams.height = imgheight;
            }
        };

        deleteObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                loadData();
            }
        };

        addObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                ((IceboxActivity) getActivity()).getProgressMaterial().dismiss();
                showErrorDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                ((IceboxActivity) getActivity()).getProgressMaterial().dismiss();
                current_page = 1;
                loadData();
            }
        };

        saveObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }

            @Override
            public void onNextResponse(BaseResponse baseResponse) {
            }
        };
    }


    @Override
    protected void loadData() {
        NetworkManager.getAlbumApi()
                .getIndex(SPUtils.getToken(), current_page, Constants.IceBoxScreenPageCount)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(indexObserver);
    }


    public void postDeletaPhoto(String photoid) {
        NetworkManager.getAlbumApi()
                .deletePhoto(SPUtils.getToken(), photoid)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(deleteObserver);
    }

    public void postSetScreenSave() {
        NetworkManager.getAlbumApi()
                .saveScreenParam(SPUtils.getToken(), screen_saver + "")
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(saveObserver);
    }

    public void postAddPhoto() {
        ((IceboxActivity) getActivity()).getProgressMaterial().show();
        Map<String, RequestBody> photos = new HashMap<>();
        if (picsUrl.size() == 1) {
            String path = PATH_CACHE_IMAGE;
            File file = new File(path);
            RequestBody photo = RequestBody.create(MediaType.parse("image/png"), file);
            photos.put("photo[]\"; filename=\"pic.png", photo);
        } else {
            for (int i = 0; i < picsUrl.size(); i++) {
                String path = picsUrl.get(i).toString();
                File file = new File(path);
                RequestBody photo = RequestBody.create(MediaType.parse("image/png"), file);
                photos.put("photo[]\"; filename=\"pic" + i + ".png", photo);
            }
        }
        NetworkManager.getAlbumApi()
                .addPhoto(SPUtils.getToken(), photos)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(addObserver);
    }

    public void adjustTabView() {
        switch (screen_saver) {
            case SCREEN_SAVER_TYPE_CLOSE:
                iceboxScreenActionClockIv.setBackgroundResource(R.drawable.icon_icebox_clock);
                iceboxScreenActionPalyIv.setBackgroundResource(R.drawable.icon_icebox_screen);
                iceboxScreenActionClockTv.setTextColor(getResources().getColor(R.color.text_nomal));
                iceboxScreenActionPalyTv.setTextColor(getResources().getColor(R.color.text_nomal));
                break;
            case SCREEN_SAVER_TYPE_PHOTO:
                iceboxScreenActionClockIv.setBackgroundResource(R.drawable.icon_icebox_clock);
                iceboxScreenActionPalyIv.setBackgroundResource(R.drawable.icon_icebox_screen_select);
                iceboxScreenActionClockTv.setTextColor(getResources().getColor(R.color.text_nomal));
                iceboxScreenActionPalyTv.setTextColor(getResources().getColor(R.color.wine_light));
                break;
            case SCREEN_SAVER_TYPE_CLOCK:
                iceboxScreenActionClockIv.setBackgroundResource(R.drawable.icon_icebox_clock_select);
                iceboxScreenActionPalyIv.setBackgroundResource(R.drawable.icon_icebox_screen);
                iceboxScreenActionClockTv.setTextColor(getResources().getColor(R.color.wine_light));
                iceboxScreenActionPalyTv.setTextColor(getResources().getColor(R.color.text_nomal));
                break;
        }
    }

    @OnClick({R.id.icebox_screen_action_clock_ll, R.id.icebox_screen_action_paly_ll, R.id.icebox_screen_action_addimg_ll,
            R.id.icebox_screen_bg_v})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icebox_screen_action_clock_ll:
                if (screen_saver == SCREEN_SAVER_TYPE_CLOCK) {
                    screen_saver = SCREEN_SAVER_TYPE_CLOSE;
                } else {
                    screen_saver = SCREEN_SAVER_TYPE_CLOCK;
                }
                adjustTabView();
                postSetScreenSave();
                break;
            case R.id.icebox_screen_action_paly_ll:
                if (screen_saver == SCREEN_SAVER_TYPE_PHOTO) {
                    screen_saver = SCREEN_SAVER_TYPE_CLOSE;
                } else {
                    screen_saver = SCREEN_SAVER_TYPE_PHOTO;
                }
                adjustTabView();
                postSetScreenSave();
                break;
            case R.id.icebox_screen_action_addimg_ll:
                startAlbum();
                break;
            case R.id.icebox_screen_bg_v :
                isDeleteStatus();
                break;
        }
    }


    private void startAlbum() {
        Intent intent = new Intent(getActivity(), AlbumTopActivity.class);
        intent.putExtra("select_type", Constants.ALBUM_SELECT_MUTIPLE);
        startActivityForResult(intent, ACTIVITY_CODE_ALBUM);
    }

    public void startPhotoZoom() {
        ArrayList<String> pathlist = SPUtils.getStringList("photo_path");
        ArrayList<String> urllist = SPUtils.getStringList("photo_url");
        if (urllist.size() == 1) {
            L.e(TAG + " 单图片上传 开始剪裁");
            Uri cameraURI = Uri.fromFile(new File(PATH_CACHE_IMAGE));
            UCrop uCrop = UCrop.of(Uri.parse(urllist.get(0)), cameraURI);
            UCrop.Options options = new UCrop.Options();
            options.setFreeStyleCropEnabled(true);
            uCrop.withMaxResultSize(1920, 1920);
            uCrop.withOptions(options);
            uCrop.start(getActivity());
        } else {
            picsUrl.clear();
            for (int i = 0; i < pathlist.size(); i++) {
                picsUrl.add(Uri.parse(pathlist.get(i)));
            }
            postAddPhoto();
        }
    }

    //判断是否为选中状态
    private boolean isDeleteStatus(){
        boolean isDeleteStatus = false;
        for (int i = 0; i < deleteStates.size(); i++) {
            if(deleteStates.get(i)){
                isDeleteStatus = true;
                deleteStates.set(i,false);
            }
        }
        photoAdapter.notifyDataSetChanged();
        return  isDeleteStatus;
    }

//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1)
//    public void onMessageEventMain(FirstEvent event) {
//        if (event.getMsg().equals("FirstEvent")){
//            startPhotoZoom();
//        }
//        Log.v(TAG, event.getMsg() + " MAIN id = " + Thread.currentThread().getId());
//
//    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            L.i(TAG, "onActivityResult-->RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_ALBUM:
                    startPhotoZoom();
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            L.e(TAG, "OnActivityResult-->" + resultCode + "用户取消了操作");
        } else {
            // 获取结果失败
            L.e(TAG, "OnActivityResult-->" + resultCode);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        EventBusUtils.unregister(this);//解除订阅

    }
}