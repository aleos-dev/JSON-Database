package server;

import com.beust.jcommander.JCommander;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Request implements Serializable {
    //    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/client/data";
    private static final String BASE_PATH = System.getProperty("user.dir") + "/JSON Database (Java)/task/src/client/data";
    private static final Gson GSON = new Gson();

    private final JsonObject request;
    private final transient Path pathToFileWithRequest = Path.of(BASE_PATH);

    public Request(String[] args) {
        request = parseArgs(args);
    }

    public JsonObject get() {
        return request;
    }

    private JsonObject parseArgs(String[] args) {
        var cmdArgs = new CommandLineArgs();
        JCommander.newBuilder().addObject(cmdArgs).build().parse(args);

        if (cmdArgs.getFileName() != null) {
            return readRequestFromJsonFile(cmdArgs.getFileName());
        } else {
            var jsonObject = new JsonObject();
            if (cmdArgs.getType() != null) jsonObject.addProperty("type", cmdArgs.getType());
            if (cmdArgs.getKey() != null) jsonObject.addProperty("key", cmdArgs.getKey());
            if (cmdArgs.getValue() != null) jsonObject.addProperty("value", cmdArgs.getValue());
            return jsonObject;
        }
    }

    private JsonObject readRequestFromJsonFile(String fileName) {
        try {
            Path path = pathToFileWithRequest.resolve(fileName);
            String jsonData = Files.readString(path, StandardCharsets.UTF_8);

            return GSON.fromJson(jsonData, JsonObject.class);
        } catch (IOException ek) {
            System.out.println(("files readString error"));
        }
        return null;
    }

    @Override
    public String toString() {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return GSON.toJson(request);
    }
}
