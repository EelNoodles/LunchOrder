package com.example.lunchorder.Model;

public class Users {

    private String UserClass, Name, Number, TotalPrice;

    public Users(){

    }

    public Users(String userClass, String name, String number, String totalPrice) {
        UserClass = userClass;
        Name = name;
        Number = number;
        TotalPrice = totalPrice;
    }

    public String getUserClass() {
        return UserClass;
    }

    public void setUserClass(String userClass) {
        UserClass = userClass;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }
}
