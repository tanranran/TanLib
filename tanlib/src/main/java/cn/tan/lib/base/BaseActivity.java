package cn.tan.lib.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;

import cn.tan.lib.R;
import cn.tan.lib.util.ActivityUtil;
import de.greenrobot.event.EventBus;

public class BaseActivity extends AppCompatActivity implements IBaseActivity{
	protected Activity context;
	protected BaseRootLayout rootLayout;
	protected ActionBarWidget actionBarWidget;
	protected ToolBarWidget toolBarWidget;
	protected EmptyLayout emptyLayout;
	protected SystemBarTintManager tintManager;
	protected boolean isShowInput = true;// 点击空白处是否隐藏键盘
	protected boolean isUserEvent = false;// 是否注册EventBus

	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		rootLayout = new BaseRootLayout(context);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ActivityUtil.getInstance().setOnActivityCreate(context);
		if (isUserEvent) {
			EventBus.getDefault().register(context);
		}
		super.onCreate(savedInstanceState);
	}
	public void initView() {}
	public void setContentView(View view) {
		initSystemBar();
		rootLayout.contentFrameLayout.removeAllViews();
		rootLayout.contentFrameLayout.addView(view);
		super.setContentView(rootLayout);
	}

	public void setContentView(int layoutResID) {
		View view = View.inflate(context, layoutResID, null);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		setContentView(view);
	}

	public ActionBarWidget actionBar(String title) {
		actionBarWidget = ActionBarWidget.initActionBar(this, title);
		return actionBarWidget;
	}
	public ToolBarWidget toorBar(String title){
		toolBarWidget=ToolBarWidget.initToolBar(this,title);
		return  toolBarWidget;
	}
	public void loadingStart() {
		if(emptyLayout==null){
			emptyLayout=new EmptyLayout(context);
			setContentView(emptyLayout);
		}else {
			emptyLayout.setVisibility(View.VISIBLE);
		}
	}
	public void loadingComplete(){
		if(emptyLayout!=null){
			emptyLayout.setVisibility(View.GONE);
		}
	}
	protected void onDestroy() {
		super.onDestroy();
		if (isUserEvent) {
			EventBus.getDefault().unregister(context);
		}
		ActivityUtil.getInstance().setOnActivityDestroy(context);
	}

	protected void onResume() {
		super.onResume();
		ActivityUtil.getInstance().setOnActivityResume(context);
	}

	@TargetApi(VERSION_CODES.KITKAT)
	public void initSystemBar() {
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			getWindow().setFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		tintManager = new SystemBarTintManager(this);// 创建状态栏的管理实例
		tintManager.setStatusBarTintEnabled(true);// 激活状态栏设置
		tintManager.setNavigationBarTintEnabled(true);// 激活导航栏设置
		rootLayout.setFitsSystemWindows(true);
		TypedArray array = obtainStyledAttributes(new int[]{R.attr.colorPrimaryDark});
		int colorPrimaryDark = array.getColor(0, getResources().getColor(R.color.toolbarColor));
		tintManager.setStatusBarTintColor(colorPrimaryDark);
		tintManager.setStatusBarDarkMode(true, this);
		// tintManager.setTintColor(Color.BLACK);//设置一个颜色给系 统栏
		// tintManager.setNavigationBarTintColor(Color.RED);
		// tintManager.setNavigationBarTintResource(R.drawable.re);//
		// 设置一个样式背景给导航栏
		// tintManager.setStatusBarTintDrawable(MyDrawable); // 设置一个状态栏资源
	}

	/**
	 * 设置StatusBar透明
	 */
	public void setSystemBarTransparent() {
		tintManager.setTintColor(Color.TRANSPARENT);
		rootLayout.setFitsSystemWindows(false);
	}
	/** 点击空白隐藏软键盘 */
	public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
		if (isShowInput) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				// 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
				View v = getCurrentFocus();
				if (isShouldHideInput(v, ev)) {
					hideSoftInput(v.getWindowToken());
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
		if (v != null && (v instanceof EditText)) {
			return isMotionEventView(v, event);
		}
		return false;
	}
	protected boolean isMotionEventView(View v, MotionEvent event) {
		if (v != null) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left+ v.getWidth();
			if (event.getX() > left && event.getX() < right&& event.getY() > top && event.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	public boolean isUserEvent() {
		return isUserEvent;
	}

	public void setUserEvent(boolean isUserEvent) {
		this.isUserEvent = isUserEvent;
	}

	public boolean isShowInput() {
		return isShowInput;
	}

	public void setShowInput(boolean isShowInput) {
		this.isShowInput = isShowInput;
	}
	@SuppressWarnings("unchecked")
	public final <E extends View> E getView(int id) {
		try {
			return (E) findViewById(id);
		} catch (ClassCastException ex) {
			throw ex;
		}
	}
}
