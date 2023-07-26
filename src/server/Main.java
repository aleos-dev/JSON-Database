package server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(5);
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static volatile boolean isRunning = true;
    private static ServerSocket server;

    public static void main(String[] args) {
        DataBase db = new DataBase();

        try {
            server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS));
            System.out.println("Server started!");

            while (isRunning) {
                waitSession(server.accept(), db);
            }
        } catch (SocketException e) {
            if (!isRunning) {
                System.out.println("Server is shutting down...");
            } else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            EXECUTOR.shutdown();
        }
    }

    private static void waitSession(Socket socket, DataBase db) {
        try {
            var output = new ObjectOutputStream(socket.getOutputStream());
            var input = new ObjectInputStream(socket.getInputStream());

            String receivedJson = (String) input.readObject();
            JsonObject request = JsonParser.parseString(receivedJson).getAsJsonObject();

            EXECUTOR.submit(() -> {
                try {
                    String response = db.handle(request);
                    output.writeObject(response);

                    if ("exit".equalsIgnoreCase(request.get("type").getAsString())) {
                        isRunning = false;
                        server.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    input.close();
                    output.close();
                    socket.close();
                }
                return null;
            });

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}