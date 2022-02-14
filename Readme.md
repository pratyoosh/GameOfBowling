## Overview

This project intends to simulate a [pin ball game scoring engine](https://www.kidslearntobowl.com/how-to-keep-score/).
Inputs are provided to the system as scores for an attempt using standard input, while the engine keeps 
a track of score across multiple frames in the game & overall score of the game.

On executing the program user is prompted to enter their scores incrementally as input, 
system prints intermediate scores after each entry & final score when the game is over.

This project is designed to contain minimal set of libraries & keep mostly to core java for ease of understanding.

### Additional libraries:
- Slf4J & Log4J for logging

### Testing
- JUnit Jupiter
- system-lambda
- Mockito

### Build
- Gradle using groovy DSL

## Prerequisites
Java Development Kit or Runtime Edition 8 or higher

## How to build & run this program
In the root directory
- Build (includes tests): `./gradlew build`
- Test only: `./gradlew test`

#### Build & Run

_These assume that the project has already been compiled_
- Run from command line (using file): `./bowling.sh <insert absolute file system path to test file>/file.txt"`
- Run from command line (using standard input): `./bowling.sh 

_To run with compilation & build_
- Run with Gradle (includes build & test): `./gradlew run 

Test reports after build are available at build/reports/tests/test/index.html

## Java Code coverage reports 
- HTML visual coverage reports can be generated in build/jacocoHtml folder by running
`./gradlew jacocoTestReport`

## Navigating the source
src/main/java

### Important Packages
- `com.foo.bowling.application`: Application entry point & glue code for dependency injection.
- `com.foo.bowling.engine`: Core logic for the game engine that run the game & mutates state.
- `com.foo.bowling.utils`: General utility classes for constants, common exceptions.

###Tests
src\test\java

#### Core E2E test case that simulates various scenarios:
GameTest.testGame


### Entry point of the program
Java entry point class is `com.foo.bowling.GameOfBowling` which delegates to
`com.foo.bowling.application.GameOfBowlingConsoleRunner` that then bootstraps the application.
