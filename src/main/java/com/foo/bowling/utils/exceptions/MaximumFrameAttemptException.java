package com.foo.bowling.utils.exceptions;

/**
 * Thrown when maximum frames for the player in the game have been reached.
 */
public class MaximumFrameAttemptException extends Exception {
    public MaximumFrameAttemptException() {
        super("Maximum frames for the player in the game have been reached, please reset the game.");
    }
}
