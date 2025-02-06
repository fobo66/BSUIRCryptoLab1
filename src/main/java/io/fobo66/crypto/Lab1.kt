package io.fobo66.crypto

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import java.io.IOException
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.readText

const val RADIX = 16

fun main(args: Array<String>) {
    var clearText = "Hello World!"

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
    val mode by parser.option(
        ArgType.Choice<DESMode>(),
        fullName = "mode",
        shortName = "m",
        description = "Operation mode for DES (ECB, CBC, CFB, OFB)"
    ).default(DESMode.ECB)

    try {
        parser.parse(args)
        if (inputFile != null) {
            val filePath = inputFile!!
            println("Reading cleartext from file $filePath...")
            clearText = loadClearTextFromFile(filePath)
        }
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
    return Paths.get(filePath).readText()
}

private fun printResults(clearText: String, encryptedText: ByteArray, decryptedText: ByteArray) {
    println("Clear text: $clearText")
    println("Encrypted text: " + BigInteger(1, encryptedText).toString(RADIX))
    println("Decrypted text: " + decryptedText.toString(Charsets.UTF_8))
}
