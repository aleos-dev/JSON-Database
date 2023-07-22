package server;

import com.google.gson.Gson;

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
    private static final String ADDRESS = "127.0.0.1";
    private static final int PORT = 23456;
    private static ServerSocket SERVER_SOCKET;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        DataBase db = new DataBase();

        try (var server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))) {
            System.out.println("Server started!");
            SERVER_SOCKET = server;

            while (true) {
                waitSession(server.accept(), db);
            }
        } catch (SocketException e) {
            /* IGNORED */
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            EXECUTOR.shutdown();
        }
    }

    private static void waitSession(Socket socket, DataBase db) {
        ClientRequest clientRequestHandler;

        try {
            var output = new ObjectOutputStream(socket.getOutputStream());
            var input = new ObjectInputStream(socket.getInputStream());

            String request = (String) input.readObject();
            clientRequestHandler = new Gson().fromJson(request, ClientRequest.class);

            EXECUTOR.submit(() -> {
                try {
                    String response = db.handle(clientRequestHandler);
                    output.writeObject(response);

                    if (clientRequestHandler.containExitCommand()) {
                        SERVER_SOCKET.close();
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