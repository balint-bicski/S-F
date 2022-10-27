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

    u8* pixels;
    u64 pixels_size;

    void print_metadata(std::ostream& stream, u8 prefix = 0) const;

    CIFF();
    CIFF(const CIFF& from);
    CIFF(CIFF&& from);

    CIFF& operator=(const CIFF& from);
    CIFF& operator=(CIFF&& from);

    ~CIFF();
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
