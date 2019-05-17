package com.cordova.smartgreenhouse.Models;

public class mHistory {
    public String tanggal,waktu,nutrisi,ph,suhu;



    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getNutrisi() {
        return nutrisi;
    }

    public void setNutrisi(String nutrisi) {
        this.nutrisi = nutrisi;
    }

    public String getPh() {
        return ph;
    }

    public void setPh(String ph) {
        this.ph = ph;
    }

    public String getSuhu() {
        return suhu;
    }

    public void setSuhu(String suhu) {
        this.suhu = suhu;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public mHistory(String nutrisi, String ph, String suhu, String tanggal,String waktu) {
        this.nutrisi = nutrisi;
        this.ph = ph;
        this.suhu = suhu;
        this.tanggal = tanggal;
        this.waktu = waktu;
    }

    public mHistory() {
    }
}
