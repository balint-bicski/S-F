#include <iostream>
#include <fstream>

#include "caffreader.h"

int main() {
    CAFFReader reader;
    const char* name = "res/3.caff";

    std::ifstream file(name);
    if (file.is_open()) {
        bool success = reader.parse(file);
        if (success) {
            CAFF caff = reader.get();
            std::cout << "Parsing successful, some test metadata: " << std::endl;
            std::cout << "Creator: " << caff.credits.creator << std::endl;
            std::cout << "CIFF count: " << caff.ciff_count << std::endl;
            std::cout << "Size of first frame: " << caff.frames.at(0).image.width << "x" << caff.frames.at(0).image.height << std::endl;
        } else {
            std::cout << "Parsing failed for file: " << name << std::endl;
        }

        file.close();
    } else {
        std::cout << "Could not open file: " << name << std::endl;
    }

    return 0;
}
