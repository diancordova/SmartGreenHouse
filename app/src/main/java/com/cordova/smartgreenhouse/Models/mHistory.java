package com.cordova.smartgreenhouse.Models;

public class mHistory {
    public String tanggal,waktu;
    public int nutrisi,ph,suhu;



    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public int getNutrisi() {
        return nutrisi;
    }

    public void setNutrisi(int nutrisi) {
        this.nutrisi = nutrisi;
    }

    public int getPh() {
        return ph;
    }

    public void setPh(int ph) {
        this.ph = ph;
    }

    public int getSuhu() {
        return suhu;
    }

    public void setSuhu(int suhu) {
        this.suhu = suhu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public mHistory(int nutrisi, int ph, int suhu, String tanggal,String waktu) {
        this.nutrisi = nutrisi;
        this.ph = ph;
        this.suhu = suhu;
        this.tanggal = tanggal;
        this.waktu = waktu;
    }

    public mHistory() {
    }
}
