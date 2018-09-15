package io.fobo66.crypto;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Lab1 {

    public static void main (String[] args) {
        String clearText = "Hello World!";
        String key = "password";
        
        Options options = new Options();
        options.addOption("file", true, "Input file");
        options.addOption("key", false, "Encryption key");
        options.addOption("mode", false, "Encryption mode for DES (CBC, E)");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("file")) {
                clearText = loadClearTextFromFile(cmd.getOptionValue("file"));
            }

            if (cmd.hasOption("key")) {
                key = cmd.getOptionValue("key");
            }

            byte[] cypherText = DES.encrypt(clearText.getBytes(), key.getBytes());
            byte[] decryptedCypherText = DES.decrypt(cypherText, key.getBytes());

            System.out.println(cypherText);
            System.out.println(new String(decryptedCypherText));
        } catch (ParseException e) {
            System.err.println( "Parsing failed.  Reason: " + e.getMessage() );
        }
    }

    private static String loadClearTextFromFile(String optionValue) {
        return "";
    }
}
