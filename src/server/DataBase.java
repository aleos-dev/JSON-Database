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

    private final Gson gson = new Gson();
    private final Path pathToDatabase = Path.of(System.getProperty("user.dir") + "/src/server/data/db.json");
    private final JsonService dbHandler = new JsonService(pathToDatabase);
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
//    /home/aleos/Documents/development/java/hyperskill/JSON Database/JSON Database (Java)/task/src/server/data/db.json

    private JsonObject db;
    private Response response;


    public String handle(JsonObject request) {
        switch (getOperation(request)) {
            case SET -> handleSetOperation(request);
//            case GET -> get(request.getKey());
//            case DELETE -> delete(request.getKey());
//            case EXIT -> disconnect();
        }
        return serializeResponseToJson(response);
    }

    private void handleSetOperation(JsonObject request) {
        readDb();
        JsonElement key = request.get("key");

        if (key.isJsonArray()) {
            JsonArray keys = request.getAsJsonArray("key");

        } else {

//            handleSetOperation();
        }

        writeDb();
        response = Response.builder().status(OK).build();
    }


    private JsonObject findRootForKeyElement(JsonArray keys, JsonObject root) {
        JsonElement je = root.get("key");

        if (keys.size() ==)

        String key = keys.get(0).getAsString();

        if (keys.size() == 1 ) {
            i
            return root.getAsJsonObject(keyEl.toString());
        }

        if (je.isJsonNull() || je.isJsonPrimitive()) {
            return root;
        } else {
//           find
        }

        return je;
    }

    private Operation getOperation(JsonObject request) {
        String receivedType = request.get("type").getAsString();
        return Operation.valueOf(receivedType.toUpperCase());
    }

    private void disconnect() {
        response = Response.builder().status(OK).build();
    }

    private void set(String key, String value) {
        readDb();
//        db.put(key, value);
        writeDb();

        response = Response.builder().status(OK).build();
    }

//    private void get(String key) {
//        readDb();
//        if (db.containsKey(key)) {
//            response = Response.builder().status(OK).value(db.get(key)).build();
//        } else {
//            response = Response.builder().status(ERROR).reason(NO_SUCH_KEY).build();
//        }
//    }
//
//    private void delete(String key) {
//        readDb();
//        if (db.containsKey(key)) {
//            db.remove(key);
//            writeDb();
//            response = Response.builder().status(OK).build();
//        } else {
//            response = Response.builder().status(ERROR).reason(NO_SUCH_KEY).build();
//        }
//    }

    private void readDb() {
        lock.readLock().lock();
        try {
            db = (JsonObject) dbHandler.read();
        } catch (IOException e) {
            db = new JsonObject();
        } finally {
            lock.readLock().unlock();
        }
    }

    private void writeDb() {
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
