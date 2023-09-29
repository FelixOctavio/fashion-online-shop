package com.felix.uas_fashionstore.ui.start.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private Integer userId;
    private Integer userType;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, Integer userId, Integer userType) {
        this.displayName = displayName;
        this.userId = userId;
        this.userType = userType;
    }

    String getDisplayName() {
        return displayName;
    }
    Integer getUserId() {
        return userId;
    }
    Integer getUserType() {
        return userType;
    }
}