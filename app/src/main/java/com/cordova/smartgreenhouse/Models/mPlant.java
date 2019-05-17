package com.cordova.smartgreenhouse.Models;


public class mPlant {
;
    public String uid,  name, firstValue, secondValue, url;

    public mPlant() {
    }

    public mPlant(String name, String firstValue, String  secondValue,String uid, String url ) {
        this.name = name;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.uid= uid;
        this.url= url;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public  String getName() {
        return name;
    }

    public String getFirstValue() { return firstValue;
    }

    public String getSecondValue() {
        return secondValue;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setFirstValue(String firstValue) {
        this.firstValue = firstValue;
    }

    public void setSecondValue(String secondValue) {
        this.secondValue = secondValue;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }



    public String toString(){
        return " "+name+"\n" +
                " "+firstValue +"\n" +
                " "+secondValue
                +"\n" +
                " "+uid;
    }

}
