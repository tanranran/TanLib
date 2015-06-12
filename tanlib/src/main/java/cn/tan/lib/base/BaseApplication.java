package cn.tan.lib.base;


import android.app.Application;

import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

public class BaseApplication extends Application{
	protected static BaseApplication instance;
	private boolean isDebug=true;
	public void onCreate() {
		instance = this;
		initLog();
		super.onCreate();
	}
	public void initLog(){
		Logger
				.init(getPackageName())               // default PRETTYLOGGER or use just init()
				.setMethodCount(2)            // default 2
				.hideThreadInfo()             // default shown
				.setLogLevel(isDebug()?LogLevel.FULL:LogLevel.NONE)  // default LogLevel.FULL
				.setMethodOffset(2);
	}
	public static BaseApplication getInstance() {
		return instance;
	}
	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
}
