package com.mercandalli.sdk.feature_aes_java;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

class AesManagerImpl implements AesManager {

    @Override
    public byte[] generateKey(final AesKeySize aesKeySize) throws NoSuchAlgorithmException {
        int size;
        if (aesKeySize == AesKeySize.KEY_128) {
            size = 128;
        } else {
            throw new IllegalStateException("Not supported: " + aesKeySize);
        }
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(size);
        SecretKey secretKey = keyGen.generateKey();
        return secretKey.getEncoded();
    }

    @Override
    public AesCrypter getAesCrypter(
            AesOpMode opMode,
            AesMode mode,
            AesPadding padding,
            byte[] key
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = configureCipher(
                opMode,
                mode,
                padding,
                key
        );
        return new AesCrypter(
                cipher
        );
    }

    private Cipher configureCipher(
            AesOpMode opMode,
            AesMode mode,
            AesPadding padding,
            byte[] key
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        StringBuilder transformationBuilder = new StringBuilder("AES/");
        if (mode == AesMode.ECB) {
            transformationBuilder.append("ECB/");
        } else {
            throw new IllegalStateException("Not supported: " + mode);
        }
        if (padding == AesPadding.NO) {
            transformationBuilder.append("NoPadding");
        } else if (padding == AesPadding.PKCS5) {
            transformationBuilder.append("PKCS5Padding");
        } else {
            throw new IllegalStateException("Not supported: " + transformationBuilder);
        }
        Cipher cipher = Cipher.getInstance(transformationBuilder.toString());
        int cipherMode;
        if (opMode == AesOpMode.CRYPT) {
            cipherMode = Cipher.ENCRYPT_MODE;
        } else if (opMode == AesOpMode.DECRYPT) {
            cipherMode = Cipher.DECRYPT_MODE;
        } else {
            throw new IllegalStateException("Not supported: " + opMode);
        }
        //noinspection ConstantConditions
        if (mode == AesMode.ECB) {
            cipher.init(cipherMode, secretKeySpec);
        } else {
            throw new IllegalStateException("Not supported: " + mode);
        }
        return cipher;
    }
}
