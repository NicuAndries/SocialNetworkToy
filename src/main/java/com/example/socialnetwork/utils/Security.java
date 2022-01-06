package com.example.socialnetwork.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Security {
    private String key;
    private Cipher cipher;
    java.security.Key aesKey;

    public Security() {
        try {
            key = "NicolaeAndries12";
            cipher = Cipher.getInstance("AES");
            aesKey =  new SecretKeySpec(key.getBytes(),"AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String encryptPassword(String inputPassword) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(inputPassword.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public boolean checkPassword(String inputPassword, String encryptedStoredPassword) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        String decrypted = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedStoredPassword.getBytes())));
        return inputPassword.equals(decrypted);
    }
}
