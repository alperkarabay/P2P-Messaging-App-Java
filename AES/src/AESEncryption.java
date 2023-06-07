import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AESEncryption {
    private static final String MESSAGE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sodales mauris ac tristique.";
    private static final String AES_KEY = "0123456789abcdef0123456789abcdef"; // 256-bit AES key in hexadecimal

    public static void main(String[] args) {
        try {
            // Generate a random Initialization Vector (IV)
            byte[] iv = generateIV();

            // Encrypt the message with AES in CBC mode
            byte[] ciphertext = encryptMessage(MESSAGE, AES_KEY, iv);

            // Decrypt the ciphertext
            String decryptedMessage = decryptMessage(ciphertext, AES_KEY, iv);

            // Print the ciphertext and decrypted message
            System.out.println("Ciphertext: " + convertToBase64(ciphertext));
            System.out.println("Decrypted Message: " + decryptedMessage);
            // Change the IV
            byte[] newIV = generateIV();

            // Encrypt the message again with the new IV
            byte[] newCiphertext = encryptMessage(MESSAGE, AES_KEY, newIV);
            // Write the output to a file
            writeToFile(convertToBase64(ciphertext), decryptedMessage, convertToBase64(newCiphertext), "aes_encryption.txt");

            System.out.println("Output saved to aes_encryption.txt.");


            // Print the new ciphertext
            System.out.println("New Ciphertext (with different IV): " + convertToBase64(newCiphertext));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException |
                 IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] generateIV() {
        byte[] iv = new byte[16]; // 128-bit IV
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private static byte[] encryptMessage(String message, String key, byte[] iv) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(hexStringToByteArray(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        return cipher.doFinal(message.getBytes());
    }

    private static String decryptMessage(byte[] ciphertext, String key, byte[] iv) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(hexStringToByteArray(key), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] decryptedBytes = cipher.doFinal(ciphertext);
        return new String(decryptedBytes);
    }

    private static void writeToFile(String ciphertext, String decryptedMessage, String newCiphertext, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Ciphertext: " + ciphertext + System.lineSeparator());
            writer.write("Decrypted Message: " + decryptedMessage + System.lineSeparator());
            writer.write("New Ciphertext (with different IV): " + newCiphertext);
        }
    }


    private static byte[] hexStringToByteArray(String hexString) {
        int length = hexString.length();
        byte[] byteArray = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            byteArray[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return byteArray;
    }

    private static String convertToBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
