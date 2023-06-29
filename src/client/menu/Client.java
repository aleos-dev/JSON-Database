package client.menu;

import server.ArgsHandler;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        ) {
            System.out.println("Client started!");

            ArgsHandler arguments = new ArgsHandler(args);
            output.writeObject(arguments);
            System.out.printf("Sent: %s\n", arguments.getRequest());

            String msg = (String) input.readObject();
            System.out.printf("Received: %s\n", msg);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
