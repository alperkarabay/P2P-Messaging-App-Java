
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

import static database.PostgreSql.connectDatabase;

public class Main {
    public static void main(String[] args) throws SQLException {
        connectDatabase();
        connectDatabase();
        final String serverIP = "localhost";
        final int serverPort = 8080;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

        try {
            // Connect to the server
            Socket socket = new Socket(serverIP, serverPort);
            System.out.println("Connected to the server.");

            // Create input/output streams for sending/receiving messages
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            // Send username to the server
            outputStream.writeUTF(username);
            outputStream.flush();

            // Start a new thread to listen for incoming messages
            Thread messageListener = new Thread(() -> {
                try {
                    while (true) {
                        String receivedMessage = inputStream.readUTF();
                        System.out.println(receivedMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            messageListener.start();

            // Send messages to the server
            String message;
            while (true) {
                message = scanner.nextLine();
                outputStream.writeUTF(message);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
