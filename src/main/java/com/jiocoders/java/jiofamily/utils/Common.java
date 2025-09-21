package com.jiocoders.java.jiofamily.utils;

import java.util.Random;

public class Common {

    // private static final String regexEmail = "^(.+)@(.+)$";
    // public static final String regexEmail =
    // "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    public static final String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public static final String regexUUID = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";

    public static final int paginationElementSize = 10;

    public static final String basePackage = "com.jiocoders.ims";
    public static final String fcmServer = "track-2022-firebase-adminsdk-e4pjl-73493d7daa.json";

    public static boolean checkNotNull(String s) {
        if (s == null) {
            return false;
        }
        s = s.trim();
        if (s.equals("") || s.isEmpty()) {
            return false;
        }
        return true;
    }

    public static boolean checkNotZero(int i) {
        if (i == 0) {
            return false;
        }
        return true;
    }

    public static Integer generateOTP() {
        Random random = new Random();
        int OTP = 100000 + random.nextInt(900000);
        return OTP;
    }
}
