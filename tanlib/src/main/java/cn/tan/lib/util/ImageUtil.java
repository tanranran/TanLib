package cn.tan.lib.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageUtil {

    /*@param view，当前想要创建截图的View
     * @param width，设置截图的宽度
     * @param height，设置截图的高度
     * @param scroll，如果为真则从View的当前滚动位置开始绘制截图
     * @param config，Bitmap的质量，比如ARGB_8888等
     *
     * @return 截图的Bitmap
     */
    public static Bitmap capture(View view, float width, float height, boolean scroll, Bitmap.Config config) {
        if (!view.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }

        Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, config);
        bitmap.eraseColor(Color.WHITE);

        Canvas canvas = new Canvas(bitmap);
        int left = view.getLeft();
        int top = view.getTop();
        if (scroll) {
            left = view.getScrollX();
            top = view.getScrollY();
        }
        int status = canvas.save();
        canvas.translate(-left, -top);

        float scale = width / view.getWidth();
        canvas.scale(scale, scale, left, top);

        view.draw(canvas);
        canvas.restoreToCount(status);

        Paint alphaPaint = new Paint();
        alphaPaint.setColor(Color.TRANSPARENT);

        canvas.drawRect(0f, 0f, 1f, height, alphaPaint);
        canvas.drawRect(width - 1f, 0f, width, height, alphaPaint);
        canvas.drawRect(0f, 0f, width, 1f, alphaPaint);
        canvas.drawRect(0f, height - 1f, width, height, alphaPaint);
        canvas.setBitmap(null);

        return bitmap;
    }

    public static boolean save(Bitmap orgBitmap, String filePath) {
        if (orgBitmap == null) {
            return false;
        }
        if (filePath == null) {
            return false;
        }
        boolean isSaveSuccess = true;
        int width = orgBitmap.getWidth();
        int height = orgBitmap.getHeight();
        int dummySize = 0;
        byte[] dummyBytesPerRow = null;
        boolean hasDummy = false;
        if (isBmpWidth4Times(width)) {
            hasDummy = true;
            dummySize = 4 - width % 4;
            dummyBytesPerRow = new byte[dummySize * 3];
            for (int i = 0; i < dummyBytesPerRow.length; i++) {
                dummyBytesPerRow[i] = -1;
            }
        }
        int[] pixels = new int[width * height];
        int imageSize = pixels.length * 3 + height * dummySize * 3;
        int imageDataOffset = 54;
        int fileSize = imageSize + imageDataOffset;
        orgBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        ByteBuffer buffer = ByteBuffer.allocate(fileSize);
        try {
            buffer.put((byte) 66);
            buffer.put((byte) 77);
            buffer.put(writeInt(fileSize));
            buffer.put(writeShort((short) 0));
            buffer.put(writeShort((short) 0));
            buffer.put(writeInt(imageDataOffset));
            buffer.put(writeInt(40));
            buffer.put(writeInt(width));
            buffer.put(writeInt(height));
            buffer.put(writeShort((short) 1));
            buffer.put(writeShort((short) 24));
            buffer.put(writeInt(0));
            buffer.put(writeInt(imageSize));
            buffer.put(writeInt(0));
            buffer.put(writeInt(0));
            buffer.put(writeInt(0));
            buffer.put(writeInt(0));
            int row = height;
            int col = width;
            int startPosition = 0;
            int endPosition = 0;
            while (row > 0) {
                startPosition = (row - 1) * col;
                endPosition = row * col;
                for (int i = startPosition; i < endPosition; i++) {
                    buffer.put(write24BitForPixcel(pixels[i]));
                    if ((hasDummy) &&
                            (isBitmapWidthLastPixcel(width, i))) {
                        buffer.put(dummyBytesPerRow);
                    }
                }
                row--;
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(buffer.array());
            fos.close();
        } catch (IOException e1) {
            e1.printStackTrace();
            isSaveSuccess = false;
        }
        return isSaveSuccess;
    }

    private static boolean isBitmapWidthLastPixcel(int width, int i) {
        return (i > 0) && (i % (width - 1) == 0);
    }

    private static boolean isBmpWidth4Times(int width) {
        return width % 4 > 0;
    }

    private static byte[] writeInt(int value) throws IOException {
        byte[] b = new byte[4];
        b[0] = ((byte) (value & 0xFF));
        b[1] = ((byte) ((value & 0xFF00) >> 8));
        b[2] = ((byte) ((value & 0xFF0000) >> 16));
        b[3] = ((byte) ((value & 0xFF000000) >> 24));
        return b;
    }

    private static byte[] write24BitForPixcel(int value) throws IOException {
        byte[] b = new byte[3];
        b[0] = ((byte) (value & 0xFF));
        b[1] = ((byte) ((value & 0xFF00) >> 8));
        b[2] = ((byte) ((value & 0xFF0000) >> 16));
        return b;
    }

    private static byte[] writeShort(short value) throws IOException {
        byte[] b = new byte[2];
        b[0] = ((byte) (value & 0xFF));
        b[1] = ((byte) ((value & 0xFF00) >> 8));
        return b;
    }

    /**
     * 从文件中获取图片
     *
     * @param imgPath 图片的路径
     * @return
     */
    public static Bitmap getBitmapFromFile(String imgPath, Context context) {
        Bitmap bitmap;
        // 获得屏幕高宽
        int w = ScreenUtils.getScreenWidth(context);
        int h = ScreenUtils.getScreenHeight(context);
        int degree = readPictureDegree(imgPath);// 检测图片是否旋转
        if (w == 0) {
            if (h == 0) {
                bitmap = getResizeBitmapByPath(imgPath, 480f, 800f, Bitmap.Config.RGB_565);
            } else {
                bitmap = getResizeBitmapByPath(imgPath, 480f, (float) h, Bitmap.Config.RGB_565);
            }

        } else {
            if (h == 0) {
                bitmap = getResizeBitmapByPath(imgPath, (float) w, 800f, Bitmap.Config.RGB_565);
            } else {
                bitmap = getResizeBitmapByPath(imgPath, (float) w, (float) h, Bitmap.Config.RGB_565);
            }
        }

        if (degree != 0) {// 旋转处理
            bitmap = rotaingImageView(degree, bitmap);
        }
        return bitmap;
    }

    public static Bitmap getResizeBitmapByPath(String srcPath, Float width, Float height, Bitmap.Config config) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth; // 原始宽
        int h = newOpts.outHeight; // 原始高
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float ww = width;// 输出宽
        float hh = height;// 输出高
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        // 此算法凑合
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        newOpts.inPreferredConfig = config;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compZoom(Bitmap image, float hh, float ww) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        // float hh = 800f;//这里设置高度为800f
        // float ww = 480f;//这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (w / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (h / hh);
        } else {
            be = (int) (h / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return bitmap;// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩算法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 获取图片路径 2014年8月12日
     *
     * @param uri
     * @return path
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getAbsoluteImagePath(final Uri uri, final Context context) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority()))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /*
     * Bitmap转换成byte[]
	 *
	 * @param Bitmap
	 */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        return baos.toByteArray();
    }

    /*
     * byte[]转换成Bitmap
     *
     * @param byte[]
     */
    public static Bitmap bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 获取图片的类型信息
     *
     * @param bytes 2~8 byte at beginning of the image file
     * @return image mimetype or null if the file is not image
     */
    public static String getImageType(byte[] bytes) {
        if (isJPEG(bytes)) {
            return "image/jpeg";
        }
        if (isGIF(bytes)) {
            return "image/gif";
        }
        if (isPNG(bytes)) {
            return "image/png";
        }
        if (isBMP(bytes)) {
            return "application/x-bmp";
        }
        return null;
    }

    private static boolean isJPEG(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == (byte) 0xFF) && (b[1] == (byte) 0xD8);
    }

    private static boolean isGIF(byte[] b) {
        if (b.length < 6) {
            return false;
        }
        return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'
                && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
    }

    private static boolean isPNG(byte[] b) {
        if (b.length < 8) {
            return false;
        }
        return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78
                && b[3] == (byte) 71 && b[4] == (byte) 13 && b[5] == (byte) 10
                && b[6] == (byte) 26 && b[7] == (byte) 10);
    }

    private static boolean isBMP(byte[] b) {
        if (b.length < 2) {
            return false;
        }
        return (b[0] == 0x42) && (b[1] == 0x4d);
    }

    /**
     * 按正方形裁切图片
     */
    public static Bitmap ImageCrop(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
        int retX = w > h ? (w - h) / 2 : 0;// 基于原图，取正方形左上角x坐标
        int retY = w > h ? 0 : (h - w) / 2;
        return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
    }

    /**
     * 扫描、刷新相册
     */
    public static void scanPhotos(String filePath, Context context) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    private static void recycleImageViewBitMap(ImageView imageView) {
        if (imageView != null) {
            BitmapDrawable bd1 = (BitmapDrawable) imageView.getDrawable();
            BitmapDrawable bd2 = (BitmapDrawable) imageView.getBackground();
            rceycleBitmapDrawable(bd1);
            rceycleBitmapDrawable(bd2);
        }
    }

    private static void rceycleBitmapDrawable(BitmapDrawable bitmapDrawable) {
        if (bitmapDrawable != null) {
            Bitmap bitmap = bitmapDrawable.getBitmap();
            rceycleBitmap(bitmap);
        }
        bitmapDrawable = null;
    }

    private static void rceycleBitmap(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * RGB值转Bitmap
     *
     * @param rgb
     * @param width
     * @param height
     * @return
     */
    private Bitmap createColorBitmap(String rgb, int width, int height) {
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int color = Color.parseColor(rgb);
        bmp.eraseColor(color);
        return bmp;
    }

    /**
     * Color值转Bitmap
     *
     * @param color
     * @param width
     * @param height
     * @return
     */
    private Bitmap createColorBitmap(int color, int width, int height) {
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmp.eraseColor(color);
        return bmp;
    }
}
