package server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class DataBase {
    enum OperationStatus {OK(), ERROR()}


    enum Operation {
        SET,
        GET,
        DELETE,
        EXIT
    }

    private final Gson gson = new Gson();
    private String gsonData = "";
    private Map<String, String> dataBase = new HashMap<>();
    private Map<String, String> response;

    public String handle(ArgsHandler arguments) {
        response = new LinkedHashMap<>();
        switch (arguments.getRequestType()) {
            case SET -> set(arguments.getKey(), arguments.getMessage());
            case GET -> get(arguments.getKey());
            case DELETE -> delete(arguments.getKey());
            case EXIT -> disconnect();
        }

        return gson.toJson(response);
    }

    private void disconnect() {
        response.put("response", OperationStatus.OK.name());
    }

    private void set(String key, String value) {
        loadDB();
        dataBase.put(key, value);
        saveDB(dataBase);

        response.put("response", OperationStatus.OK.toString());
    }

    private void get(String key) {
        loadDB();
        if (dataBase.containsKey(key)) {
            response.put("response", OperationStatus.OK.name());
            response.put("value", dataBase.get(key));

        } else {
            response.put("response", OperationStatus.ERROR.name());
            response.put("reason", "No such key");

        }
    }

    private void delete(String key) {
        loadDB();
        if (dataBase.containsKey(key)) {
            dataBase.remove(key);
            response.put("response", OperationStatus.OK.name());
        } else {
            response.put("response", OperationStatus.ERROR.name());
            response.put("reason", "No such key");
        }
        saveDB(dataBase);
    }

    private void loadDB() {
        if (dataBase.isEmpty()) {
            return;
        }
        Type hashMapType = new TypeToken<HashMap<String, String>>() {
        }.getType();
        dataBase = gson.fromJson(gsonData, hashMapType);
    }

    private void saveDB(Map<String, String> data) {
        gsonData = gson.toJson(data, HashMap.class);
    }

}
