package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import server.jsonservice.JsonService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DataBase {
    public enum Operation {SET, GET, DELETE, EXIT}

    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String NO_SUCH_KEY = "No such key";
    private static final String UNSUPPORTED_OPERATION = "Unsupported operation";

    private final Path pathToDatabase = Path.of(System.getProperty("user.dir") +
            "/JSON Database (Java)/task/src/server/data/db.json");
    private final JsonService dbHandler = new JsonService(pathToDatabase);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Gson gson = new Gson();

    private JsonObject db;
    private Response response;


    public String handle(JsonObject request) {
        switch (getOperation(request)) {
            case SET -> handleSetOperation(request);
            case GET -> handleGetOperation(request);
            case DELETE -> handleDeleteOperation(request);
            case EXIT -> disconnect();
            default -> response = Response.builder().status(ERROR).reason(UNSUPPORTED_OPERATION).build();
        }
        return serializeResponseToJson(response);
    }

    private void handleSetOperation(JsonObject request) {
        readDatabase();
        JsonElement key = request.get("key");
        JsonElement value = request.get("value");

        if (key.isJsonArray()) {
            JsonArray keyArray = key.getAsJsonArray();
            JsonObject targetJsonObject = findOrCreateTargetJsonObject(keyArray);

            int lastKeyIndex = keyArray.size() - 1;
            targetJsonObject.add(keyArray.get(lastKeyIndex).getAsString(), value);
        } else {
            db.add(key.getAsString(), value);
        }

        writeDatabase();
        response = Response.builder().status(OK).build();
    }

    private void handleGetOperation(JsonObject request) {
        readDatabase();
        JsonElement key = request.get("key");
        JsonElement value;

        if (key.isJsonArray()) {
            JsonArray keyArray = key.getAsJsonArray();
            JsonObject targetJsonObject = getTargetJsonObject(keyArray);
            int lastKeyIndex = keyArray.size() - 1;
            value = targetJsonObject == null ? null : targetJsonObject.get(keyArray.get(lastKeyIndex).getAsString());
        } else {
            value = request.get(key.toString());
        }

        if (value != null) {
            response = Response.builder().status(OK).value(value).build();
        } else {
            response = Response.builder().status(ERROR).reason(NO_SUCH_KEY).build();
        }
    }


    private void handleDeleteOperation(JsonObject request) {
        readDatabase();
        JsonElement key = request.get("key");
        JsonObject targetJsonObject;

        if (key.isJsonArray()) {
            JsonArray keyArray = key.getAsJsonArray();
            targetJsonObject = getTargetJsonObject(keyArray);

            if (targetJsonObject == null) {
                response = Response.builder().status(ERROR).reason(NO_SUCH_KEY).build();
                return;
            }
            int lastKeyIndex = keyArray.size() - 1;
            if (targetJsonObject.remove(keyArray.get(lastKeyIndex).getAsString()) == null) {
                response = Response.builder().status(ERROR).reason(NO_SUCH_KEY).build();
                return;

            }
        }

        writeDatabase();
        response = Response.builder().status(OK).build();
    }

    private JsonObject getTargetJsonObject(JsonArray keys) {
        JsonObject root = db;

        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i).getAsString();

            JsonElement value = root.get(key);

            if (value == null || value.isJsonPrimitive() || value.isJsonArray()) {
                return null;
            } else if (value.isJsonObject()) {
                root = value.getAsJsonObject();
            }
        }
        return root;
    }

    private JsonObject findOrCreateTargetJsonObject(JsonArray keys) {
        JsonObject root = db;

        for (int i = 0; i < keys.size() - 1; i++) {
            JsonObject newJsonObj = null;
            String key = keys.get(i).getAsString();

            assert root != null;
            JsonElement value = root.get(key);
            if (value.isJsonPrimitive() || value.isJsonArray()) {
                newJsonObj = new JsonObject();
                root.add(key, newJsonObj);
            } else if (value.isJsonObject()) {
                newJsonObj = value.getAsJsonObject();
            }
            root = newJsonObj;
        }
        return root;
    }

    private Operation getOperation(JsonObject request) {
        String receivedType = request.get("type").getAsString();
        return Operation.valueOf(receivedType.toUpperCase());
    }

    private void disconnect() {
        response = Response.builder().status(OK).build();
    }

    private void readDatabase() {
        lock.readLock().lock();
        try {
            db = (JsonObject) dbHandler.read();
        } catch (IOException e) {
            db = new JsonObject();
        } finally {
            lock.readLock().unlock();
        }
    }

    private void writeDatabase() {
        lock.writeLock().lock();
        try {
            dbHandler.write(db);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private String serializeResponseToJson(Response response) {
        return gson.toJson(response);
    }
}