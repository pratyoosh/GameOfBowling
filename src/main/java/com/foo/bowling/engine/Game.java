package com.foo.bowling.engine;

import com.foo.bowling.utils.Constants;
import com.foo.bowling.utils.exceptions.AlreadyScoredException;
import com.foo.bowling.utils.exceptions.MaximumFrameAttemptException;
import com.foo.bowling.application.dependencyInjection.DependencyInjector;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Game score board of a single player.
 */
public class Game {

    // All frames regardless of their scoring state.
    private Stack<Frame> allFrames;

    // A stack to contain references of only those frames with partial scores.
    private Stack<Frame> incompleteScoreFrames;

    // Overall score of the game.
    private int gameScore;

    /**
     * Gets the score of the game.
     * @return score of the game.
     */
    public int getGameScore() {
        return gameScore;
    }

    /**
     * Creates an instance of Game.
     */
    public Game() {
        gameScore = 0;
        allFrames = new Stack<>();
        incompleteScoreFrames = new Stack<>();
    }

    /**
     * Adds a scoring frame to the game
     * @return boolean set to true if the new frame score can be added else false.
     */
    public boolean addNewScoringFrame() throws MaximumFrameAttemptException {
        if (allFrames.size() < Constants.MAX_FRAMES_PER_PLAYER) {
            Frame frame = DependencyInjector.getNewFrame(allFrames.size() == Constants.MAX_FRAMES_PER_PLAYER - 1);
            allFrames.push(frame);
            incompleteScoreFrames.push(frame);
            return true;
        } else {
            throw new MaximumFrameAttemptException();
        }
    }

    /**
     * Add a player's single attempt's score to the game.
     * @param attemptScore the numeral score to be added to the game
     * @return a boolean indicating true when the game score is final, returns false otherwise
     * @throws MaximumFrameAttemptException Thrown when maximum frames for the player in the game have been reached.
     * @throws AlreadyScoredException This exception is thrown when a score is added to an already scored frame.
     */
    public boolean addAttemptScore(int attemptScore) throws MaximumFrameAttemptException, AlreadyScoredException {
        if (attemptScore < 0 || attemptScore > 10) {
            throw new IllegalArgumentException("Score can only be in the range of 0-10");
        }
        // If either this is the first play or last frame has been fully attempted or scored, try adding a new frame
        if (incompleteScoreFrames.isEmpty() || allFrames.peek().getStatus() == FrameStatus.ATTEMPTED ||
                allFrames.peek().getStatus() == FrameStatus.SCORED) {
            this.addNewScoringFrame();
        }
        // Keep track of all frames that are fully "scored" as a result of this score via this set
        Set<Frame> scoredFrames = new HashSet<>();
        for (Frame frame: incompleteScoreFrames) {
            FrameStatus status = frame.addScore(attemptScore);
            if (status == FrameStatus.SCORED) {
                gameScore += frame.getScore();
                scoredFrames.add(frame);
                if (frame.isLastFrame()) {
                    return true;
                }
            }
        }
        incompleteScoreFrames.removeAll(scoredFrames);
        return false;
    }

    @Override
    public String toString() {
        return String.format("[Game Score: %s] %s", gameScore, allFrames.toString());
    }

}
