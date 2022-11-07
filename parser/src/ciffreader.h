#ifndef CIFFREADER_H
#define CIFFREADER_H

#include <istream>

#include "caff.h"
#include "types.h"
#include "streamwrapper.h"

class CIFFReader {
    bool parse_called;
    bool parse_successful;
    CIFF data;

    bool read_caption_and_tags(Stream& stream, u64 expected_size);
    bool read_pixels(Stream& stream, u64 expected_size);

public:
    CIFFReader();

    bool parse(Stream& stream, u64 expected_size);

    CIFF get();
};

#endif
