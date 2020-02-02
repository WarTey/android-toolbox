package fr.isen.guillaume.androidtoolbox.security

import android.content.SharedPreferences
import android.util.Base64
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.PKCS8EncodedKeySpec
import javax.crypto.Cipher

class DataSecurity {

    fun generateKeyPair(sharedPref: SharedPreferences): PublicKey {
        val keyPair = KeyPairGenerator.getInstance(ALGORITHM).genKeyPair()
        savePrivateKey(sharedPref, keyPair.private)
        return keyPair.public
    }

    fun savePrivateKey(sharedPref: SharedPreferences, privateKey: PrivateKey) {
        val encodedKey = Base64.encodeToString(privateKey.encoded, Base64.NO_WRAP)
        sharedPref.edit().putString("key", encodedKey).apply()
    }

    fun getSecretKey(sharedPref: SharedPreferences): PrivateKey {
        val key = sharedPref.getString("key", null)
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(PKCS8EncodedKeySpec(Base64.decode(key, Base64.NO_WRAP)))
    }

    fun encrypt(sharedPref: SharedPreferences, fileData: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, generateKeyPair(sharedPref))
        return cipher.doFinal(fileData)
    }

    fun decrypt(sharedPref: SharedPreferences, fileData: ByteArray): String {
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(sharedPref))
        return String(cipher.doFinal(fileData))
    }

    fun readFile(filePath: String): ByteArray {
        val file = File(filePath)
        val fileContents = file.readBytes()
        val inputBuffer = BufferedInputStream(FileInputStream(file))

        inputBuffer.read(fileContents)
        inputBuffer.close()

        return fileContents
    }

    companion object {
        private const val ALGORITHM = "RSA"
        private const val CIPHER_TRANSFORMATION = "RSA/ECB/OAEPPadding"
    }
}