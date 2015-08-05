package cn.tan.lib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cn.tan.lib.R;


public class ImageLoaderUtil {

    private volatile static ImageLoaderUtil instance;

    protected ImageLoaderUtil() {
    }

    /**
     * Returns singleton class instance
     */
    public static ImageLoaderUtil getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoaderUtil();
                }
            }
        }
        return instance;
    }

    public static DisplayImageOptions getImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_stub) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.ic_empty)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.ic_error)   //设置图片加载/解码过程中错误时候显示的图片
                .resetViewBeforeLoading(false)  //设置图片在下载前是否重置，复位
                .delayBeforeLoading(0)//下载前的延迟时间
                .cacheInMemory(true) // //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) //设置下载的图片是否缓存在SD卡中
//                .preProcessor(...)//设置图片加入缓存前，对bitmap进行设置
//                .postProcessor(...)// 设置显示前的图片，显示后这个图片一直保留在缓存中
//                .extraForDownloader(...)// 设置额外的内容给ImageDownloader
                .considerExifParams(true)  // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY) // default
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .decodingOptions(new BitmapFactory.Options())//设置图片的解码配置
//                .displayer(new SimpleBitmapDisplayer()) // 淡入图片
                .handler(new Handler())
                .build();
        return options;
    }
    public void displayImage(String uri, ImageView imageAware, DisplayImageOptions options, ImageLoadingListener imageLoadingListener) {
        if (!uri.equals(imageAware.getTag())) {
            imageAware.setTag(uri);
            ImageLoader.getInstance().displayImage(uri, imageAware, options, imageLoadingListener);
        }
    }

    public void displayImage(String uri, ImageView imageAware, DisplayImageOptions options) {
        if (!uri.equals(imageAware.getTag())) {
            ImageLoader.getInstance().displayImage(uri, imageAware, options);
            imageAware.setTag(uri);
        }
    }

    public void displayImage(String uri, ImageView imageAware) {
        if (!uri.equals(imageAware.getTag())) {
            ImageLoader.getInstance().displayImage(uri, imageAware);
            imageAware.setTag(uri);
        }
    }
}
