package com.mercandalli.sdk.feature_aes_java;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class AesManagerTest {

    private static final byte[] KEY_128 = new byte[]{
            (byte) 0x2b,
            (byte) 0x7e,
            (byte) 0x15,
            (byte) 0x16,
            (byte) 0x28,
            (byte) 0xae,
            (byte) 0xd2,
            (byte) 0xa6,
            (byte) 0xab,
            (byte) 0xf7,
            (byte) 0x15,
            (byte) 0x88,
            (byte) 0x09,
            (byte) 0xcf,
            (byte) 0x4f,
            (byte) 0x3c
    };

    @Test
    public void generateKeyWithGoodSize() throws NoSuchAlgorithmException {
        // Given
        AesManager aesManager = new AesModule().createAesManager();

        // When
        byte[] bytes = aesManager.generateKey(AesKeySize.KEY_128);

        // Then
        Assert.assertEquals(
                16,
                bytes.length
        );
    }

    @Test
    public void encodeWithOutputStream() throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Given
        AesManager aesManager = new AesModule().createAesManager();
        File clearFile = new File(getFileRoot().getParent(), "clear_reference.png");
        AesCrypter aesCrypter = aesManager.getAesCrypter(
                AesOpMode.CRYPT,
                AesMode.ECB,
                AesPadding.NO,
                KEY_128
        );
        File outputCryptFile = new File(getFileRoot().getParent(), "output_crypt_File.aes");
        if (outputCryptFile.exists()) {
            outputCryptFile.delete();
        }

        // When
        InputStream input = new FileInputStream(clearFile);
        byte[] buffer = new byte[4096];
        int n;
        OutputStream output = aesCrypter.createCipherOutputStream(
                new FileOutputStream(outputCryptFile)
        );
        while ((n = input.read(buffer)) != -1) {
            if (n > 0) {
                output.write(buffer, 0, n);
            }
        }
        output.close();

        // Then
        areEquals(
                new File(getFileRoot().getParent(), "unclear_ecb_reference.aes"),
                outputCryptFile
        );
    }

    private void areEquals(
            File outputReferenceFile,
            File outputFile
    ) throws IOException {
        byte[] outputReferenceBytes = read(outputReferenceFile);
        byte[] outputBytes = read(outputFile);
        if (outputReferenceBytes.length != outputBytes.length) {
            System.out.println("Size diff: " + outputReferenceBytes.length + " != " + outputBytes.length);
        }
        for (int i = 0, size = Math.min(outputReferenceBytes.length, outputBytes.length); i < size; i++) {
            if (outputReferenceBytes[i] != outputBytes[i]) {
                Assert.fail(
                        "outputFile: ${outputFile.absolutePath}\n" +
                                "Error at " + i
                );
            }
        }
    }

    private File getFileRoot() {
        URL resource = getClass().getClassLoader().getResource("root.txt");
        String resourcePath = resource.getPath();
        return new File(resourcePath);
    }

    private static byte[] read(File file) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) ous.close();
            } catch (IOException ignored) {
            }
            try {
                if (ios != null) ios.close();
            } catch (IOException ignored) {
            }
        }
        return ous.toByteArray();
    }
}
