package server;


import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientRequestHandler implements Serializable {
    private final Map<String, String> requestMap;

    public ClientRequestHandler(String[] args) {
        requestMap = parseArgs(args);

    }

    public DataBase.Operation getRequestType() {
        return DataBase.Operation.valueOf(requestMap.get("type").toUpperCase());
    }

    public String getKey() {
        return requestMap.get("key");
    }

    public String getMessage() {
        return requestMap.get("value");
    }

    public String convertRequestToJsonString() {
        return new Gson().toJson(requestMap);
    }

    public boolean containExitCommand() {
        return "exit".equalsIgnoreCase(requestMap.get("type"));
    }


    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-t" -> map.put("type", args[i + 1]);
                case "-k" -> map.put("key", args[i + 1]);
                case "-v" -> map.put("value", String.join(" ", Arrays.copyOfRange(args, i + 1, args.length)));
            }
            i++;
        }

        return map;
    }

}
