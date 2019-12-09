package com.example.pi4homemobile.APIs;

import android.util.Base64;

import java.util.Properties;

public class AuthenticationCreator
{
    public static String createAuthenticationHeader(Properties myProperties) {
        String serverUser = myProperties.getProperty("spring.security.user.name");
        String serverPass = myProperties.getProperty("spring.security.user.password");

        return "Basic " + Base64.encodeToString((serverUser + ":" + serverPass).getBytes(), Base64.NO_WRAP);
    }
}
