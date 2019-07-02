#include <stdint.h>
#include <jni.h>
#include <string.h>
#include <string>
#include "android_debug.h"
#include "aes.hpp"
#include "text.c"

typedef const std::string string;

jstring clientStringFromStdString(JNIEnv *env, const std::string &str) {
    // return env->NewStringUTF(str.c_str());
    jbyteArray array = env->NewByteArray(static_cast<jsize>(str.size()));
    env->SetByteArrayRegion(array, 0, static_cast<jsize>(str.size()), (const jbyte *) str.c_str());
    jstring strEncode = env->NewStringUTF("UTF-8");
    jclass cls = env->FindClass("java/lang/String");
    jmethodID ctor = env->GetMethodID(cls, "<init>", "([BLjava/lang/String;)V");
    jstring object = (jstring) env->NewObject(cls, ctor, array, strEncode);
    return object;
}

jbyteArray clientByteArrayFromUnit8(JNIEnv *env, uint8_t **array) {
    size_t length = sizeof(array);
    jbyteArray output = env->NewByteArray(length);
    env->SetByteArrayRegion(output, 0, length, reinterpret_cast<jbyte*>(&array));
    return output;
}

static std::string encodeString(
        std::string message_to_encode,
        std::string key
) {
    struct AES_ctx ctx{};
    uint8_t message_to_encode_uint8_t[message_to_encode.length()];
    std::copy(message_to_encode.begin(), message_to_encode.end(), message_to_encode_uint8_t);
    uint8_t key_uint8_t[key.length()];
    std::copy(key.begin(), key.end(), key_uint8_t);
    AES_init_ctx(&ctx, key_uint8_t);
    AES_ECB_encrypt(&ctx, message_to_encode_uint8_t);
    std::string output(
            message_to_encode_uint8_t,
            message_to_encode_uint8_t + message_to_encode.length()
    );
    return output;
}

static std::string decodeString(
        std::string message_to_decode,
        std::string key
) {
    struct AES_ctx ctx{};
    uint8_t message_to_decode_uint8_t[message_to_decode.length()];
    std::copy(message_to_decode.begin(), message_to_decode.end(), message_to_decode_uint8_t);
    uint8_t key_uint8_t[key.length()];
    std::copy(key.begin(), key.end(), key_uint8_t);
    AES_init_ctx(&ctx, key_uint8_t);
    AES_ECB_decrypt(&ctx, message_to_decode_uint8_t);
    std::string output(
            message_to_decode_uint8_t,
            message_to_decode_uint8_t + message_to_decode.length()
    );
    return output;
}

static uint8_t *encodeByteArray(
        uint8_t *message_to_encode,
        uint8_t *key
) {
    struct AES_ctx ctx{};
    AES_init_ctx(&ctx, message_to_encode);
    AES_ECB_encrypt(&ctx, key);
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

extern "C" jstring Java_com_mercandalli_android_sdk_feature_1aes_internal_AesInternal_encodeString(
        JNIEnv *env,
        jobject /* this */,
        jstring message_to_encode_,
        jstring key_
) {
    const char *message_to_encode = env->GetStringUTFChars(message_to_encode_, 0);
    const char *key = env->GetStringUTFChars(key_, 0);
    string output = encodeString(message_to_encode, key);
    env->ReleaseStringUTFChars(message_to_encode_, message_to_encode);
    env->ReleaseStringUTFChars(key_, key);
    return clientStringFromStdString(env, output.c_str());
}

extern "C" jstring Java_com_mercandalli_android_sdk_feature_1aes_internal_AesInternal_decodeString(
        JNIEnv *env,
        jobject /* this */,
        jstring message_to_decode_,
        jstring key_
) {
    const char *message_to_decode = env->GetStringUTFChars(message_to_decode_, 0);
    const char *key = env->GetStringUTFChars(key_, 0);
    string output = decodeString(message_to_decode, key);
    env->ReleaseStringUTFChars(message_to_decode_, message_to_decode);
    env->ReleaseStringUTFChars(key_, key);
    return clientStringFromStdString(env, output.c_str());
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
    uint8_t *output = encodeByteArray(message_to_encode, key);
    env->ReleaseByteArrayElements(message_to_encode_, (jbyte *) message_to_encode, 0);
    env->ReleaseByteArrayElements(key_, (jbyte *) key, 0);
    return clientByteArrayFromUnit8(env, &output);
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
    uint8_t *output = decodeByteArray(message_to_decode, key);
    env->ReleaseByteArrayElements(message_to_decode_, (jbyte *) message_to_decode, 0);
    env->ReleaseByteArrayElements(key_, (jbyte *) key, 0);
    return clientByteArrayFromUnit8(env, &output);
}

