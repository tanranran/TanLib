package cn.tan.lib.util;

import cn.tan.lib.R;
import cn.tan.lib.base.BaseApplication;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	public static void showToast(int resID) {
		showToast(BaseApplication.getInstance(), Toast.LENGTH_SHORT, resID);
	}

	public static void showToast(String text) {
		showToast(BaseApplication.getInstance(), Toast.LENGTH_SHORT, text);
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
		final Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
		View mNextView = toast.getView();
		if (mNextView != null)
			mNextView.setBackgroundResource(resID);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
		return toast;
	}

	public static void showToast(final Context ctx, final int duration,
			final String text) {
		final Toast toast = Toast.makeText(ctx, text, duration);
		toast.show();
	}

	public static void showToastView(final Context ctx, final int duration,
			final String text) {
		final Toast toast = Toast.makeText(ctx, text, duration);
		View view = RelativeLayout.inflate(ctx, R.layout.layout_toast, null);
		TextView mNextView = (TextView) view.findViewById(R.id.toast_name);
		toast.setView(view);
		mNextView.setText(text);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void showToast(final Context ctx, final String text,
			Drawable left, Drawable top, Drawable right, Drawable bottom) {
		final Toast toast = Toast.makeText(ctx, text, Toast.LENGTH_LONG);
		View view = RelativeLayout.inflate(ctx, R.layout.layout_toast, null);
		TextView mNextView = (TextView) view.findViewById(R.id.toast_name);
		mNextView.setCompoundDrawablesWithIntrinsicBounds(left, top, right,bottom);
		toast.setView(view);
		toast.setGravity(Gravity.CENTER, 0, 0);
		mNextView.setText(text);
		toast.show();
	}

	/** 在UI线程运行弹出 */
	public static void showToastOnUiThread(final Activity ctx, final String text) {
		if (ctx != null) {
			ctx.runOnUiThread(new Runnable() {
				public void run() {
					showToast(ctx, text);
				}
			});
		}
	}
}
