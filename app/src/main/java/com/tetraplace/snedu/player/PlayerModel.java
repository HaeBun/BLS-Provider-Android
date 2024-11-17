package com.tetraplace.snedu.player;

public class PlayerModel {
    private String videoID;
    private String userIDToken;

    public PlayerModel(String videoID, String userIDToken) {
        setVideoID(videoID);
        setUserIDToken(userIDToken);
    }

    public String getUserIDToken() {
        return userIDToken;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setUserIDToken(String userIDToken) {
        this.userIDToken = userIDToken;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }
}