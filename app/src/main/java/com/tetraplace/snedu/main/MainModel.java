package com.tetraplace.snedu.main;

public class MainModel {
    private int userId;
    private String userIDToken;
    
    public MainModel(int userId, String userIDToken) {
        setUserId(userId);
        setUserIDToken(userIDToken);
    }

    public int getUserId() {
        return this.userId;
    }

    public String userIDToken() {
        return this.userIDToken; 
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserIDToken(String userIDToken) {
        this.userIDToken = userIDToken;
    }

}