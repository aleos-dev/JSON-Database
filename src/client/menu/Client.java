package client.menu;

import com.google.gson.Gson;
import server.ClientRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;

    public static void main(String[] args) {
        try (Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT)) {
            System.out.println("Client started!");

            sendRequest(args, socket);
            getResponse(socket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(String[] args, Socket socket) throws IOException {
        Gson gson = new Gson();

        ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
        ClientRequest clientRequest = new ClientRequest(args);

        output.writeObject(gson.toJson(clientRequest));
        System.out.printf("Sent: %s\n", clientRequest.convertRequestToJsonString());
    }

    private static void getResponse(Socket socket) throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            String messageIn = (String) input.readObject();
            System.out.printf("Received: %s\n", messageIn);
        }
    }
}
