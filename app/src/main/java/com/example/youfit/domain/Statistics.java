package com.example.youfit.domain;

public class Statistics {

    private int totalWater = 0;
    private int totalWorkoutsTotal = 0;
    private int totalTimeTotal = 0;


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

    public int getTotalTimeTotal() {
        return totalTimeTotal;
    }

    public void setTotalTimeTotal(int totalTimeTotal) {
        this.totalTimeTotal = totalTimeTotal;
    }

    public void addTotalWorkoutsTotal(int add){
        totalWorkoutsTotal += add;
    }

    public void addTotalTimeTotal(long add){
        totalTimeTotal += add;
    }

}
