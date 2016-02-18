package cn.tan.lib.util;


import com.orhanobut.logger.Logger;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by tan on 2015-06-29.
 */
public class DateUtils {

    private static final String TAG = "DateUtil";

    public static final String yyyymmddhhmm0="yyyy-MM-dd";
    public static final String yyyymmddhhmm1="yyyy-MM-dd HH:mm";
    public static final String yyyymmddhhmmss="yyyyMMddHHmmss";
    public static final String yyyymmddhhmmss2="yyyy-MM-dd-HH:mm";
    public static Calendar calendar = null;

    public static DateFormat dateFormat = null;

    public static Date date = null;

    public static String getTime(Date date,String pattern) {
        dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    /**
     * 功能描述：格式化日期
     *
     * @param dateStr String 字符型日期
     * @param format  String 格式
     * @return Date 日期
     */
    public static Date parseDate(String dateStr, String format) {
        try {
            dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 功能描述：返回年份
     *
     * @param date Date 日期
     * @return 返回年份
     */
    public static int getYear(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 功能描述：返回月份
     *
     * @param date Date 日期
     * @return 返回月份
     */
    public static int getMonth(Date date) {
        calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 功能描述：返回日份
     *
     * @param date Date 日期
     * @return 返回日份
     */
    public static int getDay(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 功能描述：返回小时
     *
     * @param date 日期
     * @return 返回小时
     */
    public static int getHour(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 功能描述：返回分钟
     *
     * @param date 日期
     * @return 返回分钟
     */
    public static int getMinute(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 返回秒钟
     *
     * @param date Date 日期
     * @return 返回秒钟
     */
    public static int getSecond(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 功能描述：返回毫秒
     *
     * @param date 日期
     * @return 返回毫秒
     */
    public static long getMillis(Date date) {
        calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getTimeInMillis();
    }

    /**
     * 获得今天星期
     *
     * @return
     */
    public static String getWeekDayString(Date date) {
        String week = "周一";
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek){
            case  Calendar.MONDAY:
                week="周一";
                break;
            case  Calendar.TUESDAY :
                week="周二";
                break;
            case  Calendar.WEDNESDAY :
                week="周三";
                break;
            case  Calendar.THURSDAY:
                week="周四";
                break;
            case  Calendar.FRIDAY :
                week="周五";
                break;
            case  Calendar.SATURDAY :
                week="周六";
                break;
            case  Calendar.SUNDAY :
                week="周日";
                break;
        }
        return week;
    }
    public static Date strToDate(int year,int month,int day) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ParsePosition pos = new ParsePosition(0);
        return formatter.parse(year+"-"+month+"-"+day, pos);
    }
    /**
     * 毫秒转日期Date
     *
     * @param str
     * @return
     */
    public static Date getDateTimeByMillisecond(String str) {
        Date date = new Date(Long.valueOf(str));
        return date;
    }

    /*
     * 比较两个日期大小 true 为大， false 为小
     */
    public static boolean dateCompare(Date dat1, Date dat2) {
        boolean dateComPareFlag = true;
        if (dat2.compareTo(dat1) != 1) {
            dateComPareFlag = false; //
        }
        return dateComPareFlag;
    }

    /**
     * 获得今天星期几
     *
     * @return
     */
    public static int getWeekDayInt() {
        int weekInt = 0;
        final int dayNames[] = {0, 1, 2, 3, 4, 5, 6};
        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        weekInt = dayNames[dayOfWeek - 1];
        return weekInt;
    }

    /**
     * 根据生日计算周岁年龄
     *
     * @return
     * @throws Exception
     */
    public static int getDuration(Date date) {
        int month = getMonth(date);
        int day = getDay(date);
        int year = getYear(date);
        month--; // following the 0-based rule
        Calendar cal = new GregorianCalendar(year, month, day);

        Date today = new Date();

        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        int intYear = Integer.parseInt(sdfYear.format(today));

        SimpleDateFormat sdfMonth = new SimpleDateFormat("MM");
        int intMonth = Integer.parseInt(sdfMonth.format(today));
        intMonth--; // following the 0-based rule

        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        int intDay = Integer.parseInt(sdfDay.format(today));

        Calendar now = new GregorianCalendar(intYear, intMonth, intDay);

        int yyyy = intYear - year;

        int[] months = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        int factor = 0;

        int mm = 0; // month duration

        int dd = 0; // day duration

        if ((month > intMonth) || (month == intMonth && day > intDay)) {
            factor = -1;
            yyyy += factor;
        }

        try {
            if (month > 12) {
                throw new Exception("Invalid input month");
            } else if (day > months[month]) {
                throw new Exception("Invalid input day");
            } else if (yyyy < 0) {
                throw new Exception("Invalid input date");
            }
        } catch (Exception e) {
            Logger.e(TAG, e.toString());
        }
        if (factor == 0) {
            // compute for days in between the given and the current date
            dd = now.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);

            if ((intYear % 4) == 0) {
                months[1]++; // increment the days in February by 1
            }

            for (int i = month; i <= intMonth; i++) {
                if (dd >= months[i]) {
                    dd -= months[i];
                    mm++;
                }
            }

            if (mm >= 12) {
                yyyy += (mm / 12);
                mm %= 12;
            }
        } else { // if the given date is greater than the current date
            intYear--; // derive previous year

            Calendar prev = new GregorianCalendar(intYear, 11, 31);

            dd = (prev.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR)) + now.get(Calendar.DAY_OF_YEAR);

            if ((intYear % 4) == 0) {
                months[1]++; // increment the days in February by 1
            }

            for (int i = month; i <= 11; i++) {
                if (dd >= months[i]) {
                    dd -= months[i];
                    mm++;
                }
            }
            intYear++; // set the value back to the current year

            if ((intYear % 4) == 0) {
                months[1]++; // increment the days in February by 1
            }

            for (int i = 0; i <= intMonth; i++) {
                if (dd >= months[i]) {
                    dd -= months[i];
                    mm++;
                }
            }

            if (mm >= 12) {
                yyyy += (mm / 12);
                mm %= 12;
            }
        }

        return yyyy;
    }
}
