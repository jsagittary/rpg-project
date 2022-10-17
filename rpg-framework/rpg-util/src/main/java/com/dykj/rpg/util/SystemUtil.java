package com.dykj.rpg.util;

/**
 * @author jyb
 * @date 2021/1/14 15:28
 * @Description
 */
public class SystemUtil {
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }


    public static void main(String[] args) {
        System.out.println(isWindows());
    }
}


