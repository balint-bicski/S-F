#include <string.h>

#include "caffreader.h"
#include "ciffreader.h"

CAFFReader::CAFFReader() : parse_called(false), parse_successful(false), data() {}

CAFF CAFFReader::get() {
    if (parse_called && parse_successful) {
        return data;
    }
     
    throw "CAFFReader::get called without a successful parse first!";
}

bool CAFFReader::parse(std::istream& stream) {
    this->parse_called = true;

    u8 id;
    u64 block_length;
    bool success = false;
    bool first = true;

    /* All blocks will be read in a loop. At least one iteration is guaranteed. */
    do {
        /* Read block metadata and assert that further data is present. */
        stream.read((char*) &id, sizeof(u8));
        stream.read((char*) &block_length, sizeof(u64));

        /* The first block must be a header block. */
        if (!stream || (first && id != 1)) {
            return false;
        }
        first = false;

        /* Read block contents based on the given ID. */
        u64 expected_length = block_length - 8 - 1;
        switch (id) {
            case 1: success = this->read_header(stream, expected_length); break;
            case 2: success = this->read_credits(stream, expected_length); break;
            case 3: success = this->read_frame(stream, expected_length); break;
            default: return false;
        }
    } while (stream && success);

    /* Assert that we read exactly as many frames as the CAFF header said there would be. */
    if (this->data.frames.size() != this->data.ciff_count) {
        success = false;
    }

    this->parse_successful = success;
    return success;
}

bool CAFFReader::read_header(std::istream& stream, u64 expected_size) {
    /* Assert that the expected size equals to 4+8+8 bytes. */
    if (expected_size != 4 + 8 + 8) {
        return false;
    }

    /* Assert that the first four bytes contain the magic keyword. */
    char magic[4];
    stream.read(magic, 4);
    if (!strncmp(magic, "CAFF", 4) || !stream) {
        return false;
    }

    /* Assert that the next eight bytes contain the correct header size. */
    u64 header_size;
    stream.read((char*) &header_size, sizeof(u64));
    if (header_size != expected_size || !stream) {
        return false;
    }

    /* Read the next eight bytes containing the number of frames. */
    u64 num_anim;
    stream.read((char*) &num_anim, sizeof(u64));
    if (!stream) {
        return false;
    }

    /* Save parsed data. */
    this->data.ciff_count = num_anim;
    return true;
}

bool CAFFReader::read_credits(std::istream& stream, u64 expected_size) {
    Credits credits;

    /* Read all fields of the creation date. */
    stream.read((char*) &credits.year, sizeof(u16));
    stream.read((char*) &credits.month, sizeof(u8));
    stream.read((char*) &credits.day, sizeof(u8));
    stream.read((char*) &credits.hour, sizeof(u8));
    stream.read((char*) &credits.minute, sizeof(u8));
    if (!stream) {
        return false;
    }

    /* Read the expected creator len. */
    u64 creator_len;
    stream.read((char*) &creator_len, sizeof(u64));

    /* Assert that the expected size matches with the calculated size. */
    if (expected_size != (2 + 1 + 1 + 1 + 1) + 8 + creator_len || !stream) {
        return false;
    }

    // TODO Choose and enforce a sensible maximum buffer size!
    /* Read and check creator name. */
    char* creator_buffer = new char[creator_len];
    stream.read(creator_buffer, creator_len);
    if (!stream) {
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

bool CAFFReader::read_frame(std::istream& stream, u64 expected_size) {
    /* Assert that by reading this CIFF, the read CIFF count does not go over the expected amount. */
    if (this->data.frames.size() + 1 > this->data.ciff_count) {
        return false;
    }

    /* Read eight bytes containing the frame duration. */
    u64 duration;
    stream.read((char*) &duration, sizeof(u64));
    if (!stream) {
        return false;
    }

    /* Read the CIFF file that follows. */
    CIFFReader reader;
    bool success = reader.parse(stream, expected_size - 8);
    if (!success) {
        return false;
    }

    /* If everything succeeded, save the compiled frame. */
    Frame frame;
    frame.duration = duration;
    frame.image = reader.get();

    this->data.frames.push_back(frame);
    return true;
}
