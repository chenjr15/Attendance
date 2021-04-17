package dev.chenjr.attendance.utils;

import java.util.UUID;

public class RandomUtil {
    public static final int MAX_RANDOM_STRING_LENGTH = 1000;

    /**
     * 生成随机字符串, 有一个最长的的限制MAX_RANDOM_STRING_LENGTH，不会返回大于限制长度的字符串
     *
     * @param length 指定生成的长度
     * @return 生成的随机字符串
     */
    public static String randomString(int length) {
        StringBuilder str = new StringBuilder();
        length = Math.min(length, MAX_RANDOM_STRING_LENGTH);
        while (length > 0) {
            UUID uuid = UUID.randomUUID();
            String s = uuid.toString().replaceAll("-", "");
            int curLength = Math.min(length, s.length());
            str.append(s, 0, curLength);
            length -= curLength;
        }

        return str.toString();
    }

    public static String randomNumberString(int length) {
        StringBuilder str = new StringBuilder();
        length = Math.min(length, MAX_RANDOM_STRING_LENGTH);
        while (length > 0) {
            UUID uuid = UUID.randomUUID();
            long lsb = uuid.getLeastSignificantBits();
            String s = Long.toString(lsb);
            // 去除可能的负号
            s = s.substring(1);
            int curLength = Math.min(length, s.length());
            str.append(s, 0, curLength);
            length -= curLength;
        }

        return str.toString();
    }
}
