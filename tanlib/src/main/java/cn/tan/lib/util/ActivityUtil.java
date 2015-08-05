package cn.tan.lib.util;

import android.app.Activity;

import java.util.ArrayList;

public class ActivityUtil {
	private volatile static ActivityUtil instance;
	private ArrayList<Activity> activityList = new ArrayList<Activity>();
	private Activity nowActivity = null;//当前Activity

	protected ActivityUtil() {
	}

	public static ActivityUtil getInstance() {//采用双重检查加锁实例化单件
		if(instance == null){//第一次检查
	        synchronized (ActivityUtil.class) {
	          if(instance == null) {//第二次检查
	            instance = new ActivityUtil();
	          }
	        }
	      }
	      return instance;
	}

	/**
	 * activity 创建
	 * 
	 * @param activity
	 */
	public void setOnActivityCreate(Activity activity) {
		if (null == activityList) {
			activityList = new ArrayList<Activity>();
		}
		activityList.add(0, activity);
		setNowActivity(activity);
	}

	/**
	 * 获取当前activity
	 *
	 * @return
	 */
	public Activity getNowActivity() {
		return this.nowActivity;
	}

	/**
	 * 设置当前的activity
	 */
	private void setNowActivity(Activity activity) {
		nowActivity = activity;
	}

	/**
	 * 设置当前的activity
	 */
	private void setCurrentActivity() {
		if (null != activityList && activityList.size() > 0) {
			nowActivity = activityList.get(0);
		}
	}
	/**
	 * 根据class从stack内获取Activity
	 * 
	 * @param cls
	 * @return
	 */
	public Activity getActivityByClass(Class<?> cls) {
		for (Activity activity : activityList) {
			if (activity.getClass().equals(cls)) {
				return activity;
			}
		}
		return null;
	}
	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		setOnActivityDestroy(getActivityByClass(cls));
	}
	/**
	 * 判断activity有没有打开
	 * 
	 * @param activity
	 * @return
	 */
	public boolean getActivty(Class<?> cls) {
		if (activityList == null || activityList.isEmpty())
			return false;
		for (Activity activity : activityList) {
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		if (activityList == null) {
			return;
		}
		for (int i = 0, size = activityList.size(); i < size; i++) {
			if (null != activityList.get(i)) {
				activityList.get(i).finish();
			}
		}
		activityList.clear();
	}
	/**
	 * activity 销毁
	 * 
	 * @param activity
	 */
	public void setOnActivityDestroy(Activity activity) {

		if (null != activity && null != activityList && activityList.size() > 0) {
			activityList.remove(activity);
			activity.finish();
			activity = null;
			setCurrentActivity();
		}
	}
	/**
	 * activity Resume
	 * 
	 * @param activity
	 */
	public void setOnActivityResume(Activity activity) {
		setNowActivity(activity);
	}
	/**
	 * 返回 activity 总数
	 * 
	 * @return
	 */
	public int getActivitysCount() {
		return activityList.size();
	}
	public void exit() {
		try {
			for (Activity activity : activityList) {
				try {
					if (activity != null)
						activity.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			activityList.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}
}
