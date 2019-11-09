package com.example.lunchorder.Model;

public class FoodStore {

    private String FoodStore, DrinkStore, Date;

    public FoodStore(){

    }

    public FoodStore(String foodStore, String drinkStore, String date) {
        FoodStore = foodStore;
        DrinkStore = drinkStore;
        Date = date;
    }

    public String getFoodStore() {
        return FoodStore;
    }

    public void setFoodStore(String foodStore) {
        FoodStore = foodStore;
    }

    public String getDrinkStore() {
        return DrinkStore;
    }

    public void setDrinkStore(String drinkStore) {
        DrinkStore = drinkStore;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
