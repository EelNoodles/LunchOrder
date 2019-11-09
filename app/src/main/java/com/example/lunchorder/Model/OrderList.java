package com.example.lunchorder.Model;

public class OrderList {

    private String Price, Food, Amount, Ice, Sweet, TotalPrice, TotalAmount;

    public OrderList(){

    }

    public OrderList(String price, String food, String amount, String ice, String sweet, String totalPrice, String totalAmount) {
        Price = price;
        Food = food;
        Amount = amount;
        Ice = ice;
        Sweet = sweet;
        TotalPrice = totalPrice;
        TotalAmount = totalAmount;
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

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getIce() {
        return Ice;
    }

    public void setIce(String ice) {
        Ice = ice;
    }

    public String getSweet() {
        return Sweet;
    }

    public void setSweet(String sweet) {
        Sweet = sweet;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }
}
