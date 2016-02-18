package cn.tan.lib.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class InputUtil {
	private InputMethodManager mInputMethodManager;
	private static Activity mActivity;
	private static class HolderClass {  
        private final static InputUtil instance = new InputUtil();  
	}  
	private InputUtil() {
		mInputMethodManager = (InputMethodManager) mActivity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	public static InputUtil getInstance(Activity activity) {  
		mActivity = activity;
        return HolderClass.instance;  
    } 
	/**
	 * 强制显示输入法
	 */
	public void show() {
		show(mActivity.getWindow().getCurrentFocus());
	}

	public void show(View view) {
		mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 强制关闭输入法
	 */
	public void hide() {
		hide(mActivity.getWindow().getCurrentFocus());
	}
	public void hideKeyboard() {
		if (mActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (mActivity.getCurrentFocus() != null)
				((InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		mActivity=null;
	}
	public void hide(View view) {
		mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/**
	 * 如果输入法已经显示，那么就隐藏它；如果输入法现在没显示，那么就显示它
	 */
	public void showOrHide() {
		mInputMethodManager.toggleSoftInput(0,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
