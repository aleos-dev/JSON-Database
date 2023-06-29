package server;


import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ArgsHandler implements Serializable {
    Map<String, String> argsMap;

    public ArgsHandler(String[] args) {
        argsMap = parseArgs(args);

    }
    public DataBase.Operation getRequestType() {
        return DataBase.Operation.valueOf(argsMap.get("-t").toUpperCase());
    }

    public int getIndex() {
        return Integer.parseInt(argsMap.get("-i"));
    }

    public String getMessage() {
        return argsMap.get("-m");
    }

    public String getRequest() {
        String type = argsMap.get("-t");

        String index = argsMap.get("-i");
        if (index == null) {
            return type;
        }
        String message = argsMap.get("-m");
        if (message == null) {
            return String.join(" ", type, index);
        }
        return String.join(" ", type, index, message);
    }

    public boolean containExitCommand() {
        return "exit".equalsIgnoreCase(argsMap.get("-t"));
    }


    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            if ("-m".equals(args[i])) {
                map.put(args[i], String.join(" ", Arrays.copyOfRange(args, i + 1, args.length)));
                break;
            }

            map.put(args[i], args[i + 1]);
            i++;
        }

        return map;
    }

}
