package com.example.projectprprii.Entities;

public class Product {

    private int id;
    private String name;
    private String description;
    private float price;
    private String link;
    private String image;
    private int is_active;

    public Product(int id, String name, String description, float price, String link, String image, int is_active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.link = link;
        this.image = image;
        this.is_active = is_active;
    }

    public Product(String name, String description, float price, String link, String image, int is_active) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.link = link;
        this.image = image;
        this.is_active = is_active;
    }

    public Product() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public String getLink() {
        return link;
    }

    public String getImage() {
        return image;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }


}
