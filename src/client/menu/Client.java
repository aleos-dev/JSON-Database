package client.menu;

import com.google.gson.Gson;
import server.ArgsHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;

    public static void main(String[] args) {
        Gson gson = new Gson();

        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ) {
            System.out.println("Client started!");

            ArgsHandler argsHandler = new ArgsHandler(args);
            output.writeObject(gson.toJson(argsHandler));
            System.out.printf("Sent: %s\n", argsHandler.getArgsAsGsonObject());

            String messageIn = (String) input.readObject();
            System.out.printf("Received: %s\n", messageIn);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
