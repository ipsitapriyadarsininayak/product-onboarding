package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;

public class NotepadReader {

//    private static final String FILE_PATH = System.getProperty("user.home") + "/Desktop/PdxValidCredentials.txt";
    private static final String FILE_PATH_INVALID = System.getProperty("user.home") + "/Desktop/InvalidCredentials.txt";


    public static String getDecodePassword(String filepath) throws IOException {
        String decodedPassword = null;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String encodedPassword = br.readLine();

            if (encodedPassword == null || encodedPassword.trim().isEmpty()) {
                throw new RuntimeException("password file is empty or not found!");
            }

            decodedPassword = new String(Base64.getDecoder().decode(encodedPassword));

            if (decodedPassword == null || decodedPassword.isEmpty()) {
                throw new RuntimeException("decoded password is empty!");

            }

        } catch (IOException e) {
            throw new RuntimeException("Error read password file" + e.getMessage());
        } catch (IllegalStateException e) {
            throw new RuntimeException("Invalid Base64 encoded password.", e);
        }
        return decodedPassword;
    }

    public static String getInvalidPassword() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(FILE_PATH_INVALID));
        String encodedPassword = br.readLine();
        return encodedPassword;

    }
}