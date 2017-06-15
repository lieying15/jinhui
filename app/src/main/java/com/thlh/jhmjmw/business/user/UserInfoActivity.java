package com.thlh.jhmjmw.business.user;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thlh.baselib.base.BaseActivity;
import com.thlh.baselib.base.BaseObserver;
import com.thlh.baselib.base.BaseResponse;
import com.thlh.baselib.config.Constants;
import com.thlh.baselib.model.response.AvatarResponse;
import com.thlh.baselib.utils.GlideCircleTransform;
import com.thlh.baselib.utils.RxUtils;
import com.thlh.baselib.utils.SPUtils;
import com.thlh.baselib.utils.SystemUtils;
import com.thlh.jhmjmw.R;
import com.thlh.jhmjmw.business.ablum.AlbumTopActivity;
import com.thlh.jhmjmw.network.NetworkManager;
import com.thlh.jhmjmw.other.ImageLoader;
import com.thlh.jhmjmw.other.L;
import com.thlh.jhmjmw.view.DialogAvatar;
import com.thlh.viewlib.ripple.RippleLinearLayout;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * 个人信息页面
 */
public class UserInfoActivity extends BaseActivity {
    private static final String TAG = "UserInfoActivity";
    /** 上传头像-获取系统相册*/
    private final int ACTIVITY_CODE_ALBUM = 0;
    /** 上传头像-启动系统相机*/
    private final int ACTIVITY_CODE_CAMERA = 1;
    private final int ACTIVITY_CODE_NICKNAME = 2;
    private  final String PATH_CACHE_IMAGE = SystemUtils.getDiskCacheDir() + "cropimage.jpg"; //相册图片缓存路径

    @BindView(R.id.userinfo_bindphone_tv)
    TextView userinfoBindphoneTv;
    @BindView(R.id.userinfo_bindphone_ll)
    RippleLinearLayout userinfoBindphoneLl;
    @BindView(R.id.userinfo_avatar_ll)
    LinearLayout userinfoAvatarLl;
    @BindView(R.id.userinfo_avatar_iv)
    ImageView userinfoAvatarIv;
    @BindView(R.id.userinfo_name_tv)
    TextView userinfoNameTv;
    @BindView(R.id.userinfo_changename_tv)
    TextView changeNameTv;
    @BindView(R.id.userinfo_editname_tv)
    EditText editNameTv;


    private DialogAvatar.Builder dialog ;
    private Uri cameraURI, avatarUri;//上传头像用
    public List<Uri> picsUrl = new ArrayList<>();
    private BaseObserver<AvatarResponse> avatarObserver;
    private BaseObserver<BaseResponse> nameObserver;


    private boolean isEditeName = false;
    private String newNickname;

    public static void activityStart(Activity context) {
        Intent intent = new Intent();
        intent.setClass(context, UserInfoActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.activity_slide_right_in, R.anim.activity_slide_left_out);
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initBaseViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_userinfo);
        ButterKnife.bind(this);
        String bindphone = (String)SPUtils.get("user_bind_mobile","").toString();
        if(!bindphone.equals("")){
            StringBuilder builder = new StringBuilder();
            builder.append(bindphone);
            builder.replace(builder.length() -4 ,builder.length(),"****");
            userinfoBindphoneTv.setText(builder.toString());
        }
        dialog = new  DialogAvatar.Builder(this);
        int logintype = Integer.valueOf(SPUtils.get("login_type",Constants.LOGIN_TYPE_NORMAL).toString());

        if(bindphone.equals("")){
            userinfoBindphoneLl.setLLRippleCompleteListener(new RippleLinearLayout.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleLinearLayout rippleRelativeLayout) {
                    PhoneVerifyCodeActivity.activityStart(UserInfoActivity.this,Constants.PHONECODE_TYPE_BINDPHONE);
                }
            });
        }

        ImageLoader.display( (String)SPUtils.get("user_avatar", "").toString(),userinfoAvatarIv,R.drawable.img_mine_avatar,new GlideCircleTransform(this));

        String nickname = (String)SPUtils.get("user_nickname","").toString();
        userinfoNameTv.setText(nickname);

        userinfoAvatarLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAvatarDialog();
            }
        });


        avatarObserver = new BaseObserver<AvatarResponse>() {
            @Override
            public void onNextResponse(AvatarResponse avatarResponse) {
                Glide.with(UserInfoActivity.this)
                        .load(avatarUri)
                        .transform(new GlideCircleTransform(UserInfoActivity.this))
                        .placeholder(R.drawable.img_mine_avatar)
                        .into(userinfoAvatarIv);
                L.i(TAG + "  getPhoto_url:" + avatarResponse.getData().getPhoto_url());
                SPUtils.put("user_avatar", avatarResponse.getData().getPhoto_url());
                SPUtils.put("needupdate_userinfo", true);

            }

            @Override
            public void onErrorResponse(AvatarResponse avatarResponse) {
                showErrorDialog(avatarResponse.getErr_msg());
            }
        };

        nameObserver = new BaseObserver<BaseResponse>() {
            @Override
            public void onNextResponse(BaseResponse baseResponse) {
                SPUtils.put("user_nickname", newNickname);
                SPUtils.put("needupdate_userinfo", true);
                showSuccessDialog(getResources().getString(R.string.Modify_nickname_success));
                editNameTv.setVisibility(View.GONE);
                userinfoNameTv.setVisibility(View.VISIBLE);
                userinfoNameTv.setText(newNickname);
                changeNameTv.setText(getResources().getString(R.string.Modify));
                isEditeName = false;
            }

            @Override
            public void onErrorResponse(BaseResponse baseResponse) {
                showErrorDialog(baseResponse.getErr_msg());
            }
        };
    }


    @Override
    protected void loadData() {
    }

    @OnClick({R.id.userinfo_changename_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userinfo_changename_tv:
                if(isEditeName){
                    postNickname();
                }else {
                    isEditeName = true;
                    editNameTv.setVisibility(View.VISIBLE);
                    userinfoNameTv.setVisibility(View.GONE);
                    changeNameTv.setText(getResources().getString(R.string.nickname_finish));
                }
                break;

        }
    }
    private void showAvatarDialog(){
        dialog.setAlbumListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlbumTopActivity.activityStart(UserInfoActivity.this,ACTIVITY_CODE_ALBUM,Constants.ALBUM_SELECT_SINGLE);
            }
        })
        .setCameraListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File file = new File(PATH_CACHE_IMAGE);
                cameraURI = Uri.fromFile(file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraURI);
                startActivityForResult(intent, ACTIVITY_CODE_CAMERA);
            }
        })
        .setCancelListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
        .create().show();
    }

    /**
     * 调用第三方剪切工具进行图片剪切,并将剪切后的图片路径返回
     */
    public void startPhotoZoom(Uri uri) {
        Uri cameraURI =Uri.fromFile(new File(PATH_CACHE_IMAGE));
        UCrop uCrop = UCrop.of(uri, cameraURI);
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        uCrop.withMaxResultSize(640,640);
        uCrop.withOptions(options);
        uCrop.start(UserInfoActivity.this);
    }

    public void startPhotoZoom() {
        ArrayList<String> pathlist = SPUtils.getStringList("photo_path");
        ArrayList<String> urllist = SPUtils.getStringList("photo_url");
        if(urllist .size() == 1){
            L.e(TAG+ " 单图片上传 开始剪裁");
            Uri cameraURI = Uri.fromFile(new File(PATH_CACHE_IMAGE));
            UCrop uCrop = UCrop.of(Uri.parse(urllist.get(0)), cameraURI);
            UCrop.Options options = new UCrop.Options();
            options.setFreeStyleCropEnabled(true);
            uCrop.withMaxResultSize(1920, 1920);
            uCrop.withOptions(options);
            uCrop.start(this);
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        avatarUri = UCrop.getOutput(result);
        if (avatarUri != null) {
            L.i(TAG + " handleCropResult 设置图片 avatarUri" + avatarUri.toString());
            userinfoAvatarIv.setImageURI(avatarUri);
            postAvatar();
        }
    }

    protected void postAvatar() {
        File file = new File(PATH_CACHE_IMAGE);
        if (file == null) {
            showErrorDialog(getResources().getString(R.string.picture_no_ture));
            return;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        NetworkManager.getUserDataApi()
                .postAvatar(SPUtils.getToken(), requestBody)
                .compose(RxUtils.androidSchedulers(this))
                .subscribe(avatarObserver);
    }

    //界面取得图片后的回调
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            L.i(TAG, "onActivityResult-->RESULT_OK");
            switch (requestCode) {
                case ACTIVITY_CODE_CAMERA:
                    // 相机拍照,获取结果后调用剪切工具
//                    Uri cameraUri = data.getData();
                    startPhotoZoom(cameraURI);
                    break;
                case ACTIVITY_CODE_ALBUM:
                    // 图库获取
                    startPhotoZoom();
                    break;
                case UCrop.REQUEST_CROP:
                    // 头像剪切后的返回结果
                    handleCropResult(data);
                    break;
                case ACTIVITY_CODE_NICKNAME:
                    userinfoNameTv.setText((String)SPUtils.get("user_nickname","").toString());
                    break;
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            L.e(TAG, "OnActivityResult-->" + resultCode + "用户取消了操作");
        } else {
            // 获取结果失败
            L.e(TAG, "OnActivityResult-->" + resultCode);
        }
    }


    protected void postNickname() {
        if(judgePostCondition()){
            L.e(TAG + " 修改昵称 newNickname："+newNickname);
            NetworkManager.getUserDataApi()
                    .postNickname(SPUtils.getToken(), newNickname)
                    .compose(RxUtils.androidSchedulers(this))
                    .subscribe(nameObserver);
        }
    }

    private boolean judgePostCondition(){
        newNickname = editNameTv.getText().toString().trim();
        if (newNickname.equals("")) {
            showWaringDialog(getResources().getString(R.string.input_nickname));
            return false;
        }

        return true;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.activity_slide_left_in, R.anim.activity_slide_right_out);
    }

}
