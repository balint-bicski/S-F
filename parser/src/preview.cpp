#define STB_IMAGE_WRITE_IMPLEMENTATION
#include "stb_image_write.h"

#include "caff.h"

bool CAFF::create_preview(u64 frame_number, const char* file) {
    if (frame_number >= this->ciff_count) {
        return false;
    }
    
    const CIFF& target = this->frames.at(frame_number).image;

    int success = stbi_write_bmp(file, target.width, target.height, 3, target.pixels);

    if (success != 0) {
        this->is_preview_created = true;
        this->preview_location = file;

        return true;
    }
    
    return false;
}
