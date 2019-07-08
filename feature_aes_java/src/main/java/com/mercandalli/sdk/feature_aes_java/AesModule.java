package com.mercandalli.sdk.feature_aes_java;

public class AesModule {

    public AesManager createAesManager() {
        return new AesManagerImpl();
    }
}
