package client.menu;

import com.google.gson.Gson;
import server.ClientRequestHandler;

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

            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(args);
            output.writeObject(gson.toJson(clientRequestHandler));
            System.out.printf("Sent: %s\n", clientRequestHandler.convertRequestToJsonString());

            String messageIn = (String) input.readObject();
            System.out.printf("Received: %s\n", messageIn);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
