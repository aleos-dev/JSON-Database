package server;


import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class ArgsHandler implements Serializable {
    private Map<String, String> argsMap;

    public ArgsHandler(String[] args) {
        argsMap = parseArgs(args);

    }

    public DataBase.Operation getRequestType() {
        return DataBase.Operation.valueOf(argsMap.get("type").toUpperCase());
    }

    public String getKey() {
        return argsMap.get("key");
    }

    public String getMessage() {
        return argsMap.get("value");
    }

    public String getArgsAsGsonObject() {
        return new Gson().toJson(argsMap);
    }

    public boolean containExitCommand() {
        return "exit".equalsIgnoreCase(argsMap.get("type"));
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
