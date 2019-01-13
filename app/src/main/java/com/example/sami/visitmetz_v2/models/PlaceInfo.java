package com.example.sami.visitmetz_v2.models;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class PlaceInfo {


    private String name;
    private String address;
    private String phoneNumber;
    private String id, id_ext;
    private Uri websiteUri;
    private LatLng latlng;
    private float rating;
    private String attributions;
    private byte[] image;


    public PlaceInfo(String id_ext,String name, String address, String phoneNumber, String id, Uri websiteUri,
                     LatLng latlng, float rating, String attributions, byte[] image) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.id_ext =  id_ext;
        this.websiteUri = websiteUri;
        this.latlng = latlng;
        this.rating = rating;
        this.attributions = attributions;
        this.image = image;

    }

    public PlaceInfo() {
    }

    public String getId_ext() {
        return id_ext;
    }

    public void setId_ext(String id_ext) {
        this.id_ext = id_ext;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Uri getWebsiteUri() {
        return websiteUri;
    }
    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }
    public LatLng getLatlng() {
        return latlng;
    }
    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "PlaceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", id='" + id + '\'' +
                ", id='" + id_ext + '\'' +
                ", websiteUri=" + websiteUri +
                ", latlng=" + latlng +
                ", rating=" + rating +
                ", attributions='" + attributions + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

}
