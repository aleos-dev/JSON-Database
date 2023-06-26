package client.menu;

import java.util.List;


public class MenuHandler {
    enum Status {OK, ERROR}
    List<String> data;

    public MenuHandler(List<String> data) {
        this.data = data;
    }

    public void handle(Operation operation, String[] userInput) {

        int index = Integer.parseInt(userInput[1]);

        String value = userInput.length == 3 ? userInput[2] : "";

        String result = "";
        try {
            result = switch (operation) {
                case SET -> setHandler(index, value);
                case GET -> getHandler(index);
                case DELETE -> deleteHandler(index);
                default -> "DEFAULT";
            };

        } catch (IllegalArgumentException ignored) {
        }

        System.out.println(result.isBlank() ? Status.ERROR : result);
    }


    private String setHandler(int index, String value) {
        if (checkArrayIndexBound(index)) {
            return "";
        }

        data.set(index - 1, value);
        return "OK";
    }

    private String getHandler(int index) {
        if (checkArrayIndexBound(index)) {
            return "";
        }

        return data.get(index - 1);
    }

    private String deleteHandler(int index) {
        if (checkArrayIndexBound(index)) {
            return "";
        }

        return setHandler(index, "");
    }


    private boolean checkArrayIndexBound(int index) {
        return index <= 0 || index > 100;
    }
}
