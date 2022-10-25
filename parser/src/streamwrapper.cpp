#include <algorithm>

#include "streamwrapper.h"

Stream::Stream(std::istream& stream) : stream(stream), is_big_endian(false) {}

void Stream::set_big_endian(bool value) {
    this->is_big_endian = value;
}

std::istream& Stream::get_stream() {
    return this->stream;
}

u8 Stream::read8() {
    u8 target;
    this->stream.read((char*) &target, 1);
    
    return target;
}

u16 Stream::read16() {
    u16 target;
    this->stream.read((char*) &target, 2);

    if (this->is_big_endian) {
        std::reverse((char*) &target, (char*) &target + 2);
    }

    return target;
}

u64 Stream::read64() {
    u64 target;
    this->stream.read((char*) &target, 8);
    
    if (this->is_big_endian) {
        std::reverse((char*) &target, (char*) &target + 8);
    }

    return target;
}

void Stream::read(char* target, size_t bytes) {
    this->stream.read(target, bytes);
}


bool Stream::operator!() const {
    return !stream;
}

Stream::operator bool() const {
    return (bool) stream;
}

int Stream::peek() {
    return stream.peek();
}
