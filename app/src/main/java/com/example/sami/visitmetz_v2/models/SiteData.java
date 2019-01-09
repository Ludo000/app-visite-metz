package com.example.sami.visitmetz_v2.models;

import java.io.Serializable;
import java.util.Arrays;

public class SiteData implements Serializable {

    private String nom;
    private Double latitude;
    private Double longitude;
    private String adresse;
    private String categorie;
    private String resume;
    private byte[] image;
    private int id_ext, id;

    public SiteData(int id, int id_ext, String nom, double latitude, double longitude, String adresse, String categorie, String resume, byte[] image)
    {
        this.id = id;
        this.id_ext= id_ext;
        this.nom = nom;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresse = adresse;
        this.categorie = categorie;
        this.resume = resume;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setIDEXT(int id_ext) {
        this.id_ext = id_ext;
    }

    @Override
    public String toString() {
        return "SiteData{" +
                "nom='" + nom + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", adresse='" + adresse + '\'' +
                ", categorie='" + categorie + '\'' +
                ", resume='" + resume + '\'' +
                ", image=" + Arrays.toString(image) +
                ", id_ext=" + id_ext +
                ", id=" + id +
                '}';
    }

    public int getIDEXT() {
        return id_ext;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }
}
