# Guru Cue Search &amp; Recommendations Data Provider for JDBC

This is the library providing the database interface extension for the Guru Cue
Search &amp; Recommendations, for data providers using the JDBC interface.

# Building the Library
The minimum required JDK version to build the library with is 1.8.

Before you start building the library put into the `libs` directory the `database`
Guru Cue Search &amp; Recommendations library.

Perform the build using [gradle](https://gradle.org/). If you don't have it
installed, you can use the gradle wrapper script `gradlew` (Linux and similar)
or `gradlew.bat` (Windows).

The build process will result in a `jar` file in the `build/libs` directory.
Copy it into the `libs` directory of dependent projects, such as
`data-provider-postgresql`.
