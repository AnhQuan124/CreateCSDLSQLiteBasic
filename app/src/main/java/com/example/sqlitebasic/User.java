package com.example.sqlitebasic;

public class User {
    private int id;
    private String masv;
    private String name;

    public int getId() {
        return id;
    }

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getMasv(){ return masv;}

    public User setMasv(String masv){
        this.masv=masv;
        return this;
    }

}

