package cn.tan.lib.base;


import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

import cn.tan.lib.util.FileUtil;

public class BaseApplication extends Application{
	protected static BaseApplication instance;
	private boolean isDebug=true;
	public static BaseApplication getInstance() {
		return instance;
	}
	public void onCreate() {
		super.onCreate();
		instance = this;
		initLog();
		initImageLoader();
		initStrictMode();
	}
	public void initImageLoader() {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
//		config.memoryCacheExtraOptions(480, 800);
//		config.diskCacheExtraOptions(480, 800, null);
		config.taskExecutorForCachedImages(DefaultConfigurationFactory.createExecutor(3, Thread.MAX_PRIORITY, QueueProcessingType.FIFO));
		config.denyCacheImageMultipleSizesInMemory();
//		config.memoryCache(new LruMemoryCache(10 * 1024 * 1024));
//		config.memoryCacheSize(20 * 1024 * 1024);
		config.memoryCacheSizePercentage(30);//设置缓存占用内存大小
		config.diskCache(new UnlimitedDiskCache(FileUtil.getImageLoaderCachePath(this)));
//		config.diskCacheSize(5000 * 1024 * 1024);
//		config.diskCacheFileCount(10000);
		config.diskCacheFileNameGenerator(new HashCodeFileNameGenerator());
//		config.imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000));
		config.imageDecoder(new BaseImageDecoder(true));
		config.defaultDisplayImageOptions(DisplayImageOptions.createSimple());
		if(isDebug){
			config.writeDebugLogs();
		}
		ImageLoader.getInstance().init(config.build());
	}
	public void initLog(){
		Logger.init(getPackageName()).setMethodCount(2).hideThreadInfo().setLogLevel(isDebug ? LogLevel.FULL : LogLevel.NONE).setMethodOffset(0);
	}
	public void initStrictMode(){
		if (isDebug && Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {

			// 主要用于发现在UI线程中是否有读写磁盘的操作，是否有网络操作，以及检查UI线程中调用的自定义代码是否执行得比较慢。
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll()
					.penaltyLog()
					.penaltyDialog()//触发违规时，显示对违规信息对话框。
					.build());

			//主要用于发现内存问题，比如 Activity内存泄露， SQL 对象内存泄露， 资源未释放，能够限定某个类的最大对象数。
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll()
					.penaltyLog()
					.build());
		}
	}
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
	}
}
