package it.polimi.ingsw.am42.network.tcp.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientTCP {

    private static String ip = "";
    private final int port;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public ClientTCP(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public static void main(String[] args) {
        final ClientTCP client = new ClientTCP(ip, 2345);
        try {
            client.startClient();
        } catch (final IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void startClient() throws IOException {
        final Socket socket = new Socket(ip, port);
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        final Scanner stdin = new Scanner(System.in);

        try {
            while (true) {
                final String inputLine = stdin.nextLine();
                socketOut.println(inputLine);
                socketOut.flush();
                final String socketLine = socketIn.nextLine();
                System.out.println(socketLine);
            }
        } catch (final NoSuchElementException e) {
            System.out.println("Connection closed");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
