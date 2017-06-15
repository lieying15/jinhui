package com.thlh.baselib.base;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.thlh.baselib.utils.ActivityUtils;
import com.thlh.baselib.utils.BaseLog;
import com.thlh.baselib.utils.DialogUtils;
import com.thlh.viewlib.progress.ProgressMaterial;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;


public abstract class BaseActivity extends RxAppCompatActivity implements BaseView{
	private  final String TAG = "BaseActivity";
	// 临时用户的缓存
	protected SharedPreferences userInfoSP;
	protected SharedPreferences tempUserInfoSP;

	protected abstract void initVariables();
	protected abstract void initBaseViews(Bundle savedInstanceState);
	protected abstract void loadData();
	protected String activityName ;
	protected Subscription subscription;
	protected List<Subscription> subscriptionList = new ArrayList<>();
	protected ProgressMaterial progressMaterial;

	//**************** Android M Permission (Android 6.0权限控制代码封装)
	private int permissionRequestCode = 88;
	private PermissionCallback permissionRunnable ;
	public interface PermissionCallback{
		void hasPermission();
		void noPermission();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityName = this.getClass().getSimpleName();
		BaseLog.i(TAG, activityName + " onCreate");
		userInfoSP = BaseApplication.getInstance().getUserinfoSharedPreferences();
		tempUserInfoSP = BaseApplication.getInstance().getTempUserInfoSharedPreferences();

		progressMaterial  = ProgressMaterial.create(this, true, null);

		ActivityUtils.addActivity(this);
		setImmersionStatus(); //顶部框的处理。
		try {
			initVariables();
			initBaseViews(savedInstanceState);
			loadData();
		}catch (Exception e){

		}

	}

	private void setImmersionStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// 透明状态栏
//			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(activityName);

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(activityName);

	}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
	protected void onStop() {
        super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseLog.i(TAG, activityName + "onDestroy()");
		ActivityUtils.removeActivity(this);
		unsubscribe();
	}

	protected void unsubscribe() {
		if (subscription != null && !subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
		if(subscriptionList.size()!=0){
			for (Subscription subscription : subscriptionList){
				if (subscription != null && !subscription.isUnsubscribed()) {
					subscription.unsubscribe();
				}
			}
		}
	}


	public ProgressMaterial getProgressMaterial() {
		return progressMaterial;
	}

	public void setProgressMaterial(ProgressMaterial progressMaterial) {
		this.progressMaterial = progressMaterial;
	}



	/**
	 * Android M运行时权限请求封装
	 * @param permissionDes 权限描述
	 * @param runnable 请求权限回调
	 * @param permissions 请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.WRITE_CONTACTS
	 */
	public void performCodeWithPermission(@NonNull String permissionDes, PermissionCallback runnable, @NonNull String... permissions){
		if(permissions == null || permissions.length == 0)return;
//        this.permissionrequestCode = requestCode;
		this.permissionRunnable = runnable;
		if((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || checkPermissionGranted(permissions)){
			if(permissionRunnable!=null){
				permissionRunnable.hasPermission();
				permissionRunnable = null;
			}
		}else{
			//permission has not been granted.
			requestPermission(permissionDes,permissionRequestCode,permissions);
		}

	}
	private boolean checkPermissionGranted(String[] permissions){
		boolean flag = true;
		for(String p:permissions){
			if(ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED){
				flag = false;
				break;
			}
		}
		return flag;
	}
	private void requestPermission(String permissionDes,final int requestCode,final String[] permissions){
		if(shouldShowRequestPermissionRationale(permissions)){
			// Provide an additional rationale to the user if the permission was not granted
			// and the user would benefit from additional context for the use of the permission.
			// For example, if the request has been denied previously.

//            Snackbar.make(getWindow().getDecorView(), requestName,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.common_ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(BaseAppCompatActivity.this,
//                                    permissions,
//                                    requestCode);
//                        }
//                    })
//                    .show();
			//如果用户之前拒绝过此权限，再提示一次准备授权相关权限
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage(permissionDes)
					.setPositiveButton("确认", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
						}
					}).show();

		}else{
			// Contact permissions have not been granted yet. Request them directly.
			ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
		}
	}
	private boolean shouldShowRequestPermissionRationale(String[] permissions){
		boolean flag = false;
		for(String p:permissions){
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,p)){
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * Callback received when a permissions request has been completed.
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if(requestCode == permissionRequestCode){
			if(verifyPermissions(grantResults)){
				if(permissionRunnable!=null) {
					permissionRunnable.hasPermission();
					permissionRunnable = null;
				}
			}else{
//				L.e("暂无权限执行相关操作！");
				if(permissionRunnable!=null) {
					permissionRunnable.noPermission();
					permissionRunnable = null;
				}
			}
		}else{
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}

	}
	public boolean verifyPermissions(int[] grantResults) {
		// At least one result must be checked.
		if(grantResults.length < 1){
			return false;
		}

		// Verify that each required permission has been granted, otherwise return false.
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				return false;
			}
		}
		return true;
	}
	//********************** END Android M Permission ****************************************

	@Override
	public void showLoadingBar() {
		getProgressMaterial().show();
	}

	@Override
	public void hideLoadindBar() {
		getProgressMaterial().hide();

	}

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.anim_null,R.anim.anim_null);
    }

	public void showSuccessDialog( String msg){
		DialogUtils.showNormal(this,DialogUtils.TYPE_NORMAL_SUCCESS,msg);
	}

	public void showWaringDialog( String msg){
		DialogUtils.showNormal(this,DialogUtils.TYPE_NORMAL_WARNING,msg);
	}

	public void showErrorDialog( String msg){
		DialogUtils.showNormal(this,DialogUtils.TYPE_NORMAL_ERROR,msg);
	}

}
