package server;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;


    public static void main(String[] args) {
        DataBase requestHandler = new DataBase();

        try (
                var server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))
        ) {
            System.out.println("Server started!");

            ArgsHandler argsHandler;
            String jsonString;
            do {
                try (
                        Socket socket = server.accept();
                        var output = new ObjectOutputStream(socket.getOutputStream());
                        var input = new ObjectInputStream(socket.getInputStream())
                ) {
                    jsonString = (String) input.readObject();
                    argsHandler = new Gson().fromJson(jsonString, ArgsHandler.class);

                    String response = requestHandler.handle(argsHandler);
                    output.writeObject(response);

                }

            } while (!argsHandler.containExitCommand());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}
