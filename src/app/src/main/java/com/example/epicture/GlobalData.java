package com.example.epicture;

public class GlobalData {
    private static String access_token;
    private static String account_username;
    enum RequestType {
        UserPhotos,
        UserFav,
        ImgFav,
        Search,
        Get
    }

    public static void setAccess_token(String access_token) {
        GlobalData.access_token = access_token;
    }

    public static void setAccount_username(String account_username) {
        GlobalData.account_username = account_username;
    }

    public static String getAccount_username() { return(account_username); }

    public static String getAccess_token() {
        return(access_token);
    }
}
