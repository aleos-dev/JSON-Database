package server;

import com.google.gson.Gson;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;

    private Map<String, String> data;

    Main() {
        data = new HashMap<>(1000);
    }

    private Response noSuchKeyResponse() {
        return Response.builder()
                .response("ERROR")
                .reason("No such key")
                .build();
    }

    private Response set(String string, String key) {
        data.put(key, string);
        return Response.builder().response("OK").build();
    }

    private Response get(String key) {
        if (!data.containsKey(key) || data.get(key).isEmpty()) {
            return noSuchKeyResponse();
        } else {
            return Response.builder()
                    .response("OK")
                    .value(data.get(key))
                    .build();
        }
    }

    private Response delete(String key) {
        if (!data.containsKey(key)) {
            return noSuchKeyResponse();
        } else {
            data.remove(key);
            return Response.builder().response("OK").build();
        }
    }

    void run() {
        try (ServerSocket server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))){
            System.out.println("Server started!");
            boolean running = true;
            Gson gson = new Gson();
            while (running) {
                Socket client = server.accept();
                DataInputStream input = new DataInputStream(client.getInputStream());
                DataOutputStream output = new DataOutputStream(client.getOutputStream());

                String requestJson = input.readUTF();
                Request request = gson.fromJson(requestJson, Request.class);
                System.out.printf("Received: %s%n", requestJson);

                switch (request.getType()) {
                    case "set": {
                        Response response = set(request.getValue(), request.getKey());
                        String responseString = gson.toJson(response);
                        System.out.printf("Sent: %s%n", responseString);
                        output.writeUTF(responseString);
                        break;
                    }
                    case "get": {
                        Response response = get(request.getKey());
                        String responseString = gson.toJson(response);
                        System.out.printf("Sent: %s%n", responseString);
                        output.writeUTF(responseString);
                        break;
                    }
                    case "delete": {
                        Response response = delete(request.getKey());
                        String responseString = gson.toJson(response);
                        System.out.printf("Sent: %s%n", responseString);
                        output.writeUTF(responseString);
                        break;
                    }
                    case "exit": {
                        String response = gson.toJson(Response.builder().response("OK").build());
                        output.writeUTF(response);
                        running = false;
                        break;
                    }
                    default:
                        throw new IllegalArgumentException("No action for " + request.getType());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.run();
    }
}