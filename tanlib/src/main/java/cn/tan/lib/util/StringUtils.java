package cn.tan.lib.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tan on 2015-06-29.
 */
public class StringUtils {


    /**
     * 过滤电话号码
     *
     * @param mobile
     */
    public static String replaceMobile(String mobile) {
        if (mobile == null) {
            return null;
        }
        mobile = replace(" ", "", mobile);
        mobile = replace("+", "", mobile);
        mobile = replace("-", "", mobile);
        if (mobile.startsWith("01")) {
            if (!mobile.startsWith("010")) {
                mobile = mobile.substring(1, mobile.length());
            }
        }
        if (mobile.startsWith("86")) {
            mobile = mobile.substring(2, mobile.length());
        }
        return mobile;
    }
    public static String phonePrivate(String str){
        try{
            return str.substring(0,str.length()-(str.substring(3)).length())+"****"+str.substring(7);
        }catch (Exception e){
            e.printStackTrace();
            return str;
        }
    }
    public static boolean apkValidate(String url) {
        String regEx = "^.*\\.apk$";// 这里是正则表达式
        return validateString(url, regEx);
    }
    // 匹配输入数据类型
    public static boolean matchCheck(String ins, int type) {
        String pat = "";
        switch (type) {
            case 0: // /手机 ?
                pat = "^1[3-8][0-9]{9}$";
                break;
            case 1:// /邮箱
                pat = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
                break;

            case 2: // /用户 ?
                pat = "^[0-9a-zA-Z]{4,12}$";
                break;
            case 3: // /密码
                pat = "^[\\s\\S]{6,20}$";
                break;
            case 4: // /中文
                pat = "^[0-9a-z\u4e00-\u9fa5|admin]{2,15}$";
                break;
            case 5: // /非零正整 ?
                pat = "^\\+?[1-9][0-9]*$";
                break;
            case 6: // /数字和字 ?
                pat = "^[A-Za-z0-9]+$";
                break;
            case 7: // /1-9的数 ?
                pat = "^[1-9]";
                break;
            case 8: // /身份 ?
                pat = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
                break;
            case 9: // /名字
                pat = "^([A-Za-z]|[\u4E00-\u9FA5])+$";
                break;
            case 10: // /时间 时：分： ?
                pat = "^([0-1][0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
                break;
        }
        Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(ins);
        return m.matches();
    }

    /**
     * 功能描述：替换字符串
     *
     * @param from   String 原始字符串
     * @param to     String 目标字符串
     * @param source String 母字符串
     * @return String 替换后的字符串
     */
    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null)
            return null;
        StringBuffer str = new StringBuffer("");
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            str.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            index = source.indexOf(from);
        }
        str.append(source);
        return str.toString();
    }

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input) || "null".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        return validateString(email, "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
    }

    /**
     * 把距离转友好的显示
     *
     * @param distance 单位为m的距离
     * @return
     */
    public static String friendlyDistance(double distance) {
        if (distance <= 0) {
            return "10米内";
        }
        // 距离显示方式：100——900米内、1公里内、2公里内、20——100公里内（10公里一个间隔）、100——1000公里内（100公里一个间隔）、1000公里外（此数据为运营确认数据，请尽量实现）
        if (distance > 100) {
            if (distance < 900) {
                return ((int) (Math.floor(distance / 100) * 100)) + 100 + "米内";
            } else if (distance < 20 * 1000) {
                return ((int) (Math.floor(distance / 1000))) + 1 + "公里内";
            } else if (distance < 100 * 1000) {// 10公里一个间隔
                return ((int) (Math.floor(distance / (1000 * 10)))) + 1 + "0公里内";
            } else if (distance < 1000 * 1000) {// 10公里一个间隔
                return ((int) (Math.floor(distance / (1000 * 100)))) + 1 + "00公里内";
            } else {
                return "1000公里外";
            }
        } else {
            return 100 + "米内";
        }
    }
    public static String getMapDistance(double distance){
        String str=distance+"";
        try{
            DecimalFormat df=new DecimalFormat(".##");
            if(distance<0){
                return "";
            }else if(distance<1000){
                return ">"+df.format(distance)+"米";
            }else{
                return ">"+df.format(distance/1000)+"公里";
            }
        }catch (Exception e){

        }
        return  ">"+str;
    }
    /**
     * 电话号码验证 最大输入11位
     *
     * @param mobiles 电话号码
     * @return 满足条件返回 true
     */
    public static boolean phoneValidate(String mobiles) {
        String regEx = "1[3,4,5,8,7]{1}\\d{9}";
        return validateString(mobiles, regEx);
    }

    /**
     * 字符验证
     *
     * @param str   需要验证的字符
     * @param regEx 正则表达式
     * @return 满足条件返回true
     */
    private static boolean validateString(String str, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * @Description:把list转换为一个用逗号分隔的字符串
     */
    public static String listToString(List list) {
        return listToString(list, ",");
    }
    public static String listToString(List list,String split) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + split);
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }
    public static List<String> StringToList(String str){
        return StringToList(str, ",");
    }
    public static List<String> StringToList(String str,String split){
        List<String> list=new ArrayList<>();
        try{
            if(!StringUtils.isEmpty(str)){
                for(int i=0;i<str.split(split).length;i++){
                    list.add(str.split(split)[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    /**
     * decode Unicode string
     *
     * @param s
     * @return
     */
    public static String decodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\' && chars[i + 1] == 'u') {
                char cc = 0;
                for (int j = 0; j < 4; j++) {
                    char ch = Character.toLowerCase(chars[i + 2 + j]);
                    if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
                        cc |= (Character.digit(ch, 16) << (3 - j) * 4);
                    } else {
                        cc = 0;
                        break;
                    }
                }
                if (cc > 0) {
                    i += 5;
                    sb.append(cc);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * encode Unicode string
     *
     * @param s
     * @return
     */
    public static String encodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);
        for (char c : s.toCharArray()) {
            if (c < 256) {
                sb.append(c);
            } else {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
                sb.append(Character.forDigit((c) & 0xf, 16));
            }
        }
        return sb.toString();
    }
    /**
     * url is usable
     *
     * @param url
     * @return
     */
    public static boolean isUrlUsable(String url) {
        if (isEmpty(url)) {
            return false;
        }

        URL urlTemp = null;
        HttpURLConnection connt = null;
        try {
            urlTemp = new URL(url);
            connt = (HttpURLConnection) urlTemp.openConnection();
            connt.setRequestMethod("HEAD");
            int returnCode = connt.getResponseCode();
            if (returnCode == HttpURLConnection.HTTP_OK) {
                return true;
            }
        } catch (Exception e) {
            return false;
        } finally {
            connt.disconnect();
        }
        return false;
    }
    /**
     * is url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(String url) {
        if(StringUtils.isEmpty(url)){
            return false;
        }else{
            Pattern pattern = Pattern.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$");
            return pattern.matcher(url).matches();
        }
    }
    public static String splitEnd(String str,String split){
        if(str.length()>0&&(str.charAt(str.length() - 1)+"").equals(split)){
            return  str.split(split,str.length()-1)[0];
        }
        return str;
    }
}
