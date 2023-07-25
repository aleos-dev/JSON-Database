package server;

import com.google.gson.*;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Request implements Serializable {
    private final JsonObject request;
    private final transient Path pathToFileWithRequest = Path.of(System.getProperty("user.dir") + "/src/client/data");

    public Request(String[] args) {
        request = parseArgs(args);
    }

    public JsonObject get() {
        return request;
    }

    public boolean isExitCommand() {
        return "exit".equalsIgnoreCase(request.get("type").getAsString());
    }

    private JsonObject parseArgs(String[] args) {
        Map<String, String> map = new LinkedHashMap<>();
        Gson gson = new Gson();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-t", "type" -> map.put("type", args[i + 1]);
                case "-k", "key" -> map.put("key", args[i + 1]);
                case "-v", "value" -> map.put("value", String.join(" ", Arrays.copyOfRange(args, i + 1, args.length)));
                case "-in" -> {
                    String fileName = args[i + 1];
                    return readRequestFromJsonFile(fileName);
                }
            }
            i++;
        }
        return gson.toJsonTree(map).getAsJsonObject();
    }

    private JsonObject readRequestFromJsonFile(String fileName) {
        Path path = pathToFileWithRequest.resolve(fileName);
        JsonElement jsonElement = null;

        try {
            String jsonData = Files.readString(path, StandardCharsets.UTF_8);
            jsonElement = JsonParser.parseString(jsonData);
        } catch (IOException e) {
            System.out.println(("files readString error"));
        }
        return jsonElement != null ? jsonElement.getAsJsonObject() : null;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(request);
    }
}
