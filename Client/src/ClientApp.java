import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientApp {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8080;

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    public static void main(String[] args) {
        ClientApp client = new ClientApp();
        client.start();
    }

    public void start() {
        try {
            socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Connected to the server.");

            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

            // Send username to the server
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();
            outputStream.writeUTF(username);
            outputStream.flush();

            // Start a new thread to listen for incoming messages from the server
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
            while (true) {
                String message = scanner.nextLine();
                outputStream.writeUTF(message);
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
