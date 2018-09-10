package com.nassaty.hireme.utils;

public class TimeUtils {

    public String getCurrentTimeStamp(){
        Long tsLong = System.currentTimeMillis()/1000;
        return tsLong.toString();
    }
}
