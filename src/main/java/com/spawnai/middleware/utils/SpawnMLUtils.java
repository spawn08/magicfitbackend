package com.spawnai.middleware.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;

public class SpawnMLUtils {

    private static SpawnMLUtils spawnMLUtils;
    private OkHttpClient okHttpClient;
    private OkHttpClient trainHttpClient;
    private static String DATA_FILE_PATH = "/opt/data/"/*"E:\\spawn-ai-java-middleware\\src\\opt\\data\\"*/;

    public static SpawnMLUtils getInstance() {
        if (spawnMLUtils == null) {
            spawnMLUtils = new SpawnMLUtils();
        }
        return spawnMLUtils;
    }

    public String callPredictAPI(String q, String modelName, String lang) {
        try {
            String PREDICT_URL = "https://spawnai.com/entity_extract?q=%s&model=%s&lang=%s";
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new BotInterceptors())
                        .build();
            }
            PREDICT_URL = String.format(PREDICT_URL, q, modelName, lang);
            Request request = new Request.Builder().url(PREDICT_URL).build();
            Response response = okHttpClient.newCall(request).execute();
            String mlResponse = response.body().string();
            return mlResponse;
        } catch (Exception e) {
            return errorMessage();

        }
    }

    public String callTrainAPI(String modelName, String lang) {
        try {
            String TRAIN_URL = "https://spawnai.com/train?model_name=%s&lang=%s";
            if (trainHttpClient == null) {
                trainHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new BotInterceptors())
                        .build();
            }
            TRAIN_URL = String.format(TRAIN_URL, modelName, lang);
            Request request = new Request.Builder().url(TRAIN_URL).build();
            Response response = trainHttpClient.newCall(request).execute();
            String mlResponse = response.body().string();
            return mlResponse;
        } catch (Exception e) {
            return errorMessage();

        }
    }

    public boolean saveFile(JSONObject jsonObject, String dataFile) {
        try {
            String line = "";
            JSONObject jsonFile = null;
            JSONObject jsonObject1 = new JSONObject();
            StringBuffer stringBuffer = new StringBuffer();
            File file = new File(DATA_FILE_PATH + dataFile);
            jsonObject.remove("modelName");
            jsonObject.remove("lang");
            if (file.exists()) {
                JSONArray jsonArray = jsonObject.getJSONArray("intent");
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);

                while ((line = br.readLine()) != null) {
                    stringBuffer.append(line);
                }
                String fileContents = (stringBuffer == null || stringBuffer.toString().isEmpty()) ? "{}" : stringBuffer.toString();
                JSONArray existingArray = null;
                jsonFile = new JSONObject(fileContents);
                if (jsonFile.has("intent")) {
                    existingArray = jsonFile.getJSONArray("intent");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject1 = jsonArray.getJSONObject(i);
                        existingArray.put(jsonObject1);
                    }
                } else {
                    existingArray = jsonObject.getJSONArray("intent");
                }
                jsonFile.put("intent", existingArray);
                byte[] writeByte = jsonFile.toString().getBytes();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(writeByte);
                fos.close();
                return true;
            } else {
                file.createNewFile();
                JSONArray jsonArray = jsonObject.getJSONArray("intent");
                FileReader fileReader = new FileReader(file);
                BufferedReader br = new BufferedReader(fileReader);

                while ((line = br.readLine()) != null) {
                    stringBuffer.append(line);
                }
                String fileContents = (stringBuffer == null) ? "{}" : stringBuffer.toString();
                jsonFile = new JSONObject(fileContents);
                JSONArray existingArray = jsonFile.getJSONArray("intent");
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject1 = jsonArray.getJSONObject(i);
                    existingArray.put(jsonObject1);
                }
                jsonFile.put("intent", existingArray);
                byte[] writeByte = jsonFile.toString().getBytes();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(writeByte);
                fos.close();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String errorMessage() {
        HashMap<String, String> map = new HashMap<>();
        map.put("error", "Request cannot be processed");
        map.put("status", "fail");
        return ResponseEntity.ok(map).toString();
    }
}
