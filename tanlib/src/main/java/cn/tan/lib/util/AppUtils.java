package cn.tan.lib.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** 
 * 跟App相关的辅助类 
 *  
 *  
 *  
 */  
public class AppUtils  
{  
  
    private AppUtils()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");  
  
    }
    /**
     * 创建快捷方式
     *
     * @param cxt
     *            Context
     * @param icon
     *            快捷方式图标
     * @param title
     *            快捷方式标题
     * @param cls
     *            要启动的类
     */
    public void createDeskShortCut(Context cxt, int icon, String title,
                                   Class<?> cls) {
        // 创建快捷方式的Intent
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutIntent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        // 快捷图片
        Parcelable ico = Intent.ShortcutIconResource.fromContext(
                cxt.getApplicationContext(), icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ico);
        Intent intent = new Intent(cxt, cls);
        // 下面两个属性是为了当应用程序卸载时桌面上的快捷方式会删除
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        // 点击快捷图片，运行的程序主入口
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播。OK
        cxt.sendBroadcast(shortcutIntent);
    }
    /** 
     * 获取应用程序名称 
     */  
    public static String getAppName(Context context)  
    {  
        try  
        {  
            PackageManager packageManager = context.getPackageManager();  
            PackageInfo packageInfo = packageManager.getPackageInfo(  
                    context.getPackageName(), 0);  
            int labelRes = packageInfo.applicationInfo.labelRes;  
            return context.getResources().getString(labelRes);  
        } catch (NameNotFoundException e)  
        {  
            e.printStackTrace();
        }  
        return null;  
    }  
  
    /** 
     * [获取应用程序版本名称信息] 
     *  
     * @param context 
     * @return 当前应用的版本名称 
     */  
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();  
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();  
        }  
        return null;  
    }
    public static int getVersionCode(Context context){
        int versionCode=1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pinfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            versionCode = pinfo.versionCode;
        } catch (NameNotFoundException e) {
        }
        return  versionCode;
    }
    /**
     * 获取当前系统的android版本号
     * @return
     */
    public static String getSystemVsersion(){
        return "Product Model: " + Build.MODEL + ","
                + Build.VERSION.SDK + ","
                + Build.VERSION.RELEASE;
    }
    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    /**
     * 判断手机是否处理睡眠
     *
     * @param context
     * @return
     */
    public static boolean isSleeping(Context context) {
        KeyguardManager kgMgr = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = kgMgr.inKeyguardRestrictedInputMode();
        Logger.d(isSleeping ? "手机睡眠中.." : "手机未睡眠...");
        return isSleeping;
    }

    /**
     * 判断是否为手机
     *
     * @param context
     * @return
     * @author wangjie
     */
    public static boolean isPhone(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int type = telephony.getPhoneType();
        if (type == TelephonyManager.PHONE_TYPE_NONE) {
            Logger.i("Current device is Tablet!");
            return false;
        } else {
            Logger.i("Current device is phone!");
            return true;
        }
    }

    /**
     * 调用系统安装应用
     */
    public static boolean install(Context context, File file) {
        try{
            if (file == null || !file.exists() || !file.isFile()) {
                ToastUtil.showLongToast(context,"安装包损坏或者丢失");
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e){
            ToastUtil.showLongToast("安装失败");
        }
        return true;
    }
    /**
     * 调用系统卸载应用
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }
    /**
     * 打开设置网络界面
     * */
    public static void getNetworkSetting(final Context context) {
        // 提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络设置提示")
                .setMessage("请检查你的网络连接")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        IntentUtil.showWiFiSetting(context);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 启动应用
     */
    public static boolean startAppByPackageName(Context context, String packageName) {
        return startAppByPackageName(context, packageName, null);
    }
    /**
     * 启动应用
     */
    public static boolean startAppByPackageName(Context context, String packageName, Map<String, String> param) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(packageName, 0);
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                resolveIntent.setPackage(pi.packageName);
            }

            List<ResolveInfo> apps = context.getPackageManager().queryIntentActivities(resolveIntent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String packageName1 = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);

                ComponentName cn = new ComponentName(packageName1, className);

                intent.setComponent(cn);
                if (param != null) {
                    for (Map.Entry<String, String> en : param.entrySet()) {
                        intent.putExtra(en.getKey(), en.getValue());
                    }
                }
                context.startActivity(intent);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "启动失败",
                    Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * 获取设备id（IMEI）
     *
     * @param context
     * @return
     * @author wangjie
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static String getDeviceIMEI(Context context) {
        String deviceId;
        if (isPhone(context)) {
            TelephonyManager telephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephony.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        }
        Logger.d("当前设备IMEI码: " + deviceId);
        return deviceId;
    }

    /**
     * 获取设备mac地址
     *
     * @param context
     * @return
     * @author wangjie
     */
    public static String getMacAddress(Context context) {
        String macAddress;
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        macAddress = info.getMacAddress();
        Logger.d("当前mac地址: " + (null == macAddress ? "null" : macAddress));
        if (null == macAddress) {
            return "";
        }
        macAddress = macAddress.replace(":", "");
        return macAddress;
    }

    //获得独一无二的Psuedo ID
    public static String getUniquePsuedoID() {
        String serial = null;
        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
                Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 位
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
    /**
     * 获取对应包名的应用的签名串
     *
     * @param context
     * @param paramString
     *            应用程序包名
     * @return
     */
    public static String getSign(Context context, String paramString) {
        if (context == null) {
            throw new IllegalAccessError("Context is null!!!");
        }
        Signature[] arrayOfSignature = getRawSignature(context, paramString);
        if ((arrayOfSignature == null) || (arrayOfSignature.length == 0)) {
            Logger.d("SIGN", "signs is null");
            return null;
        }
        int i = arrayOfSignature.length;
        if (i > 0) {
            return getMessageDigest(arrayOfSignature[0].toByteArray());
        }
        return null;
    }

    private static Signature[] getRawSignature(Context paramContext,
                                               String paramString) {
        if ((paramString == null) || (paramString.length() == 0)) {
            Logger.d("SIGN", "getSignature, packageName is null");
            return null;
        }
        PackageManager localPackageManager = paramContext.getPackageManager();
        PackageInfo localPackageInfo;
        try {
            localPackageInfo = localPackageManager.getPackageInfo(paramString,
                    64);
            if (localPackageInfo == null) {
                Logger.d("SIGN", "info is null, packageName = " + paramString);
                return null;
            }
        } catch (NameNotFoundException localNameNotFoundException) {
            Logger.d("SIGN", "NameNotFoundException");
            return null;
        }
        return localPackageInfo.signatures;
    }

    private static final String getMessageDigest(byte[] paramArrayOfByte) {
        char[] arrayOfChar1 = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98,
                99, 100, 101, 102 };
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            int i = arrayOfByte.length;
            char[] arrayOfChar2 = new char[i * 2];
            int j = 0;
            int k = 0;
            while (true) {
                if (j >= i)
                    return new String(arrayOfChar2);
                int m = arrayOfByte[j];
                int n = k + 1;
                arrayOfChar2[k] = arrayOfChar1[(0xF & m >>> 4)];
                k = n + 1;
                arrayOfChar2[n] = arrayOfChar1[(m & 0xF)];
                j++;
            }
        } catch (Exception localException) {
        }
        return null;
    }
    public static boolean isInstallByread(String packageName)
    {
        return new File("/data/data/" + packageName).exists();
    }
}