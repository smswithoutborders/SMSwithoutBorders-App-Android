package com.example.sw0b_001.Security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashes {

    public static byte[] sha512(String input) throws NoSuchAlgorithmException {
        MessageDigest md = java.security.MessageDigest.getInstance("SHA-512");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString().getBytes();
    }

    public byte[] sha256(String input) throws NoSuchAlgorithmException {
        MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            sb.append(Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString().getBytes();
    }
}
