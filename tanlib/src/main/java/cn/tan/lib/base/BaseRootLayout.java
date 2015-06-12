package cn.tan.lib.base;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class BaseRootLayout extends LinearLayout{

	public static String ACTION_BAR_TAG = "action_bar_tag";
	public static String CONTENT_TAG = "content_tag";
	public LinearLayout actionBarLLayout;
	public FrameLayout contentFrameLayout;
	public BaseRootLayout(Context context) {
		super(context);
		init();
	}
	private void init() {
		setOrientation(LinearLayout.VERTICAL);
		actionBarLLayout = new LinearLayout(getContext());
		contentFrameLayout = new FrameLayout(getContext());
		actionBarLLayout.setTag(ACTION_BAR_TAG);
		contentFrameLayout.setTag(CONTENT_TAG);
		addView(actionBarLLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addView(contentFrameLayout,new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	/**
	 * 设置是否展示ActionBar
	 * 
	 * @param isShowActionBar
	 */
	public void setShowActionBar(boolean isShowActionBar) {
		if (isShowActionBar) {
			actionBarLLayout.setVisibility(VISIBLE);
		} else {
			actionBarLLayout.setVisibility(GONE);
		}
	}
}
