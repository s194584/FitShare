package com.example.youfit.domain;

import java.util.Calendar;
import java.util.Date;

public class Statistics {

    private int totalWater = 0;
    private int totalWorkoutsTotal = 0;
    private long totalTimeTotal = 0;
    private Date lastUpdate = Calendar.getInstance().getTime();
    private int weeklyWorkoutsTotal = 0;
    private long weeklyTimeTotal = 0;

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getWeeklyWorkoutsTotal() {
        return weeklyWorkoutsTotal;
    }

    public void setWeeklyWorkoutsTotal(int weeklyWorkoutsTotal) {
        this.weeklyWorkoutsTotal = weeklyWorkoutsTotal;
    }

    public long getWeeklyTimeTotal() {
        return weeklyTimeTotal;
    }

    public void setWeeklyTimeTotal(long weeklyTimeTotal) {
        this.weeklyTimeTotal = weeklyTimeTotal;
    }

    public Statistics(){}

    public int getTotalWater() {
        return totalWater;
    }

    public void setTotalWater(int totalWater) {
        this.totalWater = totalWater;
    }

    public int getTotalWorkoutsTotal() {
        return totalWorkoutsTotal;
    }

    public void setTotalWorkoutsTotal(int totalWorkoutsTotal) {
        this.totalWorkoutsTotal = totalWorkoutsTotal;
    }

    public long getTotalTimeTotal() {
        return totalTimeTotal;
    }

    public void setTotalTimeTotal(long totalTimeTotal) {
        this.totalTimeTotal = totalTimeTotal;
    }

    public void addTotalWorkoutsTotal(int add){
        totalWorkoutsTotal += add;
        addWeeklyWorkoutsTotal(add);
    }

    public void addTotalTimeTotal(long add){
        totalTimeTotal += add;
        addWeeklyTimeTotal(add);
    }

    public void addWeeklyTimeTotal(long add){
        // Weekly check
        if (checkTimeForUpdate()){
            setWeeklyTimeTotal(0);
            lastUpdate = new Date();
        }
        weeklyTimeTotal += add;
    }
    public void addWeeklyWorkoutsTotal(long add){
        // Weekly check
        if (checkTimeForUpdate()){
            setWeeklyTimeTotal(0);
            lastUpdate = new Date();
        }
        weeklyWorkoutsTotal += add;
    }

    private boolean checkTimeForUpdate(){
        Date now = new Date();
        Calendar thisTime = Calendar.getInstance();
        thisTime.setTime(now);
        Calendar thatTime = Calendar.getInstance();
        thatTime.setTime(lastUpdate);
        return thatTime.get
                (Calendar.WEEK_OF_YEAR)
                <
                thisTime.get(Calendar.WEEK_OF_YEAR);
    }

}
