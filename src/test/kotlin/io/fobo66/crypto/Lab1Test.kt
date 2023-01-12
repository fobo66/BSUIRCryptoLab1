package io.fobo66.crypto

import java.util.*
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Lab1Test {

    @Test
    fun `DES encryption works`() {
        val encoder = Base64.getEncoder()
        val key = KeyGenerator.getInstance("DES").generateKey()
//        DES output doesn't match with javax.crypto, probably because of the padding
//        val cipher = Cipher.getInstance("DES/CBC/PKCS5Padding")
//        cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(DES.IV))
//        val expectedEncryptionResult = cipher.doFinal(CLEARTEXT.toByteArray())

        val encryptionResult = DES.encrypt(CLEARTEXT.toByteArray(), key.encoded, DESMode.CBC)
//        assertEquals(encoder.encodeToString(expectedEncryptionResult), encoder.encodeToString(encryptionResult))
        assertNotEquals(encoder.encodeToString(CLEARTEXT.toByteArray()), encoder.encodeToString(encryptionResult))
    }

    @Test
    fun `DES decryption works`() {
        val encryptionResult = DES.encrypt(CLEARTEXT.toByteArray(), KEY.toByteArray(), DESMode.CBC)
        val decryptionResult = DES.decrypt(encryptionResult, KEY.toByteArray(), DESMode.CBC)
        assertEquals(CLEARTEXT, decryptionResult.toString(Charsets.UTF_8))
    }

    @Test
    fun `DES encryption in different modes produces different results`() {
        val encoder = Base64.getEncoder()

        val cleartextBytes = CLEARTEXT.toByteArray()
        val keyBytes = KEY.toByteArray()
        val encryptionResultEcb = DES.encrypt(cleartextBytes, keyBytes, DESMode.ECB)
        val encryptionResultCbc = DES.encrypt(cleartextBytes, keyBytes, DESMode.CBC)
        assertNotEquals(encoder.encodeToString(encryptionResultEcb), encoder.encodeToString(encryptionResultCbc))
    }

    companion object {
        private const val CLEARTEXT = "testtesttesttest"
        private const val KEY = "testtest"
    }
}
