package server.jsonservice;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public class JsonService {
    private final Path path;

    public JsonService(Path path) {
        this.path = path;
    }

    public void write(JsonElement db) throws IOException {
        Gson gson = new Gson();

        // Ensure directory exists
        Files.createDirectories(path.getParent());

        Files.writeString(path, gson.toJson(db), StandardCharsets.UTF_8);
        System.out.println("Writing to: " + path.toAbsolutePath());
    }

    public JsonElement read() throws IOException {
        JsonElement database = null;

        String serializedJsonData = Files.readString(path, StandardCharsets.UTF_8);

        if (!serializedJsonData.isEmpty()) {
            database = JsonParser.parseString(serializedJsonData);
        }

        return database;
    }

}