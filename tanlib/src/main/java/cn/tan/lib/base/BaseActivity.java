package cn.tan.lib.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import cn.tan.lib.listenter.HomeListen;
import cn.tan.lib.util.ActivityUtil;
import cn.tan.lib.util.InputUtil;
import cn.tan.lib.util.StatusBarUtils;
import de.greenrobot.event.EventBus;

public class BaseActivity extends AppCompatActivity{
	protected Activity context;
	protected BaseRootLayout rootLayout;
	protected ActionBarWidget actionBarWidget;//titleBar的一种
	protected EmptyLayout emptyLayout;//加载中的布局
	protected HomeListen homeListen = null;//Home键监听
	protected boolean eventBusEnable = false;// 是否注册EventBus
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActivity();
		initHomeListen();
		initStatusBar();
	}
	public void setContentView(View view) {
		if(context==null||rootLayout==null){
			context = this;
			rootLayout = new BaseRootLayout(this);
		}
		if (rootLayout.contentFrameLayout == null) {
			rootLayout.contentFrameLayout = (FrameLayout) rootLayout.findViewWithTag(BaseRootLayout.CONTENT_TAG);
		}
		rootLayout.contentFrameLayout.addView(view);
		super.setContentView(rootLayout);
	}
	public void setContentView(@NonNull int layoutResID) {
		View view = View.inflate(this, layoutResID, null);
		view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		setContentView(view);
	}
	public void initActivity(){
		context = this;//context赋值
		rootLayout = new BaseRootLayout(this);//初始化基础页面
		ActivityUtil.getInstance().setOnActivityCreate(this);//添加当前Activity到栈中
		if (eventBusEnable) {EventBus.getDefault().register(this);}//注册EventBus
	}

	private void initHomeListen() {
		homeListen = new HomeListen(this);
		homeListen.setOnHomeBtnPressListener(new HomeListen.OnHomeBtnPressLitener() {
			public void onHomeBtnPress() {
				Logger.d("按下Home按键！");
			}

			public void onHomeBtnLongPress() {
				Logger.d("长按Home按键！");
			}
		});
	}
	public void initStatusBar(){
		StatusBarUtils.from(this).setTransparentStatusBar(true).setTransparentNavigationBar(true).setLightStatusBar(true).process();
	}
	public void setSystemBarTransparent() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			rootLayout.setFitsSystemWindows(false);
			rootLayout.statusBar.setVisibility(View.GONE);
		}
	}
	public void setSystemBarColor(int color){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			rootLayout.statusBar.setBackgroundColor(color);//设置一个颜色给系 统栏
		}
	}
	public ActionBarWidget actionBar(String title) {
		actionBarWidget = ActionBarWidget.initActionBar(this, title);
		return actionBarWidget;
	}
	public EmptyLayout getEmptyLayout() {
		if (emptyLayout == null) {
			emptyLayout = new EmptyLayout(this);
			setContentView(emptyLayout);
		}
		return emptyLayout;
	}

	public void loadingStart() {
		getEmptyLayout();
		emptyLayout.showLoading();
	}

	public void loadingComplete() {
		if (emptyLayout != null) {
			emptyLayout.setVisibility(View.GONE);
		}
	}

	public void loadingError(EmptyLayout.OnClickBtnListener btnErrorListener) {
		if (emptyLayout != null) {
			emptyLayout.showError(btnErrorListener);
		}
	}

	public void loadingError(Object errorTxt, EmptyLayout.OnClickBtnListener btnErrorListener) {
		loadingError(errorTxt.toString(), btnErrorListener);
	}

	public void loadingError(String errorTxt, EmptyLayout.OnClickBtnListener btnErrorListener) {
		if (emptyLayout != null) {
			emptyLayout.showError(errorTxt, btnErrorListener);
		}
	}

	public void JudgeEmpty(int count) {
		if (emptyLayout != null) {
			emptyLayout.JudgeEmpty(count);
		}
	}
	public void JudgeEmpty(int count,int res) {
		if (emptyLayout != null) {
			emptyLayout.JudgeEmpty(count,res);
		}
	}
	public void JudgeEmpty(int count,int res,String message,String btnTxt,EmptyLayout.OnClickBtnListener btnEmptyListener) {
		if (emptyLayout != null) {
			emptyLayout.JudgeEmpty(count,res,message,btnTxt,btnEmptyListener);
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if(null != this.getCurrentFocus()){
			InputUtil.getInstance(this).hide();
		}
		return super .onTouchEvent(event);
	}

	public <T extends View> T getView(int resId) {
		return (T) findViewById(resId);
	}

	protected void onPause() {
		super.onPause();
		homeListen.stop();
	}

	protected void onResume() {
		super.onResume();
		ActivityUtil.getInstance().setOnActivityResume(this);
		homeListen.start();
	}

	protected void onDestroy() {
		super.onDestroy();
		if (eventBusEnable) {EventBus.getDefault().unregister(this);}
		ActivityUtil.getInstance().setOnActivityDestroy(this);
	}
}
