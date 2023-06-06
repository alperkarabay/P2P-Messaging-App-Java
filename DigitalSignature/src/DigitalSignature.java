import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.util.Base64;

public class DigitalSignature {
    private static final String MESSAGE = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sodales mauris ac tristique.";

    public static void main(String[] args) {
        try {
            // Generate RSA key pair
            KeyPair keyPair = generateKeyPair();

            // Generate digital signature
            byte[] signature = generateDigitalSignature(keyPair.getPrivate());

            // Verify the digital signature
            boolean isSignatureValid = verifyDigitalSignature(keyPair.getPublic(), signature);

            // Print message, message hash, and digital signature
            System.out.println("Message: " + MESSAGE);
            System.out.println("Message Hash (SHA-256): " + calculateHash(MESSAGE));
            System.out.println("Digital Signature: " + convertToBase64(signature));
            System.out.println("Digital Signature Verification: " + (isSignatureValid ? "Valid" : "Invalid"));

            // Write the output to a file
            writeToFile(MESSAGE, calculateHash(MESSAGE), signature, isSignatureValid, "digital_signature.txt");

            System.out.println("Output saved to digital_signature.txt.");
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException e) {
            e.printStackTrace();
        }
    }

    private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private static byte[] generateDigitalSignature(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(MESSAGE.getBytes());
        return signature.sign();
    }

    private static boolean verifyDigitalSignature(PublicKey publicKey, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature verifySignature = Signature.getInstance("SHA256withRSA");
        verifySignature.initVerify(publicKey);
        verifySignature.update(MESSAGE.getBytes());
        return verifySignature.verify(signature);
    }

    private static String calculateHash(String message) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(message.getBytes());
        return convertToBase64(hashBytes);
    }

    private static void writeToFile(String message, String hash, byte[] signature, boolean isSignatureValid, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("Message: " + message + System.lineSeparator());
            writer.write("Message Hash (SHA-256): " + hash + System.lineSeparator());
            writer.write("Digital Signature: " + convertToBase64(signature) + System.lineSeparator());
            writer.write("Digital Signature Verification: " + (isSignatureValid ? "Valid" : "Invalid"));
        }
    }

    private static String convertToBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
