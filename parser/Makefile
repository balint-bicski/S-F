# The name of the final executable.
BINARY = caff_parser

# Build directory, intermediate files will be placed here.
BUILD_DIR = build
# Source code directory, containing headers and source files.
SRC_DIR = src

# Specify objects and headers using path substitution.
OBJECTS = $(patsubst $(SRC_DIR)/%.cpp, $(BUILD_DIR)/%.o, $(wildcard $(SRC_DIR)/*.cpp) )
HEADERS = $(wildcard $(SRC_DIR)/*.h)

# Compiler and linker settings.
CXX = g++
CXXFLAGS = -std=c++14 -O0 -Wall -Wdeprecated -pedantic -g
LDFLAGS = -g

# Best practice to avoid issues with similarly named files.
.PHONY: default all clean debug

# Tasks that can be called.
default: $(BINARY)

all:
	$(MAKE) $(BINARY)

clean:
	rm -f $(BINARY) $(OBJECTS)

debug: CXXFLAGS += -DDEBUG
debug: $(BINARY)

# Compilation and linking settings.
$(BINARY): $(OBJECTS)
	$(CXX) $(LDFLAGS) $^ -o $@

$(BUILD_DIR)/%.o: $(SRC_DIR)/%.cpp $(HEADERS)
	$(CXX) $(CXXFLAGS) -c $< -o $@
