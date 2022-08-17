package com.bootstraptsk4.task4.Model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private  String AccessToken;
    private  String refreshToken;


    public String getAccessToken() {
        return AccessToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = accessToken;
    }

    public JwtResponse() {
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public JwtResponse(String AccessToken, String refreshToken) {
        this.AccessToken = AccessToken;
        this.refreshToken = refreshToken;
    }


}
