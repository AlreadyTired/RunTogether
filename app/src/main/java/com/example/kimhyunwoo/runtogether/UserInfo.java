package com.example.kimhyunwoo.runtogether;

import android.app.Application;

public class UserInfo extends Application{
    private static String UserEmail="";
    private static String UserToken="";
    private static String UserNickname="";

    public static void setUserNickname(String email)
    {
        UserNickname = email;
    }
    public static String getUserNickname()
    {
        return UserNickname;
    }

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

    public static void UserDataReset()
    {
        UserToken=null;
        UserEmail=null;
    }
}
