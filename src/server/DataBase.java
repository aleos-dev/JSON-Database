package server;


import java.util.List;
import java.util.Objects;


public class DataBase {
    enum OperationStatus {OK, ERROR}
    enum Operation {
        SET,
        GET,
        DELETE,
        EXIT;

    }

    List<String> data;

    public DataBase(List<String> data) {
        this.data = data;
    }

    public String handle(ArgsHandler arguments) {
        return switch (arguments.getRequestType()) {
            case SET -> set(arguments.getIndex(), arguments.getMessage());
            case GET -> get(arguments.getIndex());
            case DELETE -> delete(arguments.getIndex());
            case EXIT -> disconnect();
        };
    }

    private String disconnect() {
        return OperationStatus.OK.name();
    }

    private String set(int index, String value) {
        if (checkArrayIndexBound(index)) {
            return OperationStatus.ERROR.name();
        }

        data.set(index - 1, value);
        return OperationStatus.OK.name();
    }

    private String get(int index) {
        if (checkArrayIndexBound(index)) {
            return OperationStatus.ERROR.name();
        }

        String requestData = data.get(index - 1);
        return Objects.equals(requestData, "") ? OperationStatus.ERROR.name() : requestData;
    }

    private String delete(int index) {
        if (checkArrayIndexBound(index)) {
            return OperationStatus.ERROR.name();
        }

        return set(index, "");
    }

    private boolean checkArrayIndexBound(int index) {
        return index <= 0 || index > data.size();
    }
}
