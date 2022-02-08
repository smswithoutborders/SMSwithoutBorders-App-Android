package com.example.sw0b_001.Security;

import com.example.sw0b_001.Helpers.GatewayValues;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Symmetric {

    public static class AES {

        public static byte[] encryptCBC(byte[] input, byte[] iv, byte[] sharedKey) throws BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, KeyStoreException, UnrecoverableEntryException, CertificateException, IOException {
            SecretKeySpec key = new SecretKeySpec(sharedKey, "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);

            byte[] encryptedBytes = cipher.doFinal(input);
            return encryptedBytes;
        }

        public static byte[] decryptCBC(byte[] input, byte[] iv, byte[] sharedKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
            SecretKeySpec key = new SecretKeySpec(sharedKey, "AES");
            byte[] decryptedBytes = null;
            try {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);

                decryptedBytes = cipher.doFinal(input);

            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return decryptedBytes;
        }
    }
}
