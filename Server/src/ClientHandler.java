import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ServerApp server;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private String username;

    public ClientHandler(Socket clientSocket, ServerApp server) {
        this.clientSocket = clientSocket;
        this.server = server;
        try {
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // Receive the username from the client
            username = inputStream.readUTF();
            System.out.println("User connected: " + username);

            // Broadcast the user joining message
            server.broadcastMessage(username + " joined the chat.",this);

            while (true) {
                String message = inputStream.readUTF();

                // Check if the message is empty, indicating client disconnect
                if (message.isEmpty()) {
                    break;
                }

                // Broadcast the received message
                server.broadcastMessage(username + ": " + message,this);
            }
        } catch (IOException e) {
            // Handle the broken pipe exception (client disconnect)
            System.out.println("User disconnected: " + username);
        } finally {
            // Remove the client from the server's active client list
            server.removeClient(this);
            closeConnection();
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
            if (clientSocket != null) {
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
