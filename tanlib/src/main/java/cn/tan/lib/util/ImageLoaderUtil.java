package cn.tan.lib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import cn.tan.lib.R;

public class ImageLoaderUtil {

    private volatile static ImageLoaderUtil instance;
    private static BitmapFactory.Options decodeOptions;
    protected ImageLoaderUtil() {
    }

    /**
     * Returns singleton class instance
     */
    public static ImageLoaderUtil getInstance() {
        if (instance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (instance == null) {
                    instance = new ImageLoaderUtil();
                }
            }
        }
        return instance;
    }
        public static BitmapFactory.Options getDecodeOptions(){
            if(decodeOptions==null){
                decodeOptions = new BitmapFactory.Options();
                decodeOptions.inPreferQualityOverSpeed = true;
                decodeOptions.inPurgeable = true;
                decodeOptions.inInputShareable = true;
                decodeOptions.inPreferredConfig = Bitmap.Config.RGB_565;
            }
            return  decodeOptions;
        }
    public static DisplayImageOptions getImageOptions(){
        return getImageOptions(0, R.drawable.ic_img_empty_error,0,true,true);
    }
    public static DisplayImageOptions getImageOptions(boolean cacheInMemory, boolean cacheOnDisk){
        return getImageOptions(0, R.drawable.ic_img_empty_error,0,cacheInMemory,cacheOnDisk);
    }
    public static DisplayImageOptions getImageOptions(int round, int res, int durationMillis){
        return getImageOptions(round,res,durationMillis,true,true);
    }
    public static DisplayImageOptions getImageOptions(int round, int res, int durationMillis, boolean cacheOnDisk){
        return getImageOptions(round,res,durationMillis,true,cacheOnDisk);
    }
        public static DisplayImageOptions getImageOptions(int round, int res, int durationMillis, boolean cacheInMemory, boolean cacheOnDisk) {
            DisplayImageOptions.Builder builder=new DisplayImageOptions.Builder();
            builder.showImageOnLoading(res); //设置图片在下载期间显示的图片
            builder.showImageForEmptyUri(res);//设置图片Uri为空或是错误的时候显示的图片
            builder.showImageOnFail(res);   //设置图片加载/解码过程中错误时候显示的图片
            builder.resetViewBeforeLoading(true);  //设置图片在下载前是否重置，复位
            builder.delayBeforeLoading(0);//下载前的延迟时间
            builder.cacheInMemory(cacheInMemory); // //设置下载的图片是否缓存在内存中
            builder.cacheOnDisk(cacheOnDisk); //设置下载的图片是否缓存在SD卡中
//              builder  .preProcessor(...);//设置图片加入缓存前，对bitmap进行设置
//              builder  .postProcessor(...);// 设置显示前的图片，显示后这个图片一直保留在缓存中
//              builder  .extraForDownloader(...);// 设置额外的内容给ImageDownloader
            builder.considerExifParams(true) ; // 是否考虑JPEG图像EXIF参数（旋转，翻转）
            builder.imageScaleType(ImageScaleType.EXACTLY); // default
            builder.bitmapConfig(Bitmap.Config.RGB_565);// 设置图片的解码类型
            builder.decodingOptions(getDecodeOptions());//设置图片的解码配置
            builder.displayer(round > 0 ? new RoundedBitmapDisplayer(round) : new FadeInBitmapDisplayer(durationMillis));
            builder.handler(new Handler());
        return builder.build();
    }

    public void displayImage(String uri, ImageView imageAware, DisplayImageOptions options, ImageLoadingListener imageLoadingListener) {
        ImageLoader.getInstance().displayImage(uri, new ImageViewAware(imageAware, false), options, imageLoadingListener);
    }

    public void displayImage(String uri, ImageView imageAware, DisplayImageOptions options) {
        ImageLoader.getInstance().displayImage(uri, new ImageViewAware(imageAware, false), options);
    }

    public void displayImage(Context context,String uri, ImageView imageAware) {
        ImageLoader.getInstance().displayImage(uri, new ImageViewAware(imageAware, false), getImageOptions(0, R.drawable.ic_img_empty_error,0));
    }
    public void displayImage(String uri, ImageView imageAware,int res) {
        ImageLoader.getInstance().displayImage(uri,  new ImageViewAware(imageAware, false), getImageOptions(0,res,0));
    }
    public void displayImage(String uri, ImageView imageAware) {
        ImageLoader.getInstance().displayImage(uri, new ImageViewAware(imageAware, false), getImageOptions(0, R.drawable.ic_img_empty_error,0));
    }
    public void displayImageRound(Context context,String uri, ImageView imageAware,int res) {
        ImageLoader.getInstance().displayImage(uri, new ImageViewAware(imageAware, false), getImageOptions(180,res,0));
    }
    public String getImageCacheSize(Context context) {
        return FileUtil.getFromFileSize(ImageLoader.getInstance().getDiskCache().getDirectory());
    }

    public void clearImageCache() {
        ToastUtil.showLongToast("清除成功");
        ImageLoader.getInstance().clearDiskCache();
    }
}
