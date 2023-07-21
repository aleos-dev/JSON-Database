package server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;
    private final static ExecutorService EXECUTOR = Executors.newFixedThreadPool(5);

    public static void main(String[] args) {
        DataBase requestDataBase = new DataBase();

        try (
                var server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))
        ) {
            System.out.println("Server started!");

            boolean exitCommandReceived = false;
            while (!exitCommandReceived) {
                try {
                    Socket socket = server.accept();
                    var output = new ObjectOutputStream(socket.getOutputStream());
                    var input = new ObjectInputStream(socket.getInputStream());

                    String request = (String) input.readObject();
                    ClientRequest clientRequestHandler = new Gson().fromJson(request, ClientRequest.class);
                    exitCommandReceived = clientRequestHandler.containExitCommand();

                    EXECUTOR.submit(() -> {
                        try {
                            String response = requestDataBase.handle(clientRequestHandler);
                            output.writeObject(response);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            output.close();
                            input.close();
                            socket.close();
                        }
                        return null;
                    });


                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            EXECUTOR.shutdown();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}