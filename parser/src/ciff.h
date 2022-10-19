#ifndef CIFF_H
#define CIFF_H

#include <string>
#include <vector>

#include "types.h"

struct CIFF {
    u64 width;
    u64 height;

    std::string caption;
    std::vector<std::string> tags;

    u8* pixels;
};

#endif
