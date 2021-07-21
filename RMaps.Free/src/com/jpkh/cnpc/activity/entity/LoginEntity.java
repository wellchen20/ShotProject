package com.jpkh.cnpc.activity.entity;

public class LoginEntity {
    /**
     * userName : sss
     * userPwd : asdfcd!
     */

    private String userName;
    private String userPwd;
    private String channel;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
