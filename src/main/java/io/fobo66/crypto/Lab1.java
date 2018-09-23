package io.fobo66.crypto;

import org.apache.commons.cli.*;

import javax.xml.bind.DatatypeConverter;

public class Lab1 {

    public static void main(String[] args) {
        String clearText = "Hello World!";
        String key = "password";
        DESMode mode = DESMode.ECB;

        Options options = new Options();
        options.addOption("file", true, "Input file");
        options.addOption("key", true, "Encryption key");
        options.addOption("m", "mode", true, "Operation mode for DES (ECB, CBC, CFB, OFB)");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("file")) {
                clearText = loadClearTextFromFile(cmd.getOptionValue("file"));
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
            System.err.println("Parsing failed. Reason: " + e.getMessage());
        }
    }

    private static String loadClearTextFromFile(String filePath) {
        return "";
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
