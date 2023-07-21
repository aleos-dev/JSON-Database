package server;

import com.google.gson.Gson;
import server.jsonservice.JsonDatabaseHandler;

import java.util.Map;

public class DataBase {



    public enum OperationStatus {
        OK, ERROR
    }

    public enum Reason {
        NO_SUCH_KEY("No such key");

        private final String message;

        Reason(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public enum Operation {
        SET, GET, DELETE, EXIT
    }

    private final Gson gson = new Gson();
    private final JsonDatabaseHandler dbHandler = new JsonDatabaseHandler();
    private Map<String, String> db;
    private Response response;



    public String handle(ClientRequest request) {
        switch (request.getOperation()) {
            case SET -> set(request.getKey(), request.getValue());
            case GET -> get(request.getKey());
            case DELETE -> delete(request.getKey());
            case EXIT -> disconnect();
        }

        return serializeResponse(response);
    }

    private void disconnect() {
        response = Response.builder().response(OperationStatus.OK.name()).build();
    }

    private void set(String key, String value) {
        readDb();
        db.put(key, value);
        writeDb();

        response = Response.builder().response(OperationStatus.OK.name()).build();
    }

    private void get(String key) {
        readDb();
        if (db.containsKey(key)) {
            response = Response.builder()
                    .response(OperationStatus.OK.name())
                    .value(db.get(key))
                    .build();
        } else {
            response = Response.builder()
                    .response(OperationStatus.ERROR.name())
                    .reason(Reason.NO_SUCH_KEY.getMessage())
                    .build();
        }
    }

    private void delete(String key) {
        readDb();
        if (db.containsKey(key)) {
            db.remove(key);
            writeDb();
            response = Response.builder().response(OperationStatus.OK.name()).build();
        } else {
            response = Response.builder()
                    .response(OperationStatus.ERROR.name())
                    .reason(Reason.NO_SUCH_KEY.getMessage())
                    .build();
        }
    }

    private void readDb() {
            db = dbHandler.read();
    }

    private void writeDb() {
            dbHandler.write(db);
    }

    private String serializeResponse(Response response) {
        return gson.toJson(response);
    }
}
