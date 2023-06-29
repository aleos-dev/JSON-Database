package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private final static String ADDRESS = "127.0.0.1";
    private final static int PORT = 23456;


    public static void main(String[] args) {
        List<String> bd = Stream.generate(() -> "").limit(1000).collect(Collectors.toList());
        DataBase requestHandler = new DataBase(bd);

        try (
                var server = new ServerSocket(PORT, 50, InetAddress.getByName(ADDRESS))
        ) {
            System.out.println("Server started!");

            ArgsHandler arguments;
            do {
                try (
                        Socket socket = server.accept();
                        var output = new ObjectOutputStream(socket.getOutputStream());
                        var input = new ObjectInputStream(socket.getInputStream())
                ) {
                    arguments = (ArgsHandler) input.readObject();

                    String response = requestHandler.handle(arguments);
                    output.writeObject(response);

                    if (arguments.containExitCommand()) {
                        break;
                    }
                }

            } while (!arguments.containExitCommand());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
