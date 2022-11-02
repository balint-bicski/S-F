# CAFF parser implementation

To build the project, create a folder named `build`, then simply run `make` in the root directory of the project.

On Ubuntu and its flavors, every dependency can be installed together by installing the `build-essential` package.

## Usage

After building, call the resulting executable (`caff_parser`) with one or two arguments:

* The first argument will be used as the path of the input CAFF file that should be parsed.
* The second, optional argument will be used as the path of the generated preview file. If this argument
  isn't provided, no preview will be generated.

An example call (on Linux): `./caff_parser res/1.caff test.bmp`

The output is a complex JSON object with a mandatory success field. If this field contains `"yes"`, then the following
`data` field contains the full metadata of the parsed CAFF file.

If it contains `"no"`, then an error occurred. The nature of this error is further described in the `reason` field.
