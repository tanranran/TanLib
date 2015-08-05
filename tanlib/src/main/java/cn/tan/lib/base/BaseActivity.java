package cn.tan.lib.base;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;

import cn.tan.lib.R;
import cn.tan.lib.interf.IBaseActivity;
import cn.tan.lib.listenter.HomeListen;
import cn.tan.lib.util.ActivityUtil;
import de.greenrobot.event.EventBus;

public class BaseActivity extends AppCompatActivity implements IBaseActivity {
	protected Activity context;
	protected BaseRootLayout rootLayout;
	protected ActionBarWidget actionBarWidget;
	protected ToolBarWidget toolBarWidget;
	protected EmptyLayout emptyLayout;
	protected SystemBarTintManager tintManager;
	protected boolean isShowInput = true;// 点击空白处是否隐藏键盘
	protected boolean isUserEvent = false;// 是否注册EventBus
	protected boolean isHidden = false;
	private HomeListen mHomeListen = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			finish();
			return;
		}
		context = this;
		rootLayout = new BaseRootLayout(context);
		ActivityUtil.getInstance().setOnActivityCreate(context);
		if (isUserEvent) {
			EventBus.getDefault().register(context);
		}
		initHomeListen();
	}

	public void initView() {}

	public void initData() {
	}

	public void setContentView(View view) {
		initSystemBar();
		rootLayout.contentFrameLayout.removeAllViews();
		rootLayout.contentFrameLayout.addView(view);
		super.setContentView(rootLayout);
	}

	public void setContentView(@StringRes int layoutResID) {
		View view = View.inflate(context, layoutResID, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		setContentView(view);
	}

	@TargetApi(VERSION_CODES.LOLLIPOP)
	public void initSystemBar() {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			getWindow().setFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			tintManager = new SystemBarTintManager(this);// 创建状态栏的管理实例
			tintManager.setStatusBarTintEnabled(true);// 激活状态栏设置
//			tintManager.setNavigationBarTintEnabled(true);// 激活导航栏设置
			TypedArray array = obtainStyledAttributes(new int[]{R.attr.colorPrimaryDark});
			int colorPrimaryDark = array.getColor(0, getResources().getColor(R.color.toolbarColor));
			tintManager.setStatusBarTintColor(colorPrimaryDark);
			tintManager.setStatusBarDarkMode(true, this);//设置小米的状态栏字体颜色
//			tintManager.setTintColor(Color.TRANSPARENT);//设置一个颜色给系 统栏
			// tintManager.setNavigationBarTintColor(Color.RED);
			// tintManager.setNavigationBarTintResource(R.drawable.re);//
			// 设置一个样式背景给导航栏
			// tintManager.setStatusBarTintDrawable(MyDrawable); // 设置一个状态栏资源
			rootLayout.setFitsSystemWindows(true);
			array.recycle();
		}
	}

	/**
	 * 设置StatusBar透明
	 */
	public void setSystemBarTransparent() {
		tintManager.setTintColor(Color.TRANSPARENT);
		rootLayout.setFitsSystemWindows(false);
	}

	public ActionBarWidget actionBar(String title) {
		actionBarWidget = ActionBarWidget.initActionBar(this, title);
		return actionBarWidget;
	}

	public ToolBarWidget toorBar(String title){
		toolBarWidget=ToolBarWidget.initToolBar(this,title);
		return  toolBarWidget;
	}

	private void moveViewWithFinger(View view, int rawY) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.topMargin = rawY;
		view.setLayoutParams(params);
	}

	protected void hideOrShowToolbar() {
		ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
			public void onAnimationUpdate(ValueAnimator animation) {
				int b = (int) ((Float.parseFloat(animation.getAnimatedValue().toString()) * rootLayout.actionBarLLayout.getHeight()));
				moveViewWithFinger(rootLayout.actionBarLLayout, isHidden ? -b : b - rootLayout.actionBarLLayout.getHeight());
			}
		};
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			rootLayout.actionBarLLayout.animate()
					.translationY(isHidden ? 0 : -rootLayout.actionBarLLayout.getHeight())
					.setInterpolator(new DecelerateInterpolator(2))
					.setUpdateListener(updateListener);
		} else {
			ObjectAnimator oa = ObjectAnimator.ofFloat(rootLayout.actionBarLLayout, View.TRANSLATION_Y, isHidden ? 0 : -rootLayout.actionBarLLayout.getHeight());
			oa.setInterpolator(new DecelerateInterpolator(2));
			oa.addUpdateListener(updateListener);
			oa.start();
		}
		isHidden = !isHidden;
	}

	protected boolean onPrepareOptionsPanel(View view, Menu menu) {
		if (menu != null) {
			if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
				try {
					Method m = menu.getClass().getDeclaredMethod(
							"setOptionalIconsVisible", Boolean.TYPE);
					m.setAccessible(true);
					m.invoke(menu, true);
				} catch (Exception e) {
					Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
				}
			}
		}
		return super.onPrepareOptionsPanel(view, menu);
	}

	public void loadingStart() {
		if(emptyLayout==null){
			emptyLayout=new EmptyLayout(context);
			setContentView(emptyLayout);
		}else {
			emptyLayout.showLoading();
			emptyLayout.setVisibility(View.VISIBLE);
		}
	}

	public void loadingComplete(){
		if(emptyLayout!=null){
			emptyLayout.setVisibility(View.GONE);
		}
	}

	protected void onPause() {
		super.onPause();
		mHomeListen.stop();
	}

	protected void onResume() {
		super.onResume();
		ActivityUtil.getInstance().setOnActivityResume(context);
		mHomeListen.start();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (isUserEvent) {
			EventBus.getDefault().unregister(context);
		}
		ActivityUtil.getInstance().setOnActivityDestroy(context);
	}

	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		switch (level) {
			case TRIM_MEMORY_UI_HIDDEN://当界面不可见时释放内存
				break;
			case TRIM_MEMORY_RUNNING_MODERATE://可能会杀进程
				break;
			case TRIM_MEMORY_RUNNING_LOW://会杀进程
				break;
			case TRIM_MEMORY_RUNNING_CRITICAL://已经开始杀进程
				break;
			case TRIM_MEMORY_BACKGROUND://后台中 可能会杀进程
				break;
			case TRIM_MEMORY_MODERATE://后台中 会杀进程
				break;
			case TRIM_MEMORY_COMPLETE://后台中 已经开始杀进程
				break;
		}
	}

	/** 点击空白隐藏软键盘 */
	public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
		if (isShowInput) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
				View v = getCurrentFocus();
				if (v != null) {
					if (isShouldHideInput(v, ev)) {
						hideSoftInput(v.getWindowToken());
					}
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	protected void hideSoftInput(IBinder token) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	protected boolean isShouldHideInput(View v, MotionEvent event) {
		return v != null && (v instanceof EditText) && isMotionEventView(v, event);
	}

	protected boolean isMotionEventView(View v, MotionEvent event) {
		if (v != null) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left+ v.getWidth();
			return !(event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom);
		}
		return false;
	}

	private void initHomeListen() {
		mHomeListen = new HomeListen(this);
		mHomeListen.setOnHomeBtnPressListener(new HomeListen.OnHomeBtnPressLitener() {
			public void onHomeBtnPress() {
				Logger.d("按下Home按键！");
			}

			public void onHomeBtnLongPress() {
				Logger.d("长按Home按键！");
			}
		});
	}

	public void setShowInput(boolean isShowInput) {
		this.isShowInput = isShowInput;
	}

	public final <E extends View> E getView(int id) throws ClassCastException {
		return (E) findViewById(id);
	}

}
