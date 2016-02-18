package cn.tan.lib.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import cn.tan.lib.base.BaseApplication;

public final class SpCache {

	private static final String PERFERENCE="SpCache";
	private SharedPreferences mPreference;
	private static SpCache INSTANCE;

	public static SpCache init(Context context, String prefFileName) {
		if (INSTANCE == null) {
			synchronized (SpCache.class) {
				if (INSTANCE == null) {
					INSTANCE = new SpCache(context, prefFileName);
				}
			}
		}
		return INSTANCE;
	}
	public SpCache() {
		this(BaseApplication.getInstance(), PERFERENCE);
	}

	public SpCache(final Context context, String sharedPreferencesName) {
		this.mPreference = context.getApplicationContext().getSharedPreferences(PERFERENCE, Context.MODE_PRIVATE);
	}

	/**
	 * 清空数据
	 */
	public static void reset(final Context ctx) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
		edit.clear();
		edit.commit();
	}

	public static String getString(String key, String defValue) {
		if (BaseApplication.getInstance() != null) {
			try {
//				return DESUtil.decrypt(PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance()).getString(key, defValue), DESUtil.KEY_NAME);
				return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance()).getString(key, defValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return defValue;
	}

	public static long getLong(String key, long defValue) {
		if (BaseApplication.getInstance() != null) {
			return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance()).getLong(key, defValue);
		}
		return defValue;
	}

	public static float getFloat(String key, float defValue) {
		if (BaseApplication.getInstance() != null) {
			return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance()).getFloat(key, defValue);
		}
		return defValue;
	}

	public static void put(String key, String value) {
		putString(key, value);
	}

	public static void put(String key, int value) {
		putInt(key, value);
	}

	public static void put(String key, float value) {
		putFloat(key, value);
	}

	public static void put(String key, boolean value) {
		putBoolean(key, value);
	}

	public static void putFloat(String key, float value) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
			Editor editor = sharedPreferences.edit();
			editor.putFloat(key, value);
			editor.commit();
		}
	}

	public static SharedPreferences getPreferences() {
		return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
	}

	public static int getInt(String key, int defValue) {
		if (BaseApplication.getInstance() != null) {
			return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance()).getInt(key, defValue);
		}
		return defValue;
	}

	public static boolean getBoolean(String key, boolean defValue) {
		if (BaseApplication.getInstance() != null) {
			return PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance()).getBoolean(key, defValue);
		}
		return defValue;
	}

	public static void putStringProcess(String key, String value) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
			Editor editor = sharedPreferences.edit();
			editor.putString(key, value);
			editor.commit();
		}
	}

	public static void putIntProcess(String key, int value) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
			Editor editor = sharedPreferences.edit();
			editor.putInt(key, value);
			editor.commit();
		}
	}

	public static int getIntProcess(String key, int defValue) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
			return sharedPreferences.getInt(key, defValue);
		}
		return defValue;
	}

	public static void putLongProcess(String key, long value) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
			Editor editor = sharedPreferences.edit();
			editor.putLong(key, value);
			editor.commit();
		}
	}

	public static long getLongProcess(String key, long defValue) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
			return sharedPreferences.getLong(key, defValue);
		}
		return defValue;
	}

	public static String getStringProcess(String key, String defValue) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
			return sharedPreferences.getString(key, defValue);
		}
		return defValue;
	}

	public static boolean hasString(String key) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
			return sharedPreferences.contains(key);
		}
		return false;
	}
	
	public static void putString(String key, String value) {
		if (BaseApplication.getInstance() != null) {
			try {
				SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
				Editor editor = sharedPreferences.edit();
//				editor.putString(key, DESUtil.encrypt(value, DESUtil.KEY_NAME));
				editor.commit();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void putLong(String key, long value) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
			Editor editor = sharedPreferences.edit();
			editor.putLong(key, value);
			editor.commit();
		}
	}

	public static void putBoolean(String key, boolean value) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
			Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, value);
			editor.commit();
		}
	}

	public static void putInt(String key, int value) {
		if (BaseApplication.getInstance() != null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BaseApplication.getInstance());
			Editor editor = sharedPreferences.edit();
			editor.putInt(key, value);
			editor.commit();
		}
	}

	public static void remove(String... keys) {
		if (keys != null && BaseApplication.getInstance() != null) {

			SharedPreferences sharedPreferences = BaseApplication.getInstance().getSharedPreferences("preference_mu", Context.MODE_MULTI_PROCESS);
			Editor editor = sharedPreferences.edit();
			for (String key : keys) {
				editor.remove(key);
			}
			editor.commit();
		}
	}

	public String get(String key, String defValue) {
		return mPreference.getString(key, defValue);
	}

	public boolean get(String key, boolean defValue) {
		return mPreference.getBoolean(key, defValue);
	}

	public int get(String key, int defValue) {
		return mPreference.getInt(key, defValue);
	}

	public float get(String key, float defValue) {
		return mPreference.getFloat(key, defValue);
	}
}
