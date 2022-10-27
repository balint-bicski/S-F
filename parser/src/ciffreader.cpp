#include <string.h>

#include "ciffreader.h"
#include "debug.h"

CIFFReader::CIFFReader() : parse_called(false), parse_successful(false), data() {}

CIFF CIFFReader::get() {
    if (parse_called && parse_successful) {
        return data;
    }
     
    throw "CIFFReader::get called without a successful parse first!";
}

bool CIFFReader::parse(Stream& stream, u64 expected_size) {
    this->parse_called = true;

    /* Assert that the first four bytes contain the magic keyword. */
    char magic[4];
    stream.read(magic, 4);
    if (strncmp(magic, "CIFF", 4) != 0 || !stream) {
        debug("CIFFReader::parse: Magic four bytes not present, or stream ended!\n");
        return false;
    }

    /* Read the header and the content size. */
    u64 header_size = stream.read64();
    u64 content_size = stream.read64();

    /* Assert that the calculated size matches the expected size. */
    if (header_size + content_size != expected_size || !stream) {
        debug("CIFFReader::parse: Expected size differs from asserted size, or stream ended!\n");
        return false;
    }

    /* Read the width and height of the image. */
    u64 width = stream.read64();
    u64 height = stream.read64();

    /* Assert that the calculated payload size matches the expected content size. */
    if (width * height * 3 != content_size || !stream) {
        debug("CIFFReader::parse: Calculated and asserted payload size differs, or stream ended!\n");
        return false;
    }

    /* Save parsed data. */
    this->data.width = width;
    this->data.height = height;

    /* Read caption and tags. The two fields together have an exact known size. */
    bool success = read_caption_and_tags(stream, header_size - 4 - 4 * 8);
    if (!success || !stream) {
        debug("CIFFReader::parse: Caption and tags reading unsuccessful, or stream ended!\n");
        return false;
    }

    /* Read pixels, and assert that they have the expected size. */
    success = read_pixels(stream, content_size);
    if (!success) {
        debug("CIFFReader::parse: Pixel reading unsuccessful, or stream ended!\n");
        return false;
    }

    this->parse_successful = true;
    return true;
}

bool CIFFReader::read_caption_and_tags(Stream& stream, u64 expected_size) {
    std::string caption;
    std::vector<std::string> tags;

    /* Read caption char by char until the first '\n'. */
    char c;
    while (true) {
        /* Assert that we can read the next character according to the expected size
         * (including terminating '\n'). */
        if (caption.length() + 1 >= expected_size) {
            debug("CIFFReader::read_caption_and_tags: Caption length over expected size!\n"); 
            return false;
        }

        /* Read and process the next character. */
        stream.read(&c, 1);
        if (!stream) {
            debug("CIFFReader::read_caption_and_tags: Stream ended before finishing caption!\n");
            return false;
        }

        if (c == '\n') {
            break;
        }

        caption.push_back(c);
    }

    /* Read tags as well, tag by tag. */
    u64 tag_bytes_read = 0;
    u64 caption_read = caption.length() + 1;
    while (stream && tag_bytes_read < expected_size - caption_read) {
        std::string current_tag;

        /* Read and process the next tag, char by char. */
        while (true) {
            /* Assert that we can read the next character according to the expected size. */
            if (tag_bytes_read >= expected_size - caption_read) {
                debug("CIFFReader::read_caption_and_tags: Tag byte count over expected size!\n");
                return false;
            }

            /* Read and process the next character. */
            stream.read(&c, 1);
            if (!stream || c == '\n') {
                debug("CIFFReader::read_caption_and_tags: Stream ended or illegal \\n found after reading tag char!\n");
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
    if (tag_bytes_read + caption_read != expected_size || !stream) {
        debug("CIFFReader::read_caption_and_tags: Caption plus tags size differs from expected size, or stream ended!\n");
        return false;
    }

    /* Save parsed data. */
    this->data.caption = caption;
    this->data.tags = tags;
    return true;
}

bool CIFFReader::read_pixels(Stream& stream, u64 expected_size) {
    /* Preallocate a large enough buffer. */
    u8* buffer = new u8[expected_size];

    /* Read content into buffer. */
    stream.read((char*) buffer, expected_size);
    if (!stream) {
        debug("CIFFReader::read_pixels: Stream ended after reading pixels into buffer!\n");
        delete[] buffer;
        return false;
    }

    /* Save parsed data. */
    this->data.pixels = buffer;
    this->data.pixels_size = expected_size;
    return true;
}
