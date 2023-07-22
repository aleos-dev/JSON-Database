package server;

import com.google.gson.Gson;
import server.jsonservice.JsonService;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
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

    private Map<String, String> db;
    private Response response;


    public String handle(ClientRequest request) {
        switch (request.getOperation()) {
            case SET -> set(request.getKey(), request.getValue());
            case GET -> get(request.getKey());
            case DELETE -> delete(request.getKey());
            case EXIT -> disconnect();
        }
        return serializeResponseToJson(response);
    }

    private void disconnect() {
        response = Response.builder().status(OK).build();
    }

    private void set(String key, String value) {
        readDb();
        db.put(key, value);
        writeDb();

        response = Response.builder().status(OK).build();
    }

    private void get(String key) {
        readDb();
        if (db.containsKey(key)) {
            response = Response.builder().status(OK).value(db.get(key)).build();
        } else {
            response = Response.builder().status(ERROR).reason(NO_SUCH_KEY).build();
        }
    }

    private void delete(String key) {
        readDb();
        if (db.containsKey(key)) {
            db.remove(key);
            writeDb();
            response = Response.builder().status(OK).build();
        } else {
            response = Response.builder().status(ERROR).reason(NO_SUCH_KEY).build();
        }
    }

    private void readDb() {
        lock.readLock().lock();
        try {
            db = dbHandler.read();
        } catch (IOException e) {
            db = new HashMap<>();
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
