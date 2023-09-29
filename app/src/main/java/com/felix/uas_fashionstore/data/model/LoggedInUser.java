package com.felix.uas_fashionstore.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private Integer userId, typeUser;
    private String username;
    private String displayName;

    public LoggedInUser(Integer userId, String username, String displayName, Integer typeUser) {
        this.userId = userId;
        this.username = username;
        this.displayName = displayName;
        this.typeUser = typeUser;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }
    public Integer getTypeUser() {
        return typeUser;
    }
}