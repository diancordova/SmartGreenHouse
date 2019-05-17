package com.cordova.smartgreenhouse.Models;


public class mUser {
    public String uid,  name, email, phone, address, active, work, url;

    public mUser() {
    }

    public mUser(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    public mUser(String name, String email, String phone, String address, String url) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.url = url;
    }


    public mUser(String name, String email, String phone, String address, String active, String work) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;

        this.active = active;
        this.work = work;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }



    public String getEmail() {
        return email;
    }

    public String getActive() {
        return active;
    }

    public String getWork() {
        return work;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public void setActive(String active) {
        this.active = active;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
