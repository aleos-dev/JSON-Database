package server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class DataBase {
    enum OperationStatus {OK(), ERROR()}
    enum Reasons {
        NO_SUCH_KEY("No such key");
        private String message;
        Reasons(String message) {
            this.message = message;
        }
    }


    enum Operation {
        SET,
        GET,
        DELETE,
        EXIT
    }

    private final Gson gson = new Gson();
    private String gsonData = "";
    private Map<String, String> dataBase = new HashMap<>();
    private Response response;

    public String handle(ClientRequestHandler arguments) {
        switch (arguments.getRequestType()) {
            case SET -> set(arguments.getKey(), arguments.getMessage());
            case GET -> get(arguments.getKey());
            case DELETE -> delete(arguments.getKey());
            case EXIT -> disconnect();
        }

        return gson.toJson(response);
    }

    private void disconnect() {
        response = Response.builder().response(OperationStatus.OK.name()).build();
    }

    private void set(String key, String value) {
        loadDB();
        dataBase.put(key, value);
        saveDB(dataBase);

        response = Response.builder().response(OperationStatus.OK.name()).build();
    }

    private void get(String key) {
        loadDB();

        if (dataBase.containsKey(key)) {
            response = Response.builder()
                    .response(OperationStatus.OK.name())
                    .value(dataBase.get(key))
                    .build();
        } else {
            response = Response.builder()
                    .response(OperationStatus.ERROR.name())
                    .reason(Reasons.NO_SUCH_KEY.message)
                    .build();
        }
    }

    private void delete(String key) {
        loadDB();
        if (dataBase.containsKey(key)) {
            dataBase.remove(key);
            response = Response.builder().response(OperationStatus.OK.name()).build();

        } else {
            response = Response.builder()
                    .response(OperationStatus.ERROR.name())
                    .reason(Reasons.NO_SUCH_KEY.message)
                    .build();
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
