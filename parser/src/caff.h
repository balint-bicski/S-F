#ifndef CAFF_H
#define CAFF_H

#include <string>
#include <vector>

#include "types.h"

struct CIFF {
    u64 width;
    u64 height;

    std::string caption;
    std::vector<std::string> tags;

    u8* pixels; // TODO This WILL create a memory leak! Must create a correct destructor!

    void print_metadata(std::ostream& stream, u8 prefix = 0) const;
};

struct Frame {
    CIFF image;
    u64 duration;

    void print_metadata(std::ostream& stream, u8 prefix = 0) const;
};

struct Credits {
    u16 year;
    u8 month;
    u8 day;
    u8 hour;
    u8 minute;

    std::string creator;

    void print_metadata(std::ostream& stream, u8 prefix = 0) const;
};

struct CAFF {
    u64 ciff_count;
    Credits credits;

    std::vector<Frame> frames;

    void print_metadata(std::ostream& stream, u8 prefix = 0) const;
};

#endif
