package io.fobo66.crypto

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.transform.theme
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.path
import java.io.IOException
import java.math.BigInteger
import kotlin.io.path.readText

const val RADIX = 16

class Lab1 : CliktCommand() {
    val inputFile by option("-f", "--file")
        .path(mustBeReadable = true, canBeDir = false, mustExist = true)
        .help { theme.info("Input file") }

    val key by option("-k", "--key")
        .help { theme.info("Encryption key") }
        .default("password")

    val mode by option("-m", "--mode")
        .choice("ECB" to DESMode.ECB, "CBC" to DESMode.CBC, "CFB" to DESMode.CFB, "OFB" to DESMode.OFB)
        .help { theme.info("Operation mode for DES (ECB, CBC, CFB, OFB)") }
        .default(DESMode.ECB)

    override fun run() {
        var clearText = "Hello World!"

        try {
            if (inputFile != null) {
                echo("Reading cleartext from file $inputFile...")
                clearText = inputFile?.readText().orEmpty()
            }
            echo("Using $mode mode of operation...")
            val encryptedText = DES.encrypt(clearText.toByteArray(), key.toByteArray(), mode)
            val decryptedText = DES.decrypt(encryptedText, key.toByteArray(), mode)
            printResults(clearText, encryptedText, decryptedText)
        } catch (e: IOException) {
            throw IllegalStateException("Failed to read cleartext from file", e)
        }
    }

    private fun printResults(clearText: String, encryptedText: ByteArray, decryptedText: ByteArray) {
        echo("Clear text: $clearText")
        echo("Encrypted text: ${BigInteger(1, encryptedText).toString(RADIX)}")
        echo("Decrypted text: ${decryptedText.toString(Charsets.UTF_8)}")
    }

}

fun main(args: Array<String>) = Lab1().main(args)
