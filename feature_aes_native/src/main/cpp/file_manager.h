#ifndef AES_ANDROID_FILE_MANAGER_H
#define AES_ANDROID_FILE_MANAGER_H

#include <fstream>

class FileManager;

class FileManager {

public:

    FileManager();

    ~FileManager();

    std::ifstream* OpenToRead(const char *path);

    std::ofstream* OpenToWrite(const char *path);

    std::ofstream* OpenToAppend(const char *path);

    void Write(std::ofstream *stream, uint8_t *buffer, uint32_t length);

    void Close(std::ifstream *stream);

    void Close(std::ofstream *stream);
};

#endif //AES_ANDROID_FILE_MANAGER_H
