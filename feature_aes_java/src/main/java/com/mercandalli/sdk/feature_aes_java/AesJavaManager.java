package com.mercandalli.sdk.feature_aes_java;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public interface AesJavaManager {

    byte[] generateKey(AesKeySize aesKeySize) throws NoSuchAlgorithmException;

    AesCrypter getAesCrypter(
            AesOpMode opMode,
            AesMode mode,
            AesPadding padding,
            byte[] key
    ) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException;
}
