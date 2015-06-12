package cn.tan.lib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

public class GaussianBlurUtil {
	static {
		System.loadLibrary("RSSupport");
		System.loadLibrary("rsjni");
	}
	private final int DEFAULT_RADIUS = 25;
	private final float DEFAULT_MAX_IMAGE_SIZE = 100;

	private Context context;
	private int radius;
	private float maxImageSize;

	public GaussianBlurUtil(Context context) {
		this.context = context;
		setRadius(DEFAULT_RADIUS);
		setMaxImageSize(DEFAULT_MAX_IMAGE_SIZE);
	}

	public Bitmap render(Bitmap bitmap, boolean scaleDown) {
		RenderScript rs = RenderScript.create(context);

		if (scaleDown) {
			bitmap = scaleDown(bitmap);
		}

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		Allocation inAlloc = Allocation.createFromBitmap(rs, bitmap,
				Allocation.MipmapControl.MIPMAP_NONE,
				Allocation.USAGE_GRAPHICS_TEXTURE);
		Allocation outAlloc = Allocation.createFromBitmap(rs, output);

		ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
				inAlloc.getElement()); // Element.U8_4(rs));
		script.setRadius(getRadius());
		script.setInput(inAlloc);
		script.forEach(outAlloc);
		outAlloc.copyTo(output);

		rs.destroy();

		return output;
	}

	public Bitmap scaleDown(Bitmap input) {
		input=RGB565toARGB888(input);
		float ratio = Math.min((float) getMaxImageSize() / input.getWidth(),
				(float) getMaxImageSize() / input.getHeight());
		int width = Math.round((float) ratio * input.getWidth());
		int height = Math.round((float) ratio * input.getHeight());

		return Bitmap.createScaledBitmap(input, width, height, true);
	}
	public Bitmap RGB565toARGB888(Bitmap img) {
	    int numPixels = img.getWidth()* img.getHeight();
	    int[] pixels = new int[numPixels];
	    //Get JPEG pixels.  Each int is the color values for one pixel.
	    img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
	    //Create a Bitmap of the appropriate format.
	    Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Config.ARGB_8888);
	    //Set RGB pixels.
	    result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
	    return result;
	}
	public int getRadius() {
		if(radius>=25){
			radius=25;
		}else if(radius<=0){
			radius=1;
		}
		return radius;
	}

	public GaussianBlurUtil setRadius(int radius) {
		this.radius = radius;
		return this;
	}

	public float getMaxImageSize() {
		if(maxImageSize>150){
			maxImageSize=150;
		}else if(maxImageSize<=0){
			maxImageSize=1;
		}
		return maxImageSize;
	}

	public void setMaxImageSize(float maxImageSize) {
		this.maxImageSize = maxImageSize;
	}
}
