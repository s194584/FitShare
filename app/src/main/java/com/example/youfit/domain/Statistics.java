package com.example.youfit.domain;

public class Statistics {

    private int totalWater = 0;
    private int totalWorkoutsTotal = 0;
    private long totalTimeTotal = 0;


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
    }

    public void addTotalTimeTotal(long add){
        totalTimeTotal += add;
    }

}
