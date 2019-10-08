package com.intacta.sosviagens.Utils;

import java.util.Calendar;

public class Utilities {
    public static final int RC_SIGN_IN = 123;
    public static boolean called = false;
    public static String callIdent="";
    public static String calltitle = "Números de emergência";
     public static boolean Isnight() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        return hour < 6 || hour > 18;
    }

}
