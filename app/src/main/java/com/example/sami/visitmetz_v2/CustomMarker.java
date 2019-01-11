package com.example.sami.visitmetz_v2;

import com.google.android.gms.maps.model.MarkerOptions;

public class CustomMarker {

    private String Categorie;

    private double lat, longi;
    private String  name;
    private String resumer;

    public CustomMarker(String Categorie, double lat, double longi, String name, String resumer) {
        this.Categorie = Categorie;

        this.lat =lat;
        this.longi=longi;
        this.name =name;
        this.resumer=resumer;
    }

    public String getResumer() {
        return resumer;
    }

    public void setResumer(String resumer) {
        this.resumer = resumer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategorie() {
        return Categorie;
    }

    public void setCategorie(String categorie) {
        Categorie = categorie;
    }



    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }
}
