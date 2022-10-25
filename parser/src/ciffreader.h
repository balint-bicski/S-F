#ifndef CIFFREADER_H
#define CIFFREADER_H

#include <istream>

#include "ciff.h"
#include "types.h"

class CIFFReader {
    bool parse_called;
    bool parse_successful;
    CIFF data;

    bool read_caption_and_tags(std::istream& stream, u64 expected_size);
    bool read_pixels(std::istream& stream, u64 expected_size);

public:
    CIFFReader();

    bool parse(std::istream& stream, u64 expected_size);

    CIFF get();
};

#endif
