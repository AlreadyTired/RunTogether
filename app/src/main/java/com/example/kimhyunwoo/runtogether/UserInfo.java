package com.example.kimhyunwoo.runtogether;

import android.app.Application;

public class UserInfo extends Application{
    private static String UserEmail;
    private static String UserToken;

    public static void setUserEmail(String email)
    {
        UserEmail = email;
    }
    public static String getUserEmail()
    {
        return UserEmail;
    }

    public static void setUserToken(String token)
    {
        UserToken = token;
    }
    public static String getUserToken()
    {
        return UserToken;
    }
}
