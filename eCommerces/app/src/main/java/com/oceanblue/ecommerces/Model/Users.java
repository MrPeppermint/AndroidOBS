package com.oceanblue.ecommerces.Model;

public class Users {

    private String userName,phoneNo,password;


    public Users()
    {

    }

    public Users(String userName, String phoneNo, String password) {
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
