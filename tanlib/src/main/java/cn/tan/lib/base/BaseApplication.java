package cn.tan.lib.base;


import android.app.Application;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import cn.tan.lib.BuildConfig;
import cn.tan.lib.util.FileUtil;

public class BaseApplication extends Application{
	protected static BaseApplication instance;
	public static BaseApplication getInstance() {
		return instance;
	}

	public void onCreate() {
		super.onCreate();
		instance = this;
		initLog();
		initOptimization();
//		LeakCanary.install(this);
		initImageLoader();
	}

	public void initLog(){
		Logger.init(getPackageName()).setMethodCount(2).hideThreadInfo().setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE).setMethodOffset(0);
	}

	public void initImageLoader() {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
		config.memoryCacheExtraOptions(480, 800);
		config.diskCacheExtraOptions(480, 800, null);
		config.taskExecutorForCachedImages(DefaultConfigurationFactory.createExecutor(3, Thread.NORM_PRIORITY - 1, QueueProcessingType.FIFO));
		config.denyCacheImageMultipleSizesInMemory();
		config.memoryCache(new WeakMemoryCache());
//		config.memoryCacheSize(20 * 1024 * 1024);
//		config.memoryCacheSizePercentage(15);//设置缓存占用内存大小
		config.diskCache(new UnlimitedDiskCache(FileUtil.getImageLoaderCachePath()));
//		config.diskCacheSize(5000 * 1024 * 1024);
//		config.diskCacheFileCount(10000);
//		config.diskCacheFileNameGenerator(new HashCodeFileNameGenerator());
		config.imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000));
		config.imageDecoder(new BaseImageDecoder(true));
		config.defaultDisplayImageOptions(DisplayImageOptions.createSimple());
		config.writeDebugLogs();
		ImageLoader.getInstance().init(config.build());
	}

	public void initOptimization() {
		if (BuildConfig.DEBUG) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads()
					.detectDiskWrites()
					.detectNetwork()
					.penaltyLog()
					.build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects()
					.detectLeakedClosableObjects()
					.penaltyLog()
					.penaltyDeath()
					.build());
		}
	}

	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
	}
}
