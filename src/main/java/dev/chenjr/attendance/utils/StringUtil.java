package dev.chenjr.attendance.utils;

import java.util.Arrays;

public class StringUtil {

    /**
     * 下划线转驼峰
     * user_name  ---->  userName
     * userName   --->  userName
     *
     * @param underlineStr 带有下划线的字符串
     * @return 驼峰字符串
     */
    public static String toCamelCase(String underlineStr) {
        if (underlineStr == null) {
            return null;
        }
        // 分成数组
        char[] charArray = underlineStr.toCharArray();
        // 判断上次循环的字符是否是"_"
        boolean underlineBefore = false;
        StringBuilder builder = new StringBuilder();
        for (int i = 0, l = charArray.length; i < l; i++) {
            // 判断当前字符是否是"_",如果跳出本次循环
            if (charArray[i] == 95) {
                underlineBefore = true;
            } else if (underlineBefore) {
                // 如果为true，代表上次的字符是"_",当前字符需要转成大写
                builder.append(charArray[i] -= 32);
                underlineBefore = false;
            } else {
                // 不是"_"后的字符就直接追加
                builder.append(charArray[i]);
            }
        }
        return builder.toString();
    }

    /**
     * 驼峰转 下划线
     * userName  ---->  user_name
     * user_name  ---->  user_name
     *
     * @param camelCaseString 驼峰字符串
     * @return 带下滑线的String
     */
    public static String toUnderlineCase(String camelCaseString) {
        if (camelCaseString == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        int i = 0;
        char ch;
        char chChanged;
        for (; i < camelCaseString.length(); ++i) {
            ch = camelCaseString.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                chChanged = (char) (ch + 32);
                if (i > 0) {
                    buf.append('_');
                }
                buf.append(chChanged);
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    public static boolean notEmpty(String search) {
        return search != null && search.length() != 0;
    }

    public static String join(String... stringList) {
        System.out.println(Arrays.toString(stringList));

        StringBuilder builder = new StringBuilder();
        for (String s : stringList) {
            if (s == null) {
                continue;
            }
            builder.append(s);
            builder.append('-');
        }
        System.out.println(builder.toString());
        if (builder.length() != 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}