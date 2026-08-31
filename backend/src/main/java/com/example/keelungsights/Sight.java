package com.example.keelungsights;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sights")
public class Sight {
    @Id
    private String id;
    private String sightName;
    private String zone;
    private String category;
    private String photoURL;
    private String description;
    private String address;

    public Sight() {

    }

    public String getId() {
        return id;
    }

    public String getSightName() {
        return sightName;
    }

    public String getZone() {
        return zone;
    }

    public String getCategory() {
        return category;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSightName(String sightName) {
        this.sightName = sightName;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Sight{" +
                "sightName='" + sightName + '\'' +
                ", zone='" + zone + '\'' +
                ", category='" + category + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

