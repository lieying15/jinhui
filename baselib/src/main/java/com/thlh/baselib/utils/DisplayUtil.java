package com.thlh.baselib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import java.net.URL;


/**
 * dp、sp 转换为 px 的工具类
 * 
 * @author fxsky 2012.11.12
 *
 */
public class DisplayUtil {
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变（DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dp2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		Log.i("dp2px","dipValue:"+dipValue+"   density:"+ scale);
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * @param pxValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
	
	public static void showHtmlInTV(String html,TextView tv_content,final Context context) {
		tv_content.setText(Html.fromHtml(html, new Html.ImageGetter() {
			public Drawable getDrawable(String source) {
				Drawable drawable = null;
				URL url;
				try {
					url = new URL(source);
					drawable = Drawable.createFromResourceStream(
							context.getResources(), null, url.openStream(), "");
				} catch (Exception e) {
					return null;
				}

				Log.i("下载下来的图片大小", drawable.getIntrinsicWidth() + "*"
						+ drawable.getIntrinsicHeight());
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight());
				return drawable;
			}
		}, null));
	}
	
//	public static void displayImage(String uri, ImageView iv, DisplayImageOptions options) {
//		//内存缓存
//		MemoryCache<String, Bitmap> memoryCache = ImageLoader.getInstance().getMemoryCache();
//		Bitmap bitmap = memoryCache.get(uri);
//
//		if (bitmap != null) {
//			iv.setImageBitmap(bitmap);
//		} else {
//
//			// 硬盘缓存
//			DiskCache discCache = ImageLoader.getInstance().getDiscCache();
//
//			File file = discCache.get(uri);
//			// 得到的是每张图片的真实高度，不是显示在屏幕上的高度，只用于比较两列中哪列的高度矮
//			if (file.exists()) {
//				// 存在
//				Bitmap b = CropImageUtil.decodeFile(file);
//
//				iv.setImageBitmap(b);
//
//			} else {
//				Log.i("网络上获取图片", "网络上获取图片");
//				ImageLoader.getInstance().displayImage(uri, iv, options);
//			}
//		}
//	}
//
//	public static void displayImage(String uri, ImageView iv) {
//		//内存存储
//		MemoryCache<String, Bitmap> memoryCache = ImageLoader.getInstance().getMemoryCache();
//		Bitmap bitmap = memoryCache.get(uri);
//		//memoryCache.
//		if (bitmap != null) {
//			iv.setImageBitmap(bitmap);
//		} else {
//			// 硬盘存储
//			DiskCache discCache = ImageLoader.getInstance().getDiscCache();
//			File file = discCache.get(uri);
//			if (file.exists()) {
//				// 存在
//				Bitmap b = CropImageUtil.decodeFile(file);
//				iv.setImageBitmap(b);
//
//
//			} else {
//				Log.i("网络上获取图片", "网络上获取图片");
//				ImageLoader.getInstance().displayImage(uri, iv);
//			}
//		}
//	}


}