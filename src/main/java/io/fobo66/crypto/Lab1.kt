package io.fobo66.crypto

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.io.IOException
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

const val RADIX = 16

fun main(args: Array<String>) {
    var clearText = "Hello World!"
    val mode: DESMode

    val parser = ArgParser("lab1")

    val inputFile by parser.option(
        ArgType.String,
        fullName = "file",
        shortName = "f",
        description = "Input file"
    )
    val key by parser.option(
        ArgType.String,
        fullName = "key",
        shortName = "k",
        description = "Encryption key"
    ).default("password")
    val modeKey by parser.option(
        ArgType.String,
        fullName = "mode",
        shortName = "m",
        description = "Operation mode for DES (ECB, CBC, CFB, OFB)"
    ).default("ECB")

    try {
        parser.parse(args)
        if (inputFile != null) {
            val filePath = inputFile!!
            println("Reading cleartext from file $filePath...")
            clearText = loadClearTextFromFile(filePath)
        }
        mode = loadDESMode(modeKey)
        println("Using $mode mode of operation...")
        val encryptedText = DES.encrypt(clearText.toByteArray(), key.toByteArray(), mode)
        val decryptedText = DES.decrypt(encryptedText, key.toByteArray(), mode)
        printResults(clearText, encryptedText, decryptedText)
    } catch (e: IOException) {
        throw IllegalStateException("Failed to read cleartext from file", e)
    }
}

@Throws(IOException::class)
private fun loadClearTextFromFile(filePath: String): String {
    return String(Files.readAllBytes(Paths.get(filePath)))
}

private fun loadDESMode(modeKey: String?): DESMode {
    return DESMode.valueOf(modeKey?.uppercase(Locale.getDefault()) ?: "ECB")
}

private fun printResults(clearText: String, encryptedText: ByteArray, decryptedText: ByteArray) {
    println("Clear text: $clearText")
    println("Encrypted text: " + BigInteger(1, encryptedText).toString(RADIX))
    println("Decrypted text: " + decryptedText.toString(Charsets.UTF_8))
}
