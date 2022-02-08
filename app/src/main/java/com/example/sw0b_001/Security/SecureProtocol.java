package com.example.sw0b_001.Security;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.security.keystore.KeyProtection;
import android.util.Base64;

import android.content.Context;

import com.example.sw0b_001.Helpers.GatewayValues;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;

public class SecureProtocol {
    private Cipher cipher;
    private SecretKeySpec key;
    private IvParameterSpec iv;
    private KeyStore keyStore;

    private SharedPreferences preferences;
    OAEPParameterSpec param = new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
    public SecureProtocol() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        public static final String DEFAULT_KEYPAIR_ALGORITHM_PADDING = "RSA/ECB/" + KeyProperties.ENCRYPTION_PADDING_RSA_OAEP;
        public static final String DEFAULT_KEYSTORE_ALIAS = "DEFAULT_SWOB_KEYSTORE";
        public static String DEFAULT_KEYSTORE_PROVIDER = "AndroidKeyStore";
        this.keyStore = KeyStore.getInstance(DEFAULT_KEYSTORE_PROVIDER);
        this.keyStore.load(null);
    }

    public SecureProtocol(Context context) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        this.keyStore = KeyStore.getInstance(DEFAULT_KEYSTORE_PROVIDER);
        this.keyStore.load(null);

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean hasKeyPairs(Context context) throws KeyStoreException {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.contains(GatewayValues.SHARED_KEY) && this.keyStore.containsAlias(DEFAULT_KEYSTORE_ALIAS) && preferences.contains(GatewayValues.VAR_PASSWDHASH);
    }

    private PublicKey getPublicKey() throws KeyStoreException {
        PublicKey publicKey = this.keyStore.getCertificate(DEFAULT_KEYSTORE_ALIAS).getPublicKey();
        return publicKey;
    }

    public String init() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, IOException, NoSuchPaddingException, UnrecoverableKeyException, KeyStoreException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        this.init_RSA();
        PublicKey pk = this.keyStore.getCertificate(DEFAULT_KEYSTORE_ALIAS).getPublicKey();
        return Base64.encodeToString(pk.getEncoded(), Base64.DEFAULT);
    }

    public String generateRandom(int length) {
//        char[] charsArray = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '#', '$', '%', '^', '*'};
        char[] charsArray = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        SecureRandom rand = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(charsArray[rand.nextInt(charsArray.length)]);
        }
        return password.toString();
    }

    public byte[] getIV() {
        // return this.iv.getIV();
        return this.cipher.getIV();
    }

    public boolean storeSecretKey(byte[] secretKey) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        SecretKey key = new SecretKeySpec(secretKey, "AES");
        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(key);
        KeyStore ks = KeyStore.getInstance(DEFAULT_KEYSTORE_PROVIDER);
        ks.load(null);
        this.keyStore.setEntry(GatewayValues.SHARED_KEY, skEntry, new KeyProtection.Builder(KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                .build());
        return true;
    }

    public boolean authenticate(String password) throws NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        byte[] hsPasswd = hash_sha512(password);

        String passwdHash = preferences.getString(GatewayValues.VAR_PASSWDHASH, null);
        passwdHash = new String(decrypt_RSA(passwdHash.getBytes()));

        return new String(hsPasswd).toUpperCase().equals(passwdHash.toUpperCase());

    }

    public boolean authenticate(String password, byte[] passwd1) throws NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        return new String(hash_sha512(password)).toUpperCase().equals(new String(passwd1).toUpperCase());
    }
}
