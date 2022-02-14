package com.foo.bowling.utils.exceptions;

/**
 * This exception is thrown when a score is added to an already scored frame.
 */
public class AlreadyScoredException extends Exception {
    public AlreadyScoredException(String reason) {
        super(reason);
    }
}
