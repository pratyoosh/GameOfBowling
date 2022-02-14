package com.foo.bowling.utils;

/**
 * Game constants that serve as configuration for the rest of program
 */
public class Constants {
    // Maximum frames in the game
    public static final int MAX_FRAMES_PER_PLAYER = 10;

    // UI messages
    public static final String WELCOME_MESSAGE = "Welcome to the game! Enter your attempt score when prompted";
    public static final String SCORE_ENTRY_MESSAGE = "Enter your score during an attempt [0-10]:\n";
    public static final String INVALID_ENTRY_MESSAGE = "Entered input %s was not a valid number [0-10], " +
            "please re-enter score. Ctrl+c to terminate the program";
    public static final String GAME_OVER_MESSAGE = "Game Over! Final score was: %s";
}
