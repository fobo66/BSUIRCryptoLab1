package io.fobo66.crypto;

import org.apache.commons.cli.*;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Lab1 {

    public static void main(String[] args) {
        String clearText = "Hello World!";
        String key = "password";
        DESMode mode = DESMode.ECB;

        Options options = new Options();
        options.addOption("f", "file", true, "Input file");
        options.addOption("key", true, "Encryption key");
        options.addOption("m", "mode", true, "Operation mode for DES (ECB, CBC, CFB, OFB)");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("file")) {
                String filePath = cmd.getOptionValue("file");
                System.out.format("Reading cleartext from file %s...%n", filePath);
                clearText = loadClearTextFromFile(filePath);
            }

            if (cmd.hasOption("key")) {
                key = cmd.getOptionValue("key");
            }

            if (cmd.hasOption("m")) {
                mode = loadDESMode(cmd);
            }

            byte[] encryptedText = DES.encrypt(clearText.getBytes(), key.getBytes(), mode);
            byte[] decryptedText = DES.decrypt(encryptedText, key.getBytes(), mode);

            printResults(clearText, encryptedText, decryptedText);
        } catch (ParseException e) {
            System.err.println("Failed to parse command line arguments. Reason: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read cleartext from file", e);
        }
    }

    private static String loadClearTextFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    private static DESMode loadDESMode(CommandLine cmd) {
        return DESMode.valueOf(cmd.getOptionValue("m").toUpperCase());
    }

    private static void printResults(String clearText, byte[] encryptedText, byte[] decryptedText) {
        System.out.println("Clear text: " + clearText);
        System.out.println("Encrypted text: " + DatatypeConverter.printHexBinary(encryptedText));
        System.out.println("Decrypted text: " + new String(decryptedText));
    }
}
