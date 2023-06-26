package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;

    public static void main(String[] args) {

        try (
                ServerSocket server = new ServerSocket(PORT, 1, InetAddress.getByName(ADDRESS))
        ) {
            System.out.println("Server started!");
            try (
                    Socket socket = server.accept();
                    DataInputStream input = new DataInputStream(socket.getInputStream());
                    DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                String inMsg = input.readUTF();
                System.out.printf("Received: %s%n", inMsg);

                String recordNumber = inMsg.substring(inMsg.lastIndexOf(" "));
                String outMsg = "A record #" + recordNumber + " was sent!";
                output.writeUTF(outMsg);
                System.out.printf("Sent: %s%n", outMsg);
            }

        } catch (IOException ignored) {

        }
    }

}
