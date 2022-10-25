#ifndef CAFFREADER_H
#define CAFFREADER_H

#include <istream>

#include "caff.h"
#include "types.h"
#include "streamwrapper.h"

class CAFFReader {
    bool parse_called;
    bool parse_successful;
    CAFF data;

    bool read_header(Stream& stream, u64 expected_size);
    bool read_credits(Stream& stream, u64 expected_size);
    bool read_frame(Stream& stream, u64 expected_size);

public:
    CAFFReader();

    bool parse(std::istream& in_stream);

    CAFF get();
};

#endif
