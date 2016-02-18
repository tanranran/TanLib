package cn.tan.lib.base;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import cn.tan.lib.R;
import cn.tan.lib.util.ScreenUtils;

public class BaseRootLayout extends LinearLayout{

	public static String ACTION_BAR_TAG = "action_bar_tag";
	public static String CONTENT_TAG = "content_tag";
	public LinearLayout actionBarLLayout;
	public FrameLayout contentFrameLayout;
	public FrameLayout statusBar;
	private boolean isHiddenActionBar;
	public BaseRootLayout(Context context) {
		super(context);
		init();
		initConfig();
	}
	private void init() {
		setLayoutTransition(new LayoutTransition());//设置隐藏动画效果
		setOrientation(LinearLayout.VERTICAL);
		statusBar=new FrameLayout(getContext());
		actionBarLLayout = new LinearLayout(getContext());
		contentFrameLayout = new FrameLayout(getContext());
		actionBarLLayout.setTag(ACTION_BAR_TAG);
		contentFrameLayout.setTag(CONTENT_TAG);
		addView(statusBar, new LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtils.getStatusHeight((Activity) getContext())));
		addView(actionBarLLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addView(contentFrameLayout,new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	private void initConfig(){
		TypedArray array = getContext().obtainStyledAttributes(new int[]{R.attr.titleBg});
		statusBar.setBackgroundColor(array.getColor(0, getResources().getColor(R.color.toolbarColor)));
		array.recycle();

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
	//隐藏或者显示ActionBar
	protected void hideOrShowActionBar() {
		ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator animation) {
				int b = (int) ((Float.parseFloat(animation.getAnimatedValue().toString()) * actionBarLLayout.getHeight()));
				moveViewWithFinger(actionBarLLayout, isHiddenActionBar ? -b : b - actionBarLLayout.getHeight());
			}
		};
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			actionBarLLayout.animate()
					.translationY(isHiddenActionBar ? 0 : -actionBarLLayout.getHeight())
					.setInterpolator(new DecelerateInterpolator(2))
					.setUpdateListener(updateListener);
		} else {
			ObjectAnimator oa = ObjectAnimator.ofFloat(actionBarLLayout, View.TRANSLATION_Y, isHiddenActionBar ? 0 : -actionBarLLayout.getHeight());
			oa.setInterpolator(new DecelerateInterpolator(2));
			oa.addUpdateListener(updateListener);
			oa.start();
		}
		isHiddenActionBar = !isHiddenActionBar;
	}
	private void moveViewWithFinger(View view, int rawY) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.topMargin = rawY;
		view.setLayoutParams(params);
	}
}
