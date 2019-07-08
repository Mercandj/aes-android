#include <stdint.h>
#include <jni.h>
#include <string>
#include <fstream>
#include "android_debug.h"
#include "aes.hpp"
#include "test.c"
#include "file_manager.h"

jbyteArray clientByteArrayFromUnit8(JNIEnv *env, uint8_t *array, int length) {
    jbyteArray output = env->NewByteArray(length);
    env->SetByteArrayRegion(output, 0, length, reinterpret_cast<jbyte *>(array));
    return output;
}

static uint8_t *encodeByteArray(
        uint8_t *message,
        uint32_t length,
        uint8_t *key,
        uint8_t *initialization_vector
) {
    struct AES_ctx ctx{};
    if (initialization_vector == nullptr) {
        AES_init_ctx(&ctx, key);
    } else {
        AES_init_ctx_iv(&ctx, key, initialization_vector);
    }
    uint32_t last_loop_size = length % AES_BLOCKLEN;
    uint32_t iterations = length / AES_BLOCKLEN;
    if (length % AES_BLOCKLEN != 0) {
        iterations++;
    }
    size_t buffer_size = AES_BLOCKLEN * sizeof(uint8_t);
    uint8_t buffer[buffer_size];
    auto *output = (uint8_t *) calloc(iterations * AES_BLOCKLEN, sizeof(uint8_t));
    for (uint32_t iterationIndex = 0; iterationIndex < iterations; iterationIndex++) {
        memset(buffer, 0, buffer_size);
        if (iterationIndex == iterations - 1 && last_loop_size > 0) {
            memcpy(
                    buffer,
                    message + iterationIndex * AES_BLOCKLEN * sizeof(uint8_t),
                    last_loop_size
            );
        } else {
            memcpy(
                    buffer,
                    message + iterationIndex * AES_BLOCKLEN * sizeof(uint8_t),
                    buffer_size
            );
        }
        AES_ECB_encrypt(&ctx, buffer);
        memcpy(
                output + iterationIndex * AES_BLOCKLEN * sizeof(uint8_t),
                buffer,
                buffer_size
        );
    }
    return output;
}

static uint8_t *decodeByteArray(
        uint8_t *message,
        uint32_t length,
        uint8_t *key,
        uint8_t *initialization_vector
) {
    struct AES_ctx ctx{};
    if (initialization_vector == nullptr) {
        AES_init_ctx(&ctx, key);
    } else {
        AES_init_ctx_iv(&ctx, key, initialization_vector);
    }
    uint32_t iterations = length / AES_BLOCKLEN;
    if (length % AES_BLOCKLEN != 0) {
        iterations++;
    }
    size_t buffer_size = AES_BLOCKLEN * sizeof(uint8_t);
    uint8_t buffer[buffer_size];
    auto *output = (uint8_t *) calloc(iterations * AES_BLOCKLEN, sizeof(uint8_t));
    for (uint32_t iterationIndex = 0; iterationIndex < iterations; iterationIndex++) {
        memset(&buffer, 0, buffer_size);
        memcpy(
                buffer,
                message + iterationIndex * AES_BLOCKLEN * sizeof(uint8_t),
                buffer_size
        );
        AES_ECB_decrypt(&ctx, buffer);
        memcpy(
                output + iterationIndex * AES_BLOCKLEN * sizeof(uint8_t),
                buffer,
                buffer_size
        );
    }
    return output;
}

extern "C" jbyteArray
Java_com_mercandalli_android_sdk_feature_1aes_internal_AesInternal_encodeByteArray(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray message_to_encode_,
        jbyteArray key_,
        jbyteArray initialization_vector_
) {
    auto *message_to_encode = (uint8_t *) env->GetByteArrayElements(message_to_encode_, 0);
    auto *key = (uint8_t *) env->GetByteArrayElements(key_, 0);
    uint8_t *initialization_vector = nullptr;
    if (initialization_vector_ != nullptr) {
        initialization_vector = (uint8_t *) env->GetByteArrayElements(initialization_vector_, 0);
    }
    auto message_to_encode_length = env->GetArrayLength(message_to_encode_);
    auto *output = encodeByteArray(
            message_to_encode,
            static_cast<uint32_t>(message_to_encode_length),
            key,
            initialization_vector
    );
    env->ReleaseByteArrayElements(message_to_encode_, (jbyte *) message_to_encode, 0);
    env->ReleaseByteArrayElements(key_, (jbyte *) key, 0);
    if (initialization_vector_ != nullptr) {
        env->ReleaseByteArrayElements(initialization_vector_, (jbyte *) initialization_vector, 0);
    }
    auto iterations = message_to_encode_length / AES_BLOCKLEN;
    if (message_to_encode_length % AES_BLOCKLEN != 0) {
        iterations++;
    }
    return clientByteArrayFromUnit8(env, output, iterations * AES_BLOCKLEN * sizeof(uint8_t));
}

extern "C" jbyteArray
Java_com_mercandalli_android_sdk_feature_1aes_internal_AesInternal_decodeByteArray(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray message_to_decode_,
        jbyteArray key_,
        jbyteArray initialization_vector_
) {
    auto *message_to_decode = (uint8_t *) env->GetByteArrayElements(message_to_decode_, 0);
    auto *key = (uint8_t *) env->GetByteArrayElements(key_, 0);
    uint8_t *initialization_vector = nullptr;
    if (initialization_vector_ != nullptr) {
        initialization_vector = (uint8_t *) env->GetByteArrayElements(initialization_vector_, 0);
    }
    auto message_to_encode_length = env->GetArrayLength(message_to_decode_);
    auto *output = decodeByteArray(
            message_to_decode,
            static_cast<uint32_t>(message_to_encode_length),
            key,
            initialization_vector
    );
    env->ReleaseByteArrayElements(message_to_decode_, (jbyte *) message_to_decode, 0);
    env->ReleaseByteArrayElements(key_, (jbyte *) key, 0);
    if (initialization_vector_ != nullptr) {
        env->ReleaseByteArrayElements(initialization_vector_, (jbyte *) initialization_vector, 0);
    }
    auto iterations = message_to_encode_length / AES_BLOCKLEN;
    if (message_to_encode_length % AES_BLOCKLEN != 0) {
        iterations++;
    }
    return clientByteArrayFromUnit8(env, output, iterations * AES_BLOCKLEN * sizeof(uint8_t));
}

extern "C" void
Java_com_mercandalli_android_sdk_feature_1aes_internal_AesInternal_debug(
        JNIEnv *env,
        jobject /* this */,
        jstring debug_
) {
    const char *path = env->GetStringUTFChars(
            debug_,
            0
    );
    uint8_t message[16] = {(uint8_t) 0x2b, (uint8_t) 0x7e, (uint8_t) 0x15, (uint8_t) 0x16,
                           (uint8_t) 0x28, (uint8_t) 0xae, (uint8_t) 0xd2, (uint8_t) 0xa6,
                           (uint8_t) 0xab, (uint8_t) 0xf7, (uint8_t) 0x15, (uint8_t) 0x88,
                           (uint8_t) 0x09, (uint8_t) 0xcf, (uint8_t) 0x4f, (uint8_t) 0x3c};

    FileManager fileManager = FileManager();
    std::ofstream *stream = fileManager.OpenToWrite(path);
    fileManager.Write(stream, message, 16);
    fileManager.Close(stream);
}

