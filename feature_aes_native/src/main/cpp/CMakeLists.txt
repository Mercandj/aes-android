cmake_minimum_required(VERSION 3.4.1)

add_library(
        aes-lib
        SHARED
        ${log-lib}
        ${android-lib}
        ./aes.c
        ./aes.h
        ./aes.hpp
        ./aes-lib.cpp
        test.c
        ./android_debug.h
        file_manager.cpp
        file_manager.h
)

find_library(
        android-lib
        android
)

find_library(
        log-lib
        log
)

target_link_libraries(
        aes-lib
        android
        ${log-lib}
)