package cn.tan.lib.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Toast;

import java.io.File;

/**
 * Created by tan on 2015-06-12.
 */
public class IntentUtil {

    /**
     * 请求相册
     */
    public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 1;
    /**
     * 请求相机
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 2;
    /**
     * 请求裁剪
     */
    public static final int REQUEST_CODE_GETIMAGE_BYCROP = 3;
    /**
     * 请求视频
     */
    public static final int REQUEST_CODE_GETVIDEO = 4;
    /**
     * 请求音频
     */
    public static final int REQUEST_CODE_GETAUDIO = 5;

    /**
     * 请求文件
     */
    public static final int REQUEST_CODE_GETFILE = 6;

    /**
     *请求多个相册
     */
    public static final int REQUEST_CODE_MULTI_IMAGE = 7;

    //请求设置权限手机状态权限获取
    public static final int REQUEST_WRITE_SETTINGS_READ_PHONE_STATE=6;

    public static final int ACTION_MANAGE_WRITE_SETTINGS=7;
    /**
     * 启动相机
     *
     * @param context
     */
    public static Intent intent;
    public static void startCamera(Context context, File picture) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
            try{
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picture));
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,100);
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA);
            }catch (Exception e){
                ToastUtil.showLongToast("相机启动失败");
            }
        } else {
            Toast.makeText(context, "没有SD卡", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 启动相册
     *
     * @param context
     */
    public static void startGallery(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);
    }
    public static void startGalleryCrop(Context context){
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCROP);
    }
    /**
     * 系统剪切
     *
     * @param uri
     */
    public static void startPhotoCrop(Uri uri, Context context) {
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", false);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCROP);
    }

    public static void startFile(Context context){
        intent = Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT), "文件选择");
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_GETFILE);
    }
    public static void startVideo(Context context) {
        intent = Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT), "视频选择");
        intent.setType("video/*");
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_GETVIDEO);
    }

    public static void startAudio(Context context) {
        intent= Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT), "音频选择");
        intent.setType("audio/*");
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_GETAUDIO);
    }
//    public static void showPicture(Context context,int maxCount,boolean isShowCamera){
//        Intent intent = new Intent(context, MultiImageSelectorActivity.class);
//        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, isShowCamera);
//        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxCount);
//        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
//        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_MULTI_IMAGE);
//    }
    public static void startRequestPermissions(Context context) {
//        ActivityCompat.requestPermissions((Activity) context,new String[]{Manifest.permission.WRITE_SETTINGS
//                        , Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION},
//                REQUEST_WRITE_SETTINGS_READ_PHONE_STATE);
    }

    public static void startManageWriteSettings(Context context) {
//        intent= new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//        ((Activity) context).startActivityForResult(intent, ACTION_MANAGE_WRITE_SETTINGS);
    }
    /**
     * 回到home，后台运行
     */
    public static void goHome(Context context) {
        Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
        mHomeIntent.addCategory(Intent.CATEGORY_HOME);
        mHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        context.startActivity(mHomeIntent);
    }
    public static void share(Context context, String content, File file) {
        try{
            intent = new Intent(Intent.ACTION_SEND);
            if (file != null) {
                //uri 是图片的地址
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setType("image/*");
                //当用户选择短信时使用sms_body取得文字
                intent.putExtra("sms_body", content);
            } else {
                intent.setType("text/plain");
            }
            intent.putExtra(Intent.EXTRA_TEXT, content);
            //自定义选择框的标题
//        context.startActivity(Intent.createChooser(intent, "邀请好友"));
            //系统默认标题
            context.startActivity(intent);
        }catch (Exception e){
            ToastUtil.showLongToast("您的手机没有可以分享的应用");
            e.printStackTrace();
        }
    }
    public static void showAppScore(Context context){
        try{
            intent=new Intent(Intent.ACTION_VIEW);
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id="+context.getPackageName().toString()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (Exception e){
            ToastUtil.showLongToast("您的手机没有安装应用商店");
        }
    }
    public static void showWiFiSetting(Context context) {
        // 判断手机系统的版本 即API大于10 就是3.0或以上版本
        if (Build.VERSION.SDK_INT > 13) {
            intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings",
                    "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }
    public static void showCallUi(Context context,String tel) {
        try{
            intent= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel.trim()));
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
            ToastUtil.showLongToast("您的手机没有安装电话程序");
        }
    }
    public static void showBrowser(Context context,String url){
        try{
            if(StringUtils.isUrl(url)){
                intent= new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            }else{
                ToastUtil.showLongToast("请输入正确的网址");
            }
        }catch (Exception e){
            ToastUtil.showLongToast("您的手机没有安装浏览器程序");
        }
    }
    public static void showSendMessage(Context context,int tel) {
        intent= new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("smsto:" + tel));
        context.startActivity(intent);
    }
    public static void showMap(Context context,double Lat,double Lot,String sellersName,String address){
       try{
           if(AppUtils.isInstallByread("com.baidu.BaiduMap")){
               intent= Intent.getIntent("intent://map/marker?location="+Lat+","+Lot+"&title="+sellersName+"&content="+address+"&src=biyinjishi.com|"+AppUtils.getAppName(context)+"#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
           }else if(AppUtils.isInstallByread("com.autonavi.minimap")){
               intent = Intent.getIntent("androidamap://viewMap?sourceApplication="+AppUtils.getAppName(context)+"&poiname="+sellersName+"&lat="+Lat+"&lon="+Lot+"&dev=0");
           }else{
               intent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://api.map.baidu.com/marker?location="+Lat+","+Lot+"&title="+sellersName+"&content="+address+"&output=html&src=biyinjishi.com|+"+AppUtils.getAppName(context)+""));
//             Uri uri = Uri.parse("geo:"+Lat+","+Lot+"?q=" + sellersName);
//             intent = new Intent(Intent.ACTION_VIEW,uri);
           }
           context.startActivity(intent);
       }catch (Exception e){
           ToastUtil.showLongToast("您的手机没有安装地图应用");
           e.printStackTrace();
       }
    }
}
