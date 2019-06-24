package com.magicfit.middleware.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.UUID;

public class WebApplicationUtils {

    private static WebApplicationUtils webApplicationUtils;

    private HashMap<String, String> map;

    public static WebApplicationUtils getInstance() {
        if (webApplicationUtils == null) {
            synchronized (WebApplicationUtils.class) {
                webApplicationUtils = new WebApplicationUtils();
            }
        }
        return webApplicationUtils;
    }

    /*
     This method is responsible for Hashing the password using MD5
     @param passwordToHash- password string to be hashed
     @salt - Salt added for hashing
    */
    public String get_SHA_512_SecurePassword(String passwordToHash, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /*
    This method generated random unique UUID
    This is usefull for created tokens for applications
    */
    public String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public HashMap<String, String> getErrorMessage(String error,String message,String status) {
        if (map == null)
            map = new HashMap<String, String>();

        map.put("error", error);
        map.put("message", message);
        map.put("status", status);
        return map;
    }
}
