package io.fobo66.crypto;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Arrays;
import javax.xml.bind.DatatypeConverter;

public class Lab1 {

    public static void main (String[] args) {
        String clearText = "Hello World!";
        String key = "password";
        DESMode mode = DESMode.ECB;
        
        Options options = new Options();
        options.addOption("file", true, "Input file");
        options.addOption("key", true, "Encryption key");
        options.addOption("m", "mode", true, "Encryption mode for DES (ECB, CBC, CFB, OFB)");

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

            byte[] encryptedText = DES.encrypt(clearText.getBytes(), key.getBytes());
            byte[] decryptedText = DES.decrypt(encryptedText, key.getBytes());

            System.out.println(DatatypeConverter.printHexBinary(encryptedText));
            System.out.println(new String(decryptedText));
        } catch (ParseException e) {
            System.err.println( "Parsing failed.  Reason: " + e.getMessage() );
        }
    }

    private static String loadClearTextFromFile(String optionValue) {
        return "";
    }

    private static DESMode loadDESMode(CommandLine cmd) {
        return DESMode.valueOf(cmd.getOptionValue("m").toUpperCase());
    }
}
