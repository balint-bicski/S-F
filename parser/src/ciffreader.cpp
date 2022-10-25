#include <string.h>

#include "ciffreader.h"

CIFFReader::CIFFReader() : parse_called(false), parse_successful(false), data() {}

CIFF CIFFReader::get() {
    if (parse_called && parse_successful) {
        return data;
    }
     
    throw "CIFFReader::get called without a successful parse first!";
}

bool CIFFReader::parse(std::istream& stream, u64 expected_size) {
    this->parse_called = true;

    /* Assert that the first four bytes contain the magic keyword. */
    char magic[4];
    stream.read(magic, 4);
    if (!strncmp(magic, "CIFF", 4) || !stream) {
        return false;
    }

    /* Read the header and the content size. */
    u64 header_size, content_size;
    stream.read((char*) &header_size, sizeof(u64));
    stream.read((char*) &content_size, sizeof(u64));

    /* Assert that the calculated size matches the expected size. */
    if (header_size + content_size != expected_size || !stream) {
        return false;
    }

    /* Read the width and height of the image. */
    u64 width, height;
    stream.read((char*) &width, sizeof(u64));
    stream.read((char*) height, sizeof(u64));

    /* Assert that the calculated payload size matches the expected content size. */
    if (width * height * 3 != content_size || !stream) {
        return false;
    }

    /* Save parsed data. */
    this->data.width = width;
    this->data.height = height;

    /* Read caption and tags. The two fields together have an exact known size. */
    bool success = read_caption_and_tags(stream, header_size - 4 - 4 * 8);
    if (!success || !stream) {
        return false;
    }

    /* Read pixels, and assert that they have the expected size. */
    success = read_pixels(stream, content_size);
    if (!success) {
        return false;
    }

    this->parse_successful = true;
    return true;
}

bool read_caption_and_tags(std::istream& stream, u64 expected_size) {
    // TODO
}
