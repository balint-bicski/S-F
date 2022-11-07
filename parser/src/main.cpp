#include <iostream>
#include <fstream>

#include "caffreader.h"

void print_error(const char* reason) {
    std::cout << "{" << std::endl;
    std::cout << "  success: \"no\"," << std::endl;
    std::cout << "  reason: \"" << reason << "\"" << std::endl;
    std::cout << "}" << std::endl;
}

int main(int argc, char** argv) {
    if (argc < 2) {
        print_error("No input file argument was provided!");
        return 1;
    }
    if (argc > 3) {
        print_error("Too many arguments were provided!");
        return 1;
    }

    std::ifstream file(argv[1]);
    if (file.is_open()) {
        CAFFReader reader;
        bool success = reader.parse(file);
        if (success) {
            CAFF caff = reader.get();

            if (argc == 3) {
                caff.create_preview(0, argv[2]);
            }

            std::cout << "{" << std::endl;
            std::cout << "  success: \"yes\"," << std::endl;
            std::cout << "  data:";
            caff.print_metadata(std::cout, 2);
            std::cout << "}" << std::endl;
        } else {
            print_error("CAFF parse failed!");
        }

        file.close();
    } else {
        print_error("CAFF file does not exist!");
    }

    return 0;
}
