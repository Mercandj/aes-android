#include "file_manager.h"

FileManager::FileManager() = default;

FileManager::~FileManager() = default;

std::ifstream *FileManager::OpenToRead(const char *path) {
    std::ifstream stream(path, std::ios::binary);
    return &stream;
}

std::ofstream *FileManager::OpenToWrite(const char *path) {
    std::ofstream stream(path, std::ios::binary);
    return &stream;
}

void FileManager::WriteToEnd(std::ofstream *stream, uint8_t *buffer, uint32_t length) {
    for (int i = 0; i < length; i++) {
        *stream << (int) buffer[i];
    }
}

void FileManager::Close(std::ifstream *stream) {
    stream->close();
}

void FileManager::Close(std::ofstream *stream) {
    stream->close();
}
