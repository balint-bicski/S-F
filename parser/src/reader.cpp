#include <string.h>

#include "reader.h"

CAFFReader::CAFFReader() : parse_called(false), parse_successful(false), data() {}

CAFF CAFFReader::get() {
    if (parse_called && parse_successful) {
        return data;
    }
     
    throw "CAFFReader::get called without a successful parse first!";
}

bool CAFFReader::parse(std::istream& stream) {

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
}

bool CAFFReader::read_credits(std::istream& stream, u64 expected_size) {

}

bool CAFFReader::read_ciff(std::istream& stream, u64 expected_size) {
    
}
