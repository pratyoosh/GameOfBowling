package com.foo.bowling.engine;

/**
 * State enumerations that describe the current state of a frame.
 */
public enum FrameStatus {
    /**
     * Initial state of a frame is NOT_FULLY_ATTEMPTED &amp; remains so until all attempts "within" the frame have been completed.
     */
    NOT_FULLY_ATTEMPTED,
    /**
     * State of frame when all the attempts "within" the frame have been completed, frame may still not be scored
     * pending results of attempts on future frames if frame is a strike or score.
     */
    ATTEMPTED,
    /**
     * Completely scored frame, this is final state of a frame.
     */
    SCORED
}
