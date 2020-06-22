package com.example.youfit;

public class Utility {

    public static String formatEnum(String enu) {
        return enu.substring(0,1) + enu.substring(1).toLowerCase();
    }

    public static String millisToHour(long input){
        long time = input/1000;
        long hours = time / (60*60);
        long rest = time-(hours*60*60);
        long minutes = rest / 60;

        return hours >= 1 ? hours + "h " + minutes + "m" : minutes + "m";
    }
}
