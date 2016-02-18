package cn.tan.lib.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Time;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.UUID;

import cn.tan.lib.base.BaseApplication;

/**
 * Created by tan on 2015-06-24.
 */
public class FileUtil {
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB的double值
    public static final String DownLoadAppName = "YinBiSanJia.apk";

    /**
     * MB 单位B
     */
    private static int MB = 1024 * 1024;

    public static boolean deleteFile(String path) {
        try {
            if (TextUtils.isEmpty(path)) {
                return true;
            }
            File file = new File(path);
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                return file.delete();
            }
            if (!file.isDirectory()) {
                return false;
            }
            for (File f : file.listFiles()) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
            return file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean IsFileExists(File f) {
        try {
            if (f==null||!f.exists()||f.length()==0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件夹内文件数量
     *
     * @param path
     * @return
     */
    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    /**
     * 获取文件夹大小
     *
     * @param file
     * @return
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFilePtah() {
        String sdCardPath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) && !SDCardUtils.isSDCardEnable())
            sdCardPath = BaseApplication.getInstance().getExternalCacheDir().toString();
        else
            sdCardPath = BaseApplication.getInstance().getCacheDir().toString();
        return sdCardPath;
    }

    public static File getAppDebugFilePath() {
        File file = null;
        file = new File(getFilePtah() + "/debug/");
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static String getAppUpdatePath() {
        File file = null;
        file = new File(getDownLoadPath(), DownLoadAppName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        return file.getPath();
    }

    public static File getDownLoadPath() {
        File file = null;
        file = new File(getFilePtah() + "/downloader/");
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static File getCameraPath(Context context) {
        return getCameraPath(context, null);
    }

    public static File getCameraPath(Context context, String photoName) {
        File picture = null;
        try {
            String fodlerPath = createPath(getFilePtah() + File.separator + "Img");
            if (StringUtils.isEmpty(photoName)) {
                photoName = getPhotoName("jpg");
            }
            picture = new File(fodlerPath + File.separator + photoName);
            picture.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "相机路径初始化失败，请重新再试", Toast.LENGTH_SHORT).show();
            return null;
        }
        return picture;
    }

    public static String getPhotoName(String imgType) {
        String mCurrentSysTime = null;
        Time time = new Time("GMT+8");
        time.setToNow();
        int year = time.year;
        int month = time.month;
        int day = time.monthDay;
        int minute = time.minute;
        int hour = time.hour;
        int sec = time.second;
        mCurrentSysTime = year + "" + month + "" + day + "" + hour + "" + minute + "" + sec + "" + UUID.randomUUID().toString().replace("-", "") + "." + imgType;
        return mCurrentSysTime;
    }

    public static File getImageLoaderCachePath(Context context) {
        File file = null;
        file = new File(getFilePtah() + File.separator + "UIImage");
        if (!file.exists())
            file.mkdirs();
        return file;
    }

    public static String getFromFileSize(File file) {
        return FormetFileSize(getFolderSize(file));
    }

    /**
     * 转换文件大小
     */
    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0.0M";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /*
     * 创建目录
     */
    public static String createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }
}
