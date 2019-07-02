cmake_minimum_required(VERSION 3.4.1)

# set (CMAKE_C_FLAGS "-std=gnu99 ${CMAKE_C_FLAGS}")
# set (CMAKE_C_STANDARD 99)
set(CMAKE_C_FLAGS "-std=c99")

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