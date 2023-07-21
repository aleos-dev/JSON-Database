package server;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ClientRequest implements Serializable {
    private final Map<String, String> requestMap;


    private final transient Gson gson = new Gson();
    private final String clientDataPath = System.getProperty("user.dir") + File.separator +
            "src" + File.separator +
            "client" + File.separator +
            "data";
    private final transient Path requestPath = Path.of(clientDataPath);

    public ClientRequest(String[] args) throws IOException {
        requestMap = parseArgs(args);

    }

    public String[] getClientRequestFromFile(String fileName) {
        return read(requestPath.resolve(fileName));
    }

    public DataBase.Operation getOperation() {
        return DataBase.Operation.valueOf(requestMap.get("type").toUpperCase());
    }

    public String getKey() {
        return requestMap.get("key");
    }

    public String getValue() {
        return requestMap.get("value");
    }

    public String convertRequestToJsonString() {
        return new Gson().toJson(requestMap);
    }

    public boolean containExitCommand() {
        return "exit".equalsIgnoreCase(requestMap.get("type"));
    }


    private Map<String, String> parseArgs(String[] args) throws IOException {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-t", "type" -> map.put("type", args[i + 1]);
                case "-k", "key" -> map.put("key", args[i + 1]);
                case "-v", "value" -> map.put("value", String.join(" ", Arrays.copyOfRange(args, i + 1, args.length)));
                case "-in" -> { return parseArgs(getClientRequestFromFile(args[i + 1])); }
            }
            i++;
        }

        return map;
    }


    private String[] read(Path path) {

        Map<String, String> database = new LinkedHashMap<>();

        try {
            String serializedData = Files.readString(path, StandardCharsets.UTF_8);

            if (!serializedData.isEmpty()) {
                Type dataType = new TypeToken<LinkedHashMap<String, String>>() {
                }.getType();
                database = gson.fromJson(serializedData, dataType);
            }
        } catch (IOException e) {
            System.out.println(("files readString error"));
        }


        return database.entrySet().stream()
                .flatMap(entry -> Stream.of(entry.getKey(), entry.getValue()))
                .toArray(String[]::new);
    }

}
