package com.example.sw0b_001.Security;


import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

public class RSAProtocol extends AsymmetricSecurity {
    private OAEPParameterSpec encryptionParameter = new OAEPParameterSpec(
            "SHA-256", "MGF1", MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT);
    private KeyPairGenerator keyPairGenerator;
    private KeyStore keyStore;

    private static final String DEFAULT_KEYPAIR_ALGORITHM_PADDING = "RSA/ECB/" + KeyProperties.ENCRYPTION_PADDING_RSA_OAEP;
    private String keyStoreProvider;
    private String keyStoreAlias;


    public RSAProtocol(String keyStoreProvider, String keyStoreAlias) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException {
        this.keyStoreProvider = keyStoreProvider;
        this.keyStoreAlias = keyStoreAlias;

        this.keyStore = KeyStore.getInstance(this.keyStoreAlias);
        this.keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, this.keyStoreProvider);

        this.keyPairGenerator.initialize(
                new KeyGenParameterSpec.Builder(
                        this.keyStoreAlias,
                        KeyProperties.PURPOSE_DECRYPT )
                        .setDigests(KeyProperties.DIGEST_SHA256)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                        .build());
    }

    public byte[] getPublicKeyEncoded() throws KeyStoreException {
        PublicKey publicKey = this.keyStore.getCertificate(this.keyStoreAlias).getPublicKey();
        return publicKey.getEncoded();
    }

    public PublicKey getPublicKey() throws KeyStoreException {
        PublicKey publicKey = this.keyStore.getCertificate(this.keyStoreAlias).getPublicKey();
        return publicKey;
    }

    public void delete() throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance(this.keyStoreProvider);
        keyStore.load(null);
        keyStore.deleteEntry(this.keyStoreAlias);
    }

    public byte[] encrypt(byte[] input) throws NoSuchPaddingException, NoSuchAlgorithmException, KeyStoreException , InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(DEFAULT_KEYPAIR_ALGORITHM_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, this.getPublicKey(), this.encryptionParameter);
        return cipher.doFinal(input);
    }

    // Requirements to use this: input has to be Base64 encoded
    public byte[] decrypt(byte[] input){
        byte[] decryptedBytes = null;
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)this.keyStore.getEntry(this.keyStoreAlias, null);
            PrivateKey privateKey = privateKeyEntry.getPrivateKey();
            Cipher cipher = Cipher.getInstance(DEFAULT_KEYPAIR_ALGORITHM_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey, this.encryptionParameter);
            decryptedBytes = cipher.doFinal(input);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return decryptedBytes;
    }
}
