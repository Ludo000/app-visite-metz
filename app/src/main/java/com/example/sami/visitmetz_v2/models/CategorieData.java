package com.example.sami.visitmetz_v2.models;

public class CategorieData {
    private int id;
    private String name;

    public CategorieData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
