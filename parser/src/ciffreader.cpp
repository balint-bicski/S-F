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

bool CIFFReader::read_caption_and_tags(std::istream& stream, u64 expected_size) {
    std::string caption;
    std::vector<std::string> tags;

    /* Read caption char by char until the first '\n'. */
    char c;
    while (true) {
        /* Assert that we can read the next character according to the expected size. */
        if (caption.length() >= expected_size) {
            return false;
        }

        /* Read and process the next character. */
        stream.read(&c, 1);
        if (!stream) {
            return false;
        }

        if (c == '\n') {
            break;
        }

        caption.push_back(c);
    }

    /* Read tags as well, tag by tag. */
    u64 tag_bytes_read = 0;
    while (stream && tag_bytes_read < expected_size - caption.length()) {
        std::string current_tag;

        /* Read and process the next tag, char by char. */
        while (true) {
            /* Assert that we can read the next character according to the expected size. */
            if (tag_bytes_read >= expected_size - caption.length()) {
                return false;
            }

            /* Read and process the next character. */
            stream.read(&c, 1);
            if (!stream || c == '\n') {
                return false;
            }
            tag_bytes_read += 1;

            /* On a delimiter, we save the current tag if it contains anything. */
            if (c == '\0') {
                if (current_tag.length() > 0) {
                    tags.push_back(current_tag);
                }
                break;
            }

            current_tag.push_back(c);
        }
    }

    /* We exit the outer loop when either the stream contains an error or we read enough bytes. */

    /* Assert that we read exactly the correct amount of bytes in total. */
    if (tag_bytes_read + caption.length() != expected_size || !stream) {
        return false;
    }

    /* Save parsed data. */
    this->data.caption = caption;
    this->data.tags = tags;
    return true;
}

bool CIFFReader::read_pixels(std::istream& stream, u64 expected_size) {
    /* Preallocate a large enough buffer. */
    u8 buffer[expected_size];

    /* Read content into buffer. */
    stream.read((char*) buffer, expected_size);
    if (!stream) {
        return false;
    }

    /* Save parsed data. */
    this->data.pixels = buffer;
    return true;
}
