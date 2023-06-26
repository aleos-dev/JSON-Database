package client.menu;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(InetAddress.getByName(ADDRESS), PORT);
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        ) {
            System.out.println("Client started!");

            String request = "Give me a record # 12";
            System.out.printf("Sent: %s%n", request);
            output.writeUTF(request);

            String msg = input.readUTF();
            System.out.printf("Received: %s%n", msg);

        } catch (IOException ignored) {

        }

    }
}
