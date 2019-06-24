package com.magicfit.middleware.utils;

import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class SpawnMLUtils {

    private static SpawnMLUtils spawnMLUtils;

    public static SpawnMLUtils getInstance() {
        if (spawnMLUtils == null) {
            spawnMLUtils = new SpawnMLUtils();
        }
        return spawnMLUtils;
    }

    public String errorMessage() {
        HashMap<String, String> map = new HashMap<>();
        map.put("error", "Request cannot be processed");
        map.put("status", "fail");
        return ResponseEntity.ok(map).toString();
    }
}
