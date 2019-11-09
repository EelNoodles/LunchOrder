package com.example.lunchorder.Model;

public class Food {

    private String Price, Food, StoreName, TotalAmount, Amount, Types;

    public Food() {

    }

    public Food(String price, String food, String storeName, String totalamount, String amount, String types) {
        this.Price = price;
        this.Food = food;
        this.StoreName = storeName;
        this.TotalAmount = totalamount;
        this.Amount = amount;
        this.Types = types;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getFood() {
        return Food;
    }

    public void setFood(String food) {
        Food = food;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getTypes() {
        return Types;
    }

    public void setTypes(String types) {
        Types = types;
    }
}