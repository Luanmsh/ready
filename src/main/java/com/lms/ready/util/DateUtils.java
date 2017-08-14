package com.lms.ready.util;

public class DateUtils {

    /**
     * 取当前UTC时间的秒数
     * @return
     */
    public static Double currentTimeSeconds() {
        return System.currentTimeMillis() / 1000.0;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        System.out.println(currentTimeSeconds());
    }
}
