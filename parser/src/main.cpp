#include <iostream>
#include <fstream>

#include "caffreader.h"

int main() {
    CAFFReader reader;
    const char* name = "res/1.caff";

    std::ifstream file(name);
    if (file.is_open()) {
        bool success = reader.parse(file);
        if (success) {
            CAFF caff = reader.get();
            std::cout << "Parsing successful, metadata output: " << std::endl;
            caff.print_metadata(std::cout);
        } else {
            std::cout << "Parsing failed for file: " << name << std::endl;
        }

        file.close();
    } else {
        std::cout << "Could not open file: " << name << std::endl;
    }

    return 0;
}
