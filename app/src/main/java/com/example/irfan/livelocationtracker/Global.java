package com.example.irfan.livelocationtracker;

public class Global {

    public static int incidentCounter;
    public static int alertCounter;
    public static double prevLat;
    public static double prevLon;
    public static boolean prevAlert;

    static {
        incidentCounter = 0;
        alertCounter = 0;
        prevLat = 999;
        prevLon = 999;
        prevAlert = false;
    }
}
