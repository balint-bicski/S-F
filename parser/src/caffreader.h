#ifndef CAFFREADER_H
#define CAFFREADER_H

#include <istream>

#include "caff.h"
#include "types.h"

class CAFFReader {
    bool parse_called;
    bool parse_successful;
    CAFF data;

    bool read_header(std::istream& stream, u64 expected_size);
    bool read_credits(std::istream& stream, u64 expected_size);
    bool read_frame(std::istream& stream, u64 expected_size);

public:
    CAFFReader();

    bool parse(std::istream& stream);

    CAFF get();
};

#endif
