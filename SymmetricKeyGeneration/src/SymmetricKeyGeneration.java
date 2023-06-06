import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SymmetricKeyGeneration {
    private static final int KEY_SIZE = 256; // Key size in bits
    private static final int NUM_KEYS = 3; // Number of symmetric keys to generate

    public static void main(String[] args) {
        try {
            // Generate symmetric keys
            Key[] symmetricKeys = generateSymmetricKeys();

            // Write the symmetric keys to the file
            writeToFile(symmetricKeys, "symmetrickeys.txt");

            System.out.println("Symmetric keys generated and saved to symmetrickeys.txt.");
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private static Key[] generateSymmetricKeys() throws NoSuchAlgorithmException {
        Key[] symmetricKeys = new Key[NUM_KEYS];

        for (int i = 0; i < NUM_KEYS; i++) {
            symmetricKeys[i] = generateSymmetricKey();
        }

        return symmetricKeys;
    }

    private static Key generateSymmetricKey() throws NoSuchAlgorithmException {
        // Generate a secure random key
        SecureRandom secureRandom = new SecureRandom();
        byte[] keyBytes = new byte[KEY_SIZE / 8];
        secureRandom.nextBytes(keyBytes);

        // Create a key specification from the generated key bytes
        return new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
    }

    private static void writeToFile(Key[] keys, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 0; i < keys.length; i++) {
                writer.write("Symmetric Key " + (i + 1) + ": " + convertToBase64(keys[i].getEncoded()) + System.lineSeparator());
            }
        }
    }

    private static String convertToBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
