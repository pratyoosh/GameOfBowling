package com.foo.bowling;

import com.foo.bowling.application.dependencyInjection.DependencyInjector;
import com.foo.bowling.application.GameOfBowlingConsoleRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point class of the program instantiated
 */
public class GameOfBowling {
    private static final Logger log = LoggerFactory.getLogger(GameOfBowling.class);
    /**
     * Entry point of the game.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        log.info("Starting game scoring engine for pin ball");
        GameOfBowlingConsoleRunner runner = DependencyInjector.getRunner();
        System.exit(runner.run());
    }
}
