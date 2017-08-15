package com.thlh.jhmjmw.business.order.comment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.GoodsComment;
import com.thlh.baselib.model.response.CommentSaveResponse;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.SystemUtils;
import com.thlh.baselib.view.NormalDialogFragment;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.ablum.AlbumTopActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.Deployment;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.BaseImgDialog;
import com.thlh.jhmjmw.view.HeaderNormal;
import com.thlh.jhmjmw.view.PoiRedStar;
import com.thlh.viewlib.easyrecyclerview.holder.EasyRecyclerViewHolder;
import com.thlh.viewlib.easyrecyclerview.widget.EasyRecyclerView;
import com.thlh.viewlib.recyclerview.EasyGridItemDecoration;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 写评价页
 */
public class OrderCommentWriteActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = "OrderCommentWriteActivity";
    private final int ACTIVITY_CODE_ALBUM = 0;
    private final String WRITE_COMMENT_ADD = "1";
    private final String WRITE_COMMENT_SAVE = "0";

    @BindView(R.id.comment_num_tv)
    TextView commentWriteNumTv;
    @BindView(R.id.comment_write_header)
    HeaderNormal commentWriteHeader;
    @BindView(R.id.comment_write_goods_iv)
    ImageView commentWriteGoodsIv;
    @BindView(R.id.comment_write_goods_name_tv)
    TextView commentWriteGoodsNameTv;
    @BindView(R.id.comment_price_tv)
    TextView commentPriceTv;
    @BindView(R.id.comment_rank_view)
    PoiRedStar commentRankView;
    @BindView(R.id.comment_write_img_rv)
    EasyRecyclerView commentWriteImgRv;
    @BindView(R.id.comment_write_img_fl)
    FrameLayout commentWriteImgFl;

    @BindView(R.id.comment_write_et)
    EditText commentWriteEt;
    @BindView(R.id.comment_write_hidename_cb)
    CheckBox commentWriteHidenameCb;
    @BindView(R.id.comment_write_submit_tv)
    TextView commentWriteSubmitTv;
    @BindView(R.id.comment_write_pics_tv)
    TextView commentWritePicsTv;


    private GoodsComment goodsComment;
    private OrderCommentWritePicsAdapter goodsPicsAdapter ;
    private List<Uri > picsUrl = new ArrayList<>();
    private EasyGridItemDecoration dataDecoration;

    private Uri cameraURI, imageUri;//上传头像用
    private int add_positon;
    private  String PATH_CACHE_IMAGE = SystemUtils.getDiskCacheDir() + "storeimage.png";
    private BaseObserver<BaseResponse> addObserver;
    private BaseObserver<CommentSaveResponse> saveObserver;

    private String orderid;
    private String itemid;
    private int rate;
    private String comment;
    private String isshowname;
    private int  rank;
    private String commentFlag;
    private String url;
    private BaseImgDialog.Builder builder;


    public static void activityStart(Activity context,GoodsComment goodsComment,int code) {
        Intent intent = new Intent();
        intent.setClass(context, OrderCommentWriteActivity.class);
        intent.putExtra("goodscomment",goodsComment);
        context.startActivityForResult(intent,code);
    }

    @Override
    protected void initVariables() {
        goodsComment = getIntent().getParcelableExtra("goodscomment");
    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_comment_write);
        ButterKnife.bind(this);
        builder = new BaseImgDialog.Builder(this);
        commentWriteHeader.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSaveDialog();
            }
        });

        if (goodsComment.getItem_img_thumb().contains("http")){
            url = goodsComment.getItem_img_thumb();
        }else {
            url = Deployment.IMAGE_PATH + goodsComment.getItem_img_thumb();
        }
        ImageLoader.displayRoundImg(url,commentWriteGoodsIv);
        commentWriteGoodsNameTv.setText(goodsComment.getItem_name());
        commentWriteNumTv.setText(getResources().getString(R.string.multiplys)+goodsComment.getItem_num());
        commentPriceTv.setText(getResources().getString(R.string.money)+ goodsComment.getPrice()  );
        commentRankView.setStar(5);
        commentRankView.setCanSelect(true);


        goodsPicsAdapter = new OrderCommentWritePicsAdapter(this);
        goodsPicsAdapter.setOnItemClickListener(new EasyRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                    startAlbum();
                    add_positon = position;
            }
        });

        GridLayoutManager inScrollLM = new GridLayoutManager(this, 5);
        commentWriteImgRv.setLayoutManager(inScrollLM);
        commentWriteImgRv.setAdapter(goodsPicsAdapter);
        dataDecoration = new EasyGridItemDecoration(this,R.drawable.divider_medium_mainback);
        dataDecoration.bottomDivider = false;
        commentWriteImgRv.addItemDecoration(dataDecoration);

        addObserver  = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                switch (commentFlag){
                    case WRITE_COMMENT_ADD :
                        SPUtils.put("order_need_update","4");//0无变化，1付款，2确认收货，3取消订单，4评论
                        showWriteSucecssDialog();
                        break;
                    case WRITE_COMMENT_SAVE:
                        finish();
                        break;
                }
            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }
        };

        saveObserver  = new BaseObserver<CommentSaveResponse>() {
            @Override
            public void onNextResponse(CommentSaveResponse saveResponse) {
                if(saveResponse ==null )return;
                String rate = saveResponse.getData().getComment().getRate();
                String comment = saveResponse.getData().getComment().getComment();
                String showname = saveResponse.getData().getComment().getShow_name();
                L.e(TAG + "读取保存信息 rate" +rate+"  comment " +comment+"  Show_name " +showname );
                commentRankView.setStar(Integer.parseInt(rate));
                commentWriteEt.setText(comment);

                if(showname.equals("1")){
                    commentWriteHidenameCb.setChecked(true);
                }else {
                    commentWriteHidenameCb.setChecked(false);
                }
                List<String > pics = saveResponse.getData().getComment().getPic();
                if(pics == null ||pics.size()==0){
                    return;
                }else{
                    commentWriteImgFl.setVisibility(View.VISIBLE);
                    for (int i = 0; i <pics.size() ; i++) {
                        L.e(TAG + "读取保存信息 pic " +pics.get(i) );
                        //读取到的图片先存到本地
                        downloadPic(pics.get(i),i);
                    }
                }

            }

            @Override
            public void onErrorResponse(CommentSaveResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }
        };
    }

    @Override
    protected void loadData() {
        L.e(TAG + " Order_id " +  goodsComment.getOrder_id() + "  Item_id " + goodsComment.getItem_id());
        NetworkManager.getItemApi()    //获取保存的数据
                .getSaveInfo(SPUtils.getToken(),goodsComment.getOrder_id(),goodsComment.getItem_id())
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(saveObserver);
    }



    @OnClick({ R.id.comment_write_pics_tv,R.id.comment_write_submit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.comment_write_pics_tv:

                if(picsUrl==null ||picsUrl.size()==0){
                    add_positon = 0;
                }
                startAlbum();
                break;

            case R.id.comment_write_submit_tv:
                postAddComment(WRITE_COMMENT_ADD);
                break;

        }
    }

    private void startAlbum() {
        AlbumTopActivity.activityStart(OrderCommentWriteActivity.this,ACTIVITY_CODE_ALBUM, Constants.ALBUM_SELECT_MUTIPLE,5);
    }

//    public void startPhotoZoom(Uri uri) {
//        PATH_CACHE_IMAGE = SystemUtils.getDiskCacheDir() + "commentpic"+add_positon+".png";
//        L.e("startPhotoZoom uri" + uri.toString() + " PATH"+PATH_CACHE_IMAGE);
//        Uri cameraURI = Uri.fromFile(new File(PATH_CACHE_IMAGE));
//        UCrop uCrop = UCrop.of(uri, cameraURI);
//        UCrop.Options options = new UCrop.Options();
//        options.setFreeStyleCropEnabled(true);
//        uCrop.withMaxResultSize(640, 640);
//        uCrop.withOptions(options);
//        uCrop.start(OrderCommentWriteActivity.this);
//    }

    public void startPhotoZoom() {
        ArrayList<String> pathlist = SPUtils.getStringList("photo_path");
        ArrayList<String> urllist = SPUtils.getStringList("photo_url");
        L.e("pathlist====" + pathlist.size());
        L.e("urllist====" + urllist.size());
        if (urllist.size() == 1) {
            L.e(TAG + " 单图片上传 开始剪裁");
            Uri cameraURI = Uri.fromFile(new File(PATH_CACHE_IMAGE));
            UCrop uCrop = UCrop.of(Uri.parse(urllist.get(0)), cameraURI);
            UCrop.Options options = new UCrop.Options();
            options.setFreeStyleCropEnabled(true);
            uCrop.withMaxResultSize(1920, 1920);
            uCrop.withOptions(options);
            uCrop.start(this);
        } else {
            picsUrl.clear();
            int num = pathlist.size() > 5 ? 5 : pathlist.size();
            for (int i = 0; i < num; i++) {
                picsUrl.add(Uri.parse(pathlist.get(i)));
            }
            commentWriteImgFl.setVisibility(View.VISIBLE);
            goodsPicsAdapter.setList(picsUrl);
            goodsPicsAdapter.notifyDataSetChanged();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            L.i(TAG, "onActivityResult-->RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_ALBUM:
                    // 图库获取,取得图片的本地Uri
//                    Uri originalUri = data.getData();
//                    startPhotoZoom(originalUri);
                    startPhotoZoom();
                    break;
                case UCrop.REQUEST_CROP:
                    // 头像剪切后的返回结果
                    handleCropResult(data);
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            L.e(TAG, "OnActivityResult-->" + resultCode + "用户取消了操作");
        } else {
            // 获取结果失败
            L.e(TAG, "OnActivityResult-->" + resultCode);
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        if(UCrop.getOutput(result) ==null) return;
        imageUri = null;
        imageUri = UCrop.getOutput(result);
        if (imageUri != null) {
            if(add_positon >= picsUrl.size()) {
                picsUrl.add(imageUri);
            }else {
                picsUrl.set(add_positon,imageUri);
            }
            commentWriteImgFl.setVisibility(View.VISIBLE);
            goodsPicsAdapter.setList(picsUrl);
            goodsPicsAdapter.notifyDataSetChanged();
        }
    }

    private boolean judgeCommentInfo(){
        if (commentRankView.getStar() == -1) {
            showWaringDialog(getResources().getString(R.string.please_evaluation));
            return false;
        }

        if(commentWriteEt.getText().toString().trim().equals("")){
            showWaringDialog(getResources().getString(R.string.evaluation_content));
            return false;
        }

        return  true;
    }

    private void postAddComment(String flag) {
        if(judgeCommentInfo()){
            progressMaterial.show();
            orderid = goodsComment.getOrder_id();
            itemid = goodsComment.getItem_id();
            rate = commentRankView.getStar();
            comment =commentWriteEt.getText().toString().trim();
            if(commentWriteHidenameCb.isChecked()){
                isshowname = "1";
            }else {
                isshowname = "0";
            }

            Map<String,RequestBody> photos = new HashMap<>();
            for (int i = 0; i <picsUrl.size() ; i++) {
//                String path = SystemUtils.getDiskCacheDir() + "commentpic" + i +".png";
                String path = picsUrl.get(i).toString();
                File file = new File(path);
                RequestBody photo = RequestBody.create(MediaType.parse("image/png"), file);
                photos.put("pic[]\"; filename=\"pic"+ i +".png", photo);
            }

            Map<String,RequestBody> param = new HashMap<>();
            RequestBody temporderid = RequestBody.create(MediaType.parse("text/*"), orderid);
            param.put("order_id",temporderid);
            RequestBody tempitemid = RequestBody.create(MediaType.parse("text/*"), itemid);
            param.put("item_id",tempitemid);
            RequestBody temprate = RequestBody.create(MediaType.parse("text/*"), rate + "");
            param.put("rate",temprate);
            RequestBody tempcomment = RequestBody.create(MediaType.parse("text/*"), comment);
            param.put("comment",tempcomment);
            RequestBody tempshow_name = RequestBody.create(MediaType.parse("text/*"), isshowname);
            param.put("show_name",tempshow_name);
            RequestBody tempflag = RequestBody.create(MediaType.parse("text/*"), flag);
            param.put("flag",tempflag);
            commentFlag = flag;
            Subscription subscription = NetworkManager.getItemApi()
                    .addComment(SPUtils.getToken(),param,photos)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(addObserver);
            subscriptionList.add(subscription);
        }
    }

    private void showWriteSucecssDialog(){
        final NormalDialogFragment writeSucessDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        writeSucessDialog.setTitleIvRes(R.drawable.icon_dialog_success);
        writeSucessDialog.setContentStr(getResources().getString(R.string.evaluation_success));
        writeSucessDialog.setFinalBtnStr(getResources().getString(R.string.shopcart_total_confirm));
        writeSucessDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
        writeSucessDialog.show(ft, "writeSucessDialog");
    }

    private void showSaveDialog(){
        final NormalDialogFragment saveDialog = new NormalDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        saveDialog.setTitleIvRes(R.drawable.img_dialog_file);
        saveDialog.setContentStr(getResources().getString(R.string.no_content)+"\n"+getResources().getString(R.string.edit_save));
        saveDialog.setMiddleBtnVisible(View.VISIBLE);
        saveDialog.setMiddleBtnStr(getResources().getString(R.string.no_save));
        saveDialog.setMiddleBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDialog.dismiss();
                finish();
            }
        });
        saveDialog.setFinalBtnStr(getResources().getString(R.string.save));
        saveDialog.setFinalBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postAddComment(WRITE_COMMENT_SAVE);
                saveDialog.dismiss();
            }
        });
        saveDialog.show(ft, "saveDialog");
    }

    // 回退键弹窗口
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showSaveDialog();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 下载图片
     * @param url
     * @param num
     */
    private void downloadPic( String url, int num ){
        final String path = getExternalFilesDir(null) + File.separator + "saveimg"+ num +".png";
        Subscription subscription = NetworkManager.getDownLoadImgApi()
                .download(Deployment.IMAGE_PATH + url)
                .subscribeOn(Schedulers.newThread())//在新线程中实现该方法
                        .map(new Func1<ResponseBody, Bitmap>() {
                            @Override
                            public Bitmap call(ResponseBody responseBody) {
                                try {
                                    byte[]  bytes  =  getBytesFromStream(responseBody.byteStream());
                                    saveBytesToFile(bytes,path);
                                    L.e(TAG + "图片已保存为Bitmap" );
                                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                                    return bitmap;//返回一个bitmap对象
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())//在Android主线程中展示
                        .subscribe(new Subscriber<Bitmap>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                            }

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable arg0) {
//                                L.d(TAG, "onError ===== " + arg0.toString());
                            }

                            @Override
                            public void onNext(Bitmap arg0) {
                                L.e(TAG + "图片已经下载" );
                                picsUrl.add(Uri.parse(path));
                                L.e(TAG + "图片已经下载 url + Uri.parse(path)" );
                                goodsPicsAdapter.setList(picsUrl);
                            }
                        });
        subscriptionList.add(subscription);
    }

    private byte[] getBytesFromStream(InputStream is) throws IOException {
        int len;
        int size = 1024;
        byte[] buf;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        buf = new byte[size];
        while((len = is.read(buf, 0, size)) != -1) {
            bos.write(buf, 0, len);
        }
        buf = bos.toByteArray();
        return buf;
    }

    public  static  void  saveBytesToFile(byte[]  bytes,  String  path)  {
        FileOutputStream fileOuputStream ;
        try  {
            fileOuputStream =  new  FileOutputStream(path);
            fileOuputStream.write(bytes);
        }  catch(FileNotFoundException e)  {
            e.printStackTrace();
        }  catch(IOException  e)  {
            e.printStackTrace();
        }  finally{
//            fileOuputStream.close();
        }
    }


}
