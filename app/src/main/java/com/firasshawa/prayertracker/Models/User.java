package com.firasshawa.prayertracker.Models;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID =1L;

    private String key;
    private String name;

    public User() {
        this.key = "";
        this.name = "";
    }

    public User(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
