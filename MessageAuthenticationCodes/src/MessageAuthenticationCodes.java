import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.io.FileWriter;
import java.io.IOException;

public class MessageAuthenticationCodes {
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String SECRET_KEY = "jaB7BdldXuvTOngjpuPTHtoV9TpjLBoRkdq0Gxlr/CU=";

    public static void main(String[] args) {
        try {
            // Generate HMAC-SHA256 message authentication code
            String hmacSha256 = generateHmacSha256(SECRET_KEY, "YourMessage");

            // Generate new key using HMAC-SHA256 on 𝐾𝑆
            String newKey = generateNewKey(SECRET_KEY);

            // Print output to a text file
            writeToFile(hmacSha256, newKey, "message_authentication_codes.txt");
        } catch (NoSuchAlgorithmException | InvalidKeyException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateHmacSha256(String key, String message)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
        mac.init(secretKeySpec);
        byte[] macBytes = mac.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(macBytes);
    }

    private static String generateNewKey(String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
        mac.init(secretKeySpec);
        byte[] newKeyBytes = mac.doFinal(key.getBytes());
        return Base64.getEncoder().encodeToString(newKeyBytes);
    }

    private static void writeToFile(String hmacSha256, String newKey, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("HMAC-SHA256: " + hmacSha256 + System.lineSeparator());
            writer.write("New Key: " + newKey);
        }
    }
}
