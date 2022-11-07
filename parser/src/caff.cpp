#include <cstring>
#include <iomanip>
#include <iostream>

#include "caff.h"

CAFF::CAFF() : is_preview_created(false), preview_location(NULL) {}

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
        stream << padding << "  ]," << std::endl;
    }
    {
        stream << padding << "  preview_created: ";
        stream << (this->is_preview_created ? "\"yes\"" : "\"no\"");
        if (this->is_preview_created) {
            stream << "," << std::endl;
            stream << padding << "  preview_location: \"" << this->preview_location << "\"";
        }
        stream << std::endl;
    }
    stream << padding << "}" << std::endl;
}

void Credits::print_metadata(std::ostream& stream, u8 prefix) const {
    std::string padding(prefix, ' ');
    
    stream << "{" << std::endl;
    stream << padding << "  creator: \"" << this->creator << "\"," << std::endl;
    stream << padding << "  date: \"" << this->year << "-";
    stream << std::setw(2) << std::setfill('0') << (u64) this->month << "-";
    stream << std::setw(2) << (u64) this->day << " ";
    stream << std::setw(2) << (u64) this->hour << ":";
    stream << std::setw(2) << (u64) this->minute << "\"" << std::endl;
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
    {
        stream << padding << "  tags: [";
        for (const std::string& tag : this->tags) {
            stream << "\"" << tag << "\"";
            if (&tag != &this->tags.back()) {
                stream << ", ";
            }
        }
        stream << "]," << std::endl;
    }
    stream << padding << "  content_size: " << this->pixels_size << std::endl;
    stream << padding << "}";
}

CIFF::CIFF() {
    this->pixels = NULL;
    this->pixels_size = 0;
}

CIFF::CIFF(const CIFF& from) {
    this->width = from.width;
    this->height = from.height;
    this->caption = from.caption;
    this->tags = from.tags;

    this->pixels = NULL;
    this->pixels_size = 0;

    if (from.pixels != NULL) {
        this->pixels = new u8[from.pixels_size];
        memcpy((char*) this->pixels, (char*) from.pixels, from.pixels_size);
        this->pixels_size = from.pixels_size;
    }    
}

CIFF::CIFF(CIFF&& from) {
    this->width = from.width;
    this->height = from.height;
    this->caption = std::move(from.caption);
    this->tags = std::move(from.tags);

    this->pixels = from.pixels;
    this->pixels_size = from.pixels_size;
    from.pixels = NULL;
    from.pixels_size = 0;
}

CIFF& CIFF::operator=(const CIFF& from) {
    this->width = from.width;
    this->height = from.height;
    this->caption = from.caption;
    this->tags = from.tags;

    if (this->pixels != NULL) {
        delete[] this->pixels;
        this->pixels = NULL;
        this->pixels_size = 0;
    }

    if (from.pixels != NULL) {
        this->pixels = new u8[from.pixels_size];
        memcpy((char*) this->pixels, (char*) from.pixels, from.pixels_size);
        this->pixels_size = from.pixels_size;
    }

    return *this;
}

CIFF& CIFF::operator=(CIFF&& from) {
    this->width = from.width;
    this->height = from.height;
    this->caption = std::move(from.caption);
    this->tags = std::move(from.tags);

    if (this->pixels != NULL) {
        delete[] this->pixels;
    }
    this->pixels = from.pixels;
    this->pixels_size = from.pixels_size;
    from.pixels = NULL;
    from.pixels_size = 0;

    return *this;
}

CIFF::~CIFF() {
    if (this->pixels != NULL) {
        delete[] this->pixels;
    }
}
