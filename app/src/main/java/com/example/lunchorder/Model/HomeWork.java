package com.example.lunchorder.Model;

public class HomeWork {

    private String Title, DeadLine, Remarks = "登入者無設定備註。", Subject;

    public HomeWork(){

    }

    public HomeWork(String title, String deadLine, String remarks, String subject) {
        Title = title;
        DeadLine = deadLine;
        Remarks = remarks;
        Subject = subject;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDeadLine() {
        return DeadLine;
    }

    public void setDeadLine(String deadLine) {
        DeadLine = deadLine;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }
}
