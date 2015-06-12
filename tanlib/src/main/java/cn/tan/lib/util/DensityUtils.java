package cn.tan.lib.util;

import android.content.Context;
import android.util.TypedValue;

public class DensityUtils {
	 private DensityUtils()  
	    {  
	        /* cannot be instantiated */  
	        throw new UnsupportedOperationException("cannot be instantiated");  
	    }  
	  
	 /**
		 * 将px值转换为dip或dp值，保证尺寸大小不变
		 * 
		 * @param pxValue
		 * @param scale
		 *            （DisplayMetrics类中属性density）
		 * @return
		 */
		public static int px2dp(Context context, float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}

		/**
		 * 将dip或dp值转换为px值，保证尺寸大小不变
		 * 
		 * @param dipValue
		 * @param scale
		 *            （DisplayMetrics类中属性density）
		 * @return
		 */
		public static int dp2px(Context context, float dipValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dipValue * scale + 0.5f);
		}
	  
	    /** 
	     * sp转px 
	     *  
	     * @param context 
	     * @param val 
	     * @return 
	     */  
	    public static int sp2px(Context context, float spVal)  
	    {  
	        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,  
	                spVal, context.getResources().getDisplayMetrics());  
	    }  
	  
	    
	    /** 
	     * px转sp 
	     *  
	     * @param fontScale 
	     * @param pxVal 
	     * @return 
	     */  
	    public static float px2sp(Context context, float pxVal)  
	    {  
	        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);  
	    }  
	  
}
