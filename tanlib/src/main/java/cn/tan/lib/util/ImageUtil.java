package cn.tan.lib.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

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
    private static byte[] writeInt(int value)throws IOException{
        byte[] b = new byte[4];
        b[0] = ((byte)(value & 0xFF));
        b[1] = ((byte)((value & 0xFF00) >> 8));
        b[2] = ((byte)((value & 0xFF0000) >> 16));
        b[3] = ((byte)((value & 0xFF000000) >> 24));
        return b;
    }

    private static byte[] write24BitForPixcel(int value)throws IOException {
        byte[] b = new byte[3];
        b[0] = ((byte)(value & 0xFF));
        b[1] = ((byte)((value & 0xFF00) >> 8));
        b[2] = ((byte)((value & 0xFF0000) >> 16));
        return b;
    }
    private static byte[] writeShort(short value)throws IOException {
        byte[] b = new byte[2];
        b[0] = ((byte)(value & 0xFF));
        b[1] = ((byte)((value & 0xFF00) >> 8));
        return b;
    }
}
