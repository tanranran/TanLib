package cn.tan.lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.tan.lib.R;
import cn.tan.lib.base.BaseApplication;

public class ToastUtil {

	public static Toast mToast;
	public static void showToast(int resID) {
		showToast(BaseApplication.getInstance(), Toast.LENGTH_SHORT, resID);
	}

	public static void showToast(String text) {
		showToast(BaseApplication.getInstance(), Toast.LENGTH_SHORT, text);
	}
	public static void showToast(Object text) {
		showToast(BaseApplication.getInstance(), Toast.LENGTH_SHORT, text.toString());
	}
	public static void showToast(Context ctx, int resID) {
		showToast(ctx, Toast.LENGTH_SHORT, resID);
	}

	public static void showToast(Context ctx, String text) {
		showToast(ctx, Toast.LENGTH_SHORT, text);
	}

	public static void showLongToast(Context ctx, int resID) {
		showToast(ctx, Toast.LENGTH_LONG, resID);
	}

	public static void showLongToast(int resID) {
		showToast(BaseApplication.getInstance(), Toast.LENGTH_LONG, resID);
	}
	public static void showLongToast(Object obj) {
		showToast(BaseApplication.getInstance(), Toast.LENGTH_LONG, obj.toString());
	}

	public static void showLongToast(Context ctx, String text) {
		showToast(ctx, Toast.LENGTH_LONG, text);
	}

	public static void showLongToast(String text) {
		showToast(BaseApplication.getInstance(), Toast.LENGTH_LONG, text);
	}

	public static void showToast(Context ctx, int duration, int resID) {
		try {
			showToast(ctx, duration, ctx.getString(resID));
		} catch (Exception e) {
			showToast(ctx, duration, resID + "");
		}
	}

	/** Toast一个图片 */
	public static Toast showToastImage(Context ctx, int resID) {
		mToast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
		View mNextView = mToast.getView();
		if (mNextView != null)
			mNextView.setBackgroundResource(resID);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
		return mToast;
	}

	public static void showToast(final Context ctx, final int duration,
			final String text) {
		initToastAssignment(ctx, text);
		mToast.show();
	}

	public static void showToastView(final Context ctx, final int duration,
			final String text) {
		initToastAssignment(ctx, text);
		View view = RelativeLayout.inflate(ctx, R.layout.layout_toast, null);
		TextView mNextView = (TextView) view.findViewById(R.id.toast_name);
		mToast.setView(view);
		mNextView.setText(text);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mToast.show();
	}

	public static void showToast(final Context ctx, final String text,
			Drawable left, Drawable top, Drawable right, Drawable bottom) {
		initToastAssignment(ctx, text);
		View view = RelativeLayout.inflate(ctx, R.layout.layout_toast, null);
		TextView mNextView = (TextView) view.findViewById(R.id.toast_name);
		mNextView.setCompoundDrawablesWithIntrinsicBounds(left, top, right,bottom);
		mToast.setView(view);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		mNextView.setText(text);
		mToast.show();
	}
	public static void initToastAssignment(final Context ctx, final String text){
		if(mToast!=null){
			mToast.cancel();
		}
		mToast=Toast.makeText(ctx, text, Toast.LENGTH_LONG);
	}
	/** 在UI线程运行弹出 */
	public static void showToastOnUiThread(final Activity ctx, final String text) {
		if (ctx != null) {
			ctx.runOnUiThread(new Runnable() {
				public void run() {
					showLongToast(ctx, text);
				}
			});
		}
	}
}
