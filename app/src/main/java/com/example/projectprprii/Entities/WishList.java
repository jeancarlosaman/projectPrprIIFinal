package com.example.projectprprii.Entities;

import java.util.ArrayList;

public class WishList {

    private int id;
    private String name;
    private String description;
    private int user_id;
    private String gifts;
    private String creation_date;
    private String end_date;

    private ArrayList<Gift> giftsList;

    public WishList(int id, String name, String description, int user_id, String gifts, String creation_date, String end_date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user_id = user_id;
        this.gifts = gifts;
        this.creation_date = creation_date;
        this.end_date = end_date;
        giftsList = new ArrayList<>();
    }

    public WishList(String name, String description, int user_id, String gifts, String creation_date, String end_date) {
        this.name = name;
        this.description = description;
        this.user_id = user_id;
        this.gifts = gifts;
        this.creation_date = creation_date;
        this.end_date = end_date;
        giftsList = new ArrayList<>();
    }

    public WishList() {
    }

    public WishList(int wishlistId, String name, String description, int userId, String createdAt, String endDate) {
        this.id = wishlistId;
        this.name = name;
        this.description = description;
        this.user_id = userId;
        this.creation_date = createdAt;
        this.end_date = endDate;
        giftsList = new ArrayList<>();
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

    public int getUser_id() {
        return user_id;
    }

    public String getGifts() {
        return gifts;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public String getEnd_date() {
        return end_date;
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

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setGifts(String gifts) {
        this.gifts = gifts;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void addGift(Gift gift){
        giftsList.add(gift);
    }
    public ArrayList<Gift> getGiftsList(){
        return giftsList;
    }

}
