import java.io.FileWriter;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class RSAKeyPairGenerator {
    private static final int KEY_SIZE = 1024;
    private static final String OUTPUT_FILE = "keys.txt";

    public static void main(String[] args) {
        generateKeyPairs();
    }

    public static void generateKeyPairs() {
        try {
            // Generate key pair for the server
            KeyPair serverKeyPair = generateKeyPair();
            System.out.println("Server Public Key: " + serverKeyPair.getPublic());
            System.out.println("Server Private Key: " + serverKeyPair.getPrivate());
            System.out.println();

            // Generate key pair for the first user
            KeyPair user1KeyPair = generateKeyPair();
            System.out.println("User 1 Public Key: " + user1KeyPair.getPublic());
            System.out.println("User 1 Private Key: " + user1KeyPair.getPrivate());
            System.out.println();

            // Generate key pair for the second user
            KeyPair user2KeyPair = generateKeyPair();
            System.out.println("User 2 Public Key: " + user2KeyPair.getPublic());
            System.out.println("User 2 Private Key: " + user2KeyPair.getPrivate());

            // Write the key pairs to a text file
            writeKeyPairsToFile(serverKeyPair, user1KeyPair, user2KeyPair);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    private static void writeKeyPairsToFile(KeyPair serverKeyPair, KeyPair user1KeyPair, KeyPair user2KeyPair) throws IOException {
        try (FileWriter writer = new FileWriter(OUTPUT_FILE)) {
            writer.write("Server Public Key:\n" + serverKeyPair.getPublic() + "\n");
            writer.write("Server Private Key:\n" + serverKeyPair.getPrivate() + "\n\n");

            writer.write("User 1 Public Key:\n" + user1KeyPair.getPublic() + "\n");
            writer.write("User 1 Private Key:\n" + user1KeyPair.getPrivate() + "\n\n");

            writer.write("User 2 Public Key:\n" + user2KeyPair.getPublic() + "\n");
            writer.write("User 2 Private Key:\n" + user2KeyPair.getPrivate() + "\n");
        }
        System.out.println("Key pairs written to " + OUTPUT_FILE);
    }
}
