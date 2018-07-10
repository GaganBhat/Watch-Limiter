package com.example.gaganbhat.testapp;

public class Utils{

    public static double millisToSeconds(int millis){
        return (millis / 1000.0);
    }

    public static double millisToMinutes(int millis) {
        return (millis / 1000) / 60.0;
    }

    public static double minutesToMillis(int minutes) {
        return (minutes * 60) * 1000.0;
    }

    public static double minutesToSeconds(int minutes) {
        return minutes * 60.0;
    }


}
