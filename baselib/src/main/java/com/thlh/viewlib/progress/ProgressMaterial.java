package com.thlh.viewlib.progress;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.thlh.baselib.R;


public class ProgressMaterial extends Dialog {
    public ProgressMaterial(Context context) {
        super(context);
    }

    public ProgressMaterial(Context context, int theme) {
        super(context, theme);
    }
    /**
     * 弹出自定义ProgressDialog
     *
     * @param context
     *            上下文
     * @param cancelable
     *            是否按返回键取消
     * @param cancelListener
     *            按下返回键监听
     * @return
     */
    public static ProgressMaterial createAndShow(Context context, boolean cancelable, OnCancelListener cancelListener) {
        ProgressMaterial dialog = create(context, cancelable,cancelListener);
        dialog.show();
        return dialog;
    }

    public static ProgressMaterial create(Context context, boolean cancelable, OnCancelListener cancelListener) {
        ProgressMaterial dialog = new ProgressMaterial(context, R.style.material_progress_dialog);
        dialog.setTitle("");
        dialog.setContentView(R.layout.progress_material);

        // 按返回键是否取消
        dialog.setCancelable(cancelable);
        // 监听返回键处理
        dialog.setOnCancelListener(cancelListener);
        // 设置居中
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 300;
        lp.gravity = Gravity.CENTER;
        // 设置背景层透明度
        lp.dimAmount = 0f;
        dialog.getWindow().setAttributes(lp);
        return dialog;
    }

    /**
     * 当窗口焦点改变时调用
     */
    public void onWindowFocusChanged(boolean hasFocus) {
//        ImageView imageView = (ImageView) findViewById(R.id.iv_loading);
//        // 获取ImageView上的动画背景
//        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
//        // 开始动画
//        spinner.start();
    }

    /**
     * 给Dialog设置提示信息
     *
     * @param message
     */
    public void setMessage(CharSequence message) {
        if (message != null && message.length() > 0) {

        }
    }

}