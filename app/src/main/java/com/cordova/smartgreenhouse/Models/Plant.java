package com.cordova.smartgreenhouse.Models;

import java.io.Serializable;

public class Plant implements Serializable {
    public String uid, nama, firstValue, secondValue;

    public Plant() {
    }

    public Plant(String nama, String firstValue, String secondValue) {
        this.nama = nama;
        this.firstValue = firstValue;
        this.secondValue = secondValue;

    }


    public String getNama() {
        return nama;
    }

    public String getFirstValue() {
        return firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }


    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setFirstValue(String firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String toString() {
        return " " + nama + "\n" +
                " " + firstValue + "\n" +
                " " + secondValue;
    }


}
