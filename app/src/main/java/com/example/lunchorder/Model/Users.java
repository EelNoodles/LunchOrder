package com.example.lunchorder.Model;

public class Users {

    private String Name, Number, TotalPrice;

    public Users(){

    }

    public Users(String name, String number, String totalPrice) {
        Name = name;
        Number = number;
        TotalPrice = totalPrice;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        this.Number = number;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }
}
