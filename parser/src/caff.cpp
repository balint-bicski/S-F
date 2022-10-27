#include <iomanip>
#include <iostream>

#include "caff.h"

void CAFF::print_metadata(std::ostream& stream, u8 prefix) const {
    std::string padding(prefix, ' ');
    
    stream << padding << "{" << std::endl;
    stream << padding << "  ciff_count: " << this->ciff_count << "," << std::endl;
    {
        stream << padding << "  credits: ";
        this->credits.print_metadata(stream, prefix + 2);
        stream << "," << std::endl;
    }
    {
        stream << padding << "  frames: [" << std::endl;
        for (const Frame& frame : this->frames) {
            frame.print_metadata(stream, prefix + 4);

            if (&frame != &this->frames.back()) {
                stream << ",";
            }
            stream << std::endl;
        }
        stream << padding << "  ]" << std::endl;
    }
    stream << padding << "}" << std::endl;
}

void Credits::print_metadata(std::ostream& stream, u8 prefix) const {
    std::string padding(prefix, ' ');
    
    stream << "{" << std::endl;
    stream << padding << "  creator: \"" << this->creator << "\"," << std::endl;
    stream << padding << "  date: \"" << this->year << "-";
    stream << std::setw(2) << std::setfill('0') << this->month << "-";
    stream << std::setw(2) << this->day << " ";
    stream << std::setw(2) << this->hour << ":";
    stream << std::setw(2) << this->minute << "\"" << std::endl;
    stream << padding << "}";
}

void Frame::print_metadata(std::ostream& stream, u8 prefix) const {
    std::string padding(prefix, ' ');
    
    stream << padding << "{" << std::endl;
    {
        stream << padding << "  image: ";
        this->image.print_metadata(stream, prefix + 2);
        stream << "," << std::endl;
    }
    stream << padding << "  duration: " << this->duration << std::endl;
    stream << padding << "}";
}

void CIFF::print_metadata(std::ostream& stream, u8 prefix) const {
    std::string padding(prefix, ' ');
    
    stream << "{" << std::endl;
    stream << padding << "  width: " << this->width << "," << std::endl;
    stream << padding << "  height: " << this->height << "," << std::endl;
    stream << padding << "  caption: \"" << this->caption << "\"," << std::endl;
    stream << padding << "  tags: " /*TODO*/ << std::endl;
    stream << padding << "}";
}
