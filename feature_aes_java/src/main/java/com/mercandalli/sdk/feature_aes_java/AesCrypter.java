package com.mercandalli.sdk.feature_aes_java;

import java.io.OutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;

public class AesCrypter {

    private final Cipher cipher;

    AesCrypter(Cipher cipher) {
        this.cipher = cipher;
    }

    public CipherOutputStream createCipherOutputStream(OutputStream outputStream) {
        return new CipherOutputStream(outputStream, cipher);
    }
}
