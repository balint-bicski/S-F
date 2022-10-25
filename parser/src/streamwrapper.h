#ifndef STREAMWRAPPER_H
#define STREAMWRAPPER_H

#include <iostream>

#include "types.h"

class Stream {
    std::istream& stream;
    bool is_big_endian;

public:
    Stream(std::istream& stream);

    void set_big_endian(bool value);
    std::istream& get_stream();

    u8 read8();
    u16 read16();
    u64 read64();

    void read(char* target, size_t bytes);

    operator bool() const;
    bool operator!() const;
    int peek();
};

#endif
