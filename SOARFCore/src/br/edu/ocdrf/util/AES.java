package br.edu.ocdrf.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class AES {

    private String encryptionKey;
    private final String encryptionAlgorithm = "AES/GCM/NoPadding";
    private final String keySpec = "AES";

    public AES() {
    }

    public void setKeyFromHashedPassword(String humanPassword) {
        encryptionKey = passwordToSHA1(humanPassword);
    }

    public void setSecretKey(String secretKey) {
        encryptionKey = secretKey;
    }

    public String encrypt(String plainText) throws Exception {

        byte[] getFirst16bytes = Arrays.copyOf(encryptionKey.getBytes(), 16);

        SecretKeySpec keySpecification = new SecretKeySpec(getFirst16bytes, keySpec);
        Cipher cipher = Cipher.getInstance(encryptionAlgorithm);

        final int blockSize = cipher.getBlockSize();

        final byte[] ivData = new byte[blockSize];
        final SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");
        rnd.nextBytes(ivData);
        
        GCMParameterSpec params = new GCMParameterSpec(blockSize * Byte.SIZE, ivData);
        

        cipher.init(Cipher.ENCRYPT_MODE, keySpecification, params);

        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

        final byte[] ivAndEncryptedMessage = new byte[ivData.length + encryptedBytes.length];

        System.arraycopy(ivData, 0, ivAndEncryptedMessage, 0, blockSize);
        System.arraycopy(encryptedBytes, 0, ivAndEncryptedMessage, blockSize, encryptedBytes.length);

        String encryptedText = Base64.encodeBase64String(ivAndEncryptedMessage);

        return encryptedText;
        
    }

    public String decrypt(String encrypted) throws Exception {

        final byte[] ivAndEncryptedMessage = Base64.decodeBase64(encrypted);

        byte[] getFirst16bytes = Arrays.copyOf(encryptionKey.getBytes(), 16);
        SecretKeySpec keySpecification = new SecretKeySpec(getFirst16bytes, keySpec);

        Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
        final int blockSize = cipher.getBlockSize();

        final byte[] ivData = new byte[blockSize];
        System.arraycopy(ivAndEncryptedMessage, 0, ivData, 0, blockSize);
        
        GCMParameterSpec params = new GCMParameterSpec(blockSize * Byte.SIZE, ivData);
        

        final byte[] encryptedMessage = new byte[ivAndEncryptedMessage.length - blockSize];
        System.arraycopy(ivAndEncryptedMessage, blockSize, encryptedMessage, 0, encryptedMessage.length);

        cipher.init(Cipher.DECRYPT_MODE, keySpecification, params);

        byte[] plainBytes = cipher.doFinal(encryptedMessage);

        String results = new String(plainBytes);

        return results;
    }

    public static String generateSecretKey() throws NoSuchAlgorithmException {

        SecretKey key = null;
        String keyString = null;

        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        key = generator.generateKey();

        if (key != null) {
            keyString = Arrays.toString(key.getEncoded());
        }

        return keyString;
    }

    private String passwordToSHA1(String password) {

        String sha1Hash = null;

        if (null == password) {
            return null;
        }

        try {

            //Create MessageDigest object for MD5
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            //Update input string in message digest
            digest.update(password.getBytes(), 0, password.length());

            //Converts message digest value in base 16 (hex) 
            sha1Hash = new BigInteger(1, digest.digest()).toString(16);

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();
        }

        return sha1Hash;
    }
}
