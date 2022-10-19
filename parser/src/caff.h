#ifndef CAFF_H
#define CAFF_H

#include <string>
#include <vector>

#include "ciff.h"
#include "types.h"

struct Frame {
    CIFF image;
    u64 duration;
};

struct Credits {
    u16 year;
    u8 month;
    u8 day;
    u8 hour;
    u8 minute;

    std::string creator;
};

struct CAFF {
    u64 ciff_count;
    Credits credits;

    std::vector<Frame> frames;
};

#endif
