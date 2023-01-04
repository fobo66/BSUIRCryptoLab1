package io.fobo66.crypto

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlin.test.assertEquals

class Lab1Test {
    @kotlin.test.Test
    fun `DES encryption works`() {
        val encoder = Base64.getEncoder()
        val key = SecretKeySpec(KEY.toByteArray(), "DES")
        val cipher = Cipher.getInstance("DES")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val expectedEncryptionResult = cipher.doFinal(CLEARTEXT.toByteArray())

        val encryptionResult = DES.encrypt(CLEARTEXT.toByteArray(), KEY.toByteArray(), DESMode.ECB)
        assertEquals(encoder.encodeToString(expectedEncryptionResult), encoder.encodeToString(encryptionResult))
    }

    companion object {
        private const val CLEARTEXT = "test test test"
        private const val KEY = "testtest"
    }
}
