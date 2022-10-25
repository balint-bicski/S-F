#include <algorithm>
#include <string.h>

#include "caffreader.h"
#include "ciffreader.h"
#include "debug.h"

CAFFReader::CAFFReader() : parse_called(false), parse_successful(false), data() {}

CAFF CAFFReader::get() {
    if (parse_called && parse_successful) {
        return data;
    }
     
    throw "CAFFReader::get called without a successful parse first!";
}

bool CAFFReader::parse(std::istream& in_stream) {
    this->parse_called = true;

    Stream stream(in_stream);

    bool success = false;
    bool first = true;

    /* All blocks will be read in a loop. At least one iteration is guaranteed. */
    do {
        /* Read block metadata and assert that further data is present. */
        u8 id = stream.read8();
        u64 block_length = stream.read64();

        /* The first block must be a header block. */
        if (!stream || (first && id != 1)) {
            debug("CAFFReader::parse: First block was not a header block or stream ended!\n");
            return false;
        }
        first = false;

        /* Read block contents based on the given ID. */
        switch (id) {
            case 1: success = this->read_header(stream, block_length); break;
            case 2: success = this->read_credits(stream, block_length); break;
            case 3: success = this->read_frame(stream, block_length); break;
            default: debug("CAFFReader::parse: Invalid block id detected!\n"); return false;
        }
    } while (stream && stream.peek() != EOF && success);

    /* Assert that we read exactly as many frames as the CAFF header said there would be. */
    if (success && this->data.frames.size() != this->data.ciff_count) {
        debug("CAFFReader::parse: All blocks read, but expected and actual frame count differs!\n");
        success = false;
    }

    this->parse_successful = success;
    return success;
}

bool CAFFReader::read_header(Stream& stream, u64 expected_size) {
    /* Assert that the expected size equals to 4+8+8 bytes. */
    if (expected_size != 4 + 8 + 8) {
        debug("CAFFReader::read_header: Header expected size differs from mandatory size!\n");
        debug("                         This may not be an error, switching to big endian parsing!\n");

        std::reverse((char*) &expected_size, (char*) &expected_size + 8);

        if (expected_size == 4 + 8 + 8) {
            stream.set_big_endian(true);
        } else {
            debug("CAFFReader::read_header: Header expected size differs from mandatory size even with little endian encoding!\n");
            return false;
        }
    }

    /* Assert that the first four bytes contain the magic keyword. */
    char magic[4];
    stream.read(magic, 4);
    if (strncmp(magic, "CAFF", 4) != 0 || !stream) {
        debug("CAFFReader::read_header: Magic four bytes not present, or stream ended!\n");
        return false;
    }

    /* Assert that the next eight bytes contain the correct header size. */
    u64 header_size = stream.read64();
    if (header_size != expected_size || !stream) {
        debug("CAFFReader::read_header: Expected and parsed header sizes differ, or stream ended!\n");
        return false;
    }

    /* Read the next eight bytes containing the number of frames. */
    u64 num_anim = stream.read64();
    if (!stream) {
        debug("CAFFReader::read_header: Stream ended after reading animation frame count!\n");
        return false;
    }

    /* Save parsed data. */
    this->data.ciff_count = num_anim;
    return true;
}

bool CAFFReader::read_credits(Stream& stream, u64 expected_size) {
    Credits credits;

    /* Read all fields of the creation date. */
    credits.year = stream.read16();
    credits.month = stream.read8();
    credits.day = stream.read8();
    credits.hour = stream.read8();
    credits.minute = stream.read8();
    if (!stream) {
        debug("CAFFReader::read_credits: Stream ended after reading creation date!\n");
        return false;
    }

    /* Read the expected creator len. */
    u64 creator_len = stream.read64();

    /* Assert that the expected size matches with the calculated size. */
    if (expected_size != (2 + 1 + 1 + 1 + 1) + 8 + creator_len || !stream) {
        debug("CAFFReader::read_credits: Expected size not equal to parsed size, or stream ended!\n");
        return false;
    }

    // TODO Choose and enforce a sensible maximum buffer size!
    /* Read and check creator name. */
    char* creator_buffer = new char[creator_len];
    stream.read(creator_buffer, creator_len);
    if (!stream) {
        debug("CAFFReader::read_credits: Stream ended after reading creator name!\n");
        delete[] creator_buffer;
        return false;
    }

    /* Copy the creator name into a string, free buffer. */
    credits.creator = std::string(creator_buffer, creator_len);
    delete[] creator_buffer;

    /* Save parsed data. */
    this->data.credits = credits;
    return true;
}

bool CAFFReader::read_frame(Stream& stream, u64 expected_size) {
    /* Assert that by reading this CIFF, the read CIFF count does not go over the expected amount. */
    if (this->data.frames.size() + 1 > this->data.ciff_count) {
        debug("CAFFReader::read_frame: By reading this frame, we go over the expected CIFF count!\n");
        return false;
    }

    /* Read eight bytes containing the frame duration. */
    u64 duration = stream.read64();
    if (!stream) {
        debug("CAFFReader::read_frame: Stream ended after reading frame duration!\n");
        return false;
    }

    /* Read the CIFF file that follows. */
    CIFFReader reader;
    bool success = reader.parse(stream, expected_size - 8);
    if (!success) {
        debug("CAFFReader::read_frame: CIFF parse was unsuccessful!\n");
        return false;
    }

    /* If everything succeeded, save the compiled frame. */
    Frame frame;
    frame.duration = duration;
    frame.image = reader.get();

    this->data.frames.push_back(frame);
    return true;
}
