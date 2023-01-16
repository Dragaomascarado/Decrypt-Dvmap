import java.io.FileInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Decrypt {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Decrypt <file_path>");
            return;
        }

        String filePath = args[0];

        try {
            decryptFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void decryptFile(String filePath) throws IOException {
        // Read the file into a byte array
        byte[] fileBytes = readFile(filePath);

        // Set the first four bytes to the correct values for a zip file
        fileBytes[0] = 80;
        fileBytes[1] = 75;
        fileBytes[2] = 3;
        fileBytes[3] = 4;

        // Perform the XOR operations on the file's bytes
        for (int i = 4; i < fileBytes.length; i++) {
            if (i % 7 == 0) {
                fileBytes[i] = (byte) (fileBytes[i] ^ 43);
            } else if (i % 5 == 0) {
                fileBytes[i] = (byte) (fileBytes[i] ^ 53);
            } else if (i % 3 == 0) {
                fileBytes[i] = (byte) (fileBytes[i] ^ 59);
            } else if (i % 17 == 0) {
                fileBytes[i] = (byte) (fileBytes[i] ^ 17);
            } else if (i % 71 == 0) {
                fileBytes[i] = (byte) (fileBytes[i] ^ 71);
            } else {
                fileBytes[i] = (byte) (fileBytes[i] ^ 29);
            }
        }

        // Create a new file name with the .zip extension
        File originalFile = new File(filePath);
        String newFilePath = filePath.substring(0, filePath.lastIndexOf(".")) + ".zip";

        // Write the decrypted bytes to the new file
        FileOutputStream fileOutputStream = new FileOutputStream(new File(newFilePath));
        fileOutputStream.write(fileBytes);
        fileOutputStream.close();
    }

    private static byte[] readFile(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        byte[] fileBytes = new byte[inputStream.available()];
        inputStream.read(fileBytes);
        inputStream.close();
        return fileBytes;
    }
}
