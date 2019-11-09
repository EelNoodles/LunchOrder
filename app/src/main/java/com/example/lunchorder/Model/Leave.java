package com.example.lunchorder.Model;

public class Leave {

    private String Name, Number, LeaveDate, LeaveResult, Status = "✔  出席。";

    public Leave(){

    }

    public Leave(String name, String number, String leaveDate, String leaveResult, String status) {
        Name = name;
        Number = number;
        LeaveDate = leaveDate;
        LeaveResult = leaveResult;
        Status = status;
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

    public String getLeaveDate() {
        return LeaveDate;
    }

    public void setLeaveDate(String leaveDate) {
        LeaveDate = leaveDate;
    }

    public String getLeaveResult() {
        return LeaveResult;
    }

    public void setLeaveResult(String leaveResult) {
        LeaveResult = leaveResult;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

}
