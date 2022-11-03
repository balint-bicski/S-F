#ifndef READERLIMITS_H
#define READERLIMITS_H

#include "types.h"

namespace Limits {
    const u64 creator_len = 16 * 1000;
    const u64 caption_and_tags_size = 16 * 1000;
    const u64 pixels_size = 64 * 1000 * 1000;
}

#endif
