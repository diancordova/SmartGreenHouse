package com.cordova.smartgreenhouse.Models;

public class History {
    int xValue, yValue, nutrisi;
    public History() {

    }

    public History(int xValue, int yValue, int nutrisi) {
        this.xValue = xValue;
        this.yValue = yValue;
        this.nutrisi = nutrisi;
    }

    public int getxValue() {
        return xValue;
    }



    public int getyValue() {
        return yValue;
    }

    public int getNutrisi() {
        return nutrisi;
    }
}
