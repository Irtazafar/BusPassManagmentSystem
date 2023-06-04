package com.example.finalproject;

public class UsersClass {
    private String rollNum,name,contactNo,email,password,accountStatus,pass, userLevel;

    public UsersClass(String rollNum, String name, String contactNo, String email) {
        this.rollNum = rollNum;
        this.name = name;
        this.contactNo = contactNo;
        this.email = email;
    }

    public UsersClass(String rollNum, String name, String contactNo, String email, String password, String accountStatus, String pass, String userLevel) {
        this.rollNum = rollNum;
        this.name = name;
        this.contactNo = contactNo;
        this.email = email;
        this.password = password;
        this.accountStatus = accountStatus;
        this.pass = pass;
        this.userLevel = userLevel;
    }

    public String getRollNum() {
        return rollNum;
    }

    public void setRollNum(String rollNum) {
        this.rollNum = rollNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(String userLevel) {
        this.userLevel = userLevel;
    }
}
