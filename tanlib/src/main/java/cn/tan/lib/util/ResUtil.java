package cn.tan.lib.util;

import java.lang.reflect.Field;

/**
 * Created by Tan on 2015/8/10.
 */
public class ResUtil {
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
