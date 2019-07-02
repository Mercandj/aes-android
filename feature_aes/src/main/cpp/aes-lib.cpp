#include <stdint.h>
#include <jni.h>
#include <string>
#include "android_debug.h"
#include "aes.hpp"
#include "test.c"

jbyteArray clientByteArrayFromUnit8(JNIEnv *env, uint8_t *array, int length) {
    jbyteArray output = env->NewByteArray(length);
    env->SetByteArrayRegion(output, 0, length, reinterpret_cast<jbyte *>(array));
    return output;
}

static uint8_t *encodeByteArray(
        uint8_t *message_to_encode,
        uint8_t *key
) {
    struct AES_ctx ctx{};
    AES_init_ctx(&ctx, key);
    AES_ECB_encrypt(&ctx, message_to_encode);
    return message_to_encode;
}

static uint8_t *decodeByteArray(
        uint8_t *message_to_decode,
        uint8_t *key
) {
    struct AES_ctx ctx{};
    AES_init_ctx(&ctx, key);
    AES_ECB_decrypt(&ctx, message_to_decode);
    return message_to_decode;
}

extern "C" jbyteArray
Java_com_mercandalli_android_sdk_feature_1aes_internal_AesInternal_encodeByteArray(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray message_to_encode_,
        jbyteArray key_
) {
    auto *message_to_encode = (uint8_t *) env->GetByteArrayElements(message_to_encode_, 0);
    auto *key = (uint8_t *) env->GetByteArrayElements(key_, 0);
    auto message_to_encode_length = env->GetArrayLength(message_to_encode_);
    uint8_t *output = encodeByteArray(message_to_encode, key);
    auto result = clientByteArrayFromUnit8(env, output, message_to_encode_length);
    env->ReleaseByteArrayElements(message_to_encode_, (jbyte *) message_to_encode, 0);
    env->ReleaseByteArrayElements(key_, (jbyte *) key, 0);
    return result;
}

extern "C" jbyteArray
Java_com_mercandalli_android_sdk_feature_1aes_internal_AesInternal_decodeByteArray(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray message_to_decode_,
        jbyteArray key_
) {
    auto *message_to_decode = (uint8_t *) env->GetByteArrayElements(message_to_decode_, 0);
    auto *key = (uint8_t *) env->GetByteArrayElements(key_, 0);
    auto message_to_encode_length = env->GetArrayLength(message_to_decode_);
    uint8_t *output = decodeByteArray(message_to_decode, key);
    env->ReleaseByteArrayElements(message_to_decode_, (jbyte *) message_to_decode, 0);
    env->ReleaseByteArrayElements(key_, (jbyte *) key, 0);
    return clientByteArrayFromUnit8(env, output, message_to_encode_length);
}

