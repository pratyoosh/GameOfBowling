package com.foo.bowling.engine;

import com.foo.bowling.utils.exceptions.AlreadyScoredException;

/**
 * Represents the score of an Frame within the game, the overall game can be visualized as stack of score in each frame.
 */
// TODO: Move the model out of this class into a pure model class
public class Frame {
    /**
     * Is this the last frame of the game for a given player.
     */
    private final boolean isLastFrame;
    /**
     * Current score, initialized to 0 but exposed outside the class as -1 for non fully scored frames.
     */
    private int score;
    /**
     * Current status of the frame.
     */
    private FrameStatus status;
    /**
     * Pins scored in 1st attempt
     */
    private int pinsAttempt1;
    /**
     * Pins scored in 2nd attempt
     */
    private int pinsAttempt2;
    /**
     * Pins scored in the bonus attempt, only set when this is the last frame.
     */
    private int pinsBonusAttempt;
    /**
     * How many attempts have been made after the strike.
     */
    private int attemptsAfterStrike;

    /**
     * Initalize the frame with some defaults.
     *
     * @param isLastFrame is this the last frame in a game?
     */
    public Frame(boolean isLastFrame) {
        this.isLastFrame = isLastFrame;
        pinsAttempt1 = pinsAttempt2 = pinsBonusAttempt = -1;
        attemptsAfterStrike = 0;
        score = 0;
        status = FrameStatus.NOT_FULLY_ATTEMPTED;
    }


    /**
     * Determine if player scored a strike in this frame.
     *
     * @return Whether player scored a strike in this frame.
     */
    public boolean isStrike() {
        return pinsAttempt1 == 10;
    }

    /**
     * Determine if player scored a spare in this frame.
     *
     * @return Whether player scored a spare in this frame.
     */
    public boolean isSpare() {
        // Math.max to eliminate one of values being -1
        return (pinsAttempt1 != 10) && (Math.max(pinsAttempt1, 0) + Math.max(pinsAttempt2, 0) == 10);
    }

    public boolean isResultsFinalized() {
        return this.status == FrameStatus.SCORED;
    }

    /**
     * This method accepts the next score for an not fully scored frame &amp;
     * in turns adds the score from the attempt passed as a parameter to the frame score.
     *
     * @param attemptScore Score i.e. number of pins scored during the given attempt.
     * @return State of the frame after the current attempt.
     * @throws AlreadyScoredException In case the caller passes an attempt's score to an already scored frame this exception is thrown,
     *                                this would usually indicate code error.
     *                                Scores from subsequent attempts must only be passed to a frame if it has not already been scored.
     */
    public FrameStatus addScore(int attemptScore) throws AlreadyScoredException {
        // Pre-conditions for method execution.
        if (attemptScore < 0 || attemptScore > 10) {
            throw new IllegalArgumentException("Score can only be in the range of 0-10");
        } else if (this.status.equals(FrameStatus.SCORED)) {
            throw new AlreadyScoredException("Current frame has already been scored, attempt to add new score failed. " +
                    "This may be a code bug.");
        } else if (isLastFrame) { // This is the last frame
            return scoreLastFrame(attemptScore);
        } else { // This is any frame before last frame
            return scoreNotLastFrame(attemptScore);
        }
    }

    /**
     * Manages the logic for scoring last frame (with up to 3 scores in the frame).
     * @param attemptScore Score i.e. number of pins scored during the given attempt.
     * @return State of the frame after the current attempt.
     */
    private FrameStatus scoreLastFrame(int attemptScore) {
        if (pinsAttempt1 == -1) { // Un-scored first pin
            pinsAttempt1 = attemptScore;
            score += attemptScore;
        } else if (pinsAttempt2 == -1) { // In the last frame you always attempt second time
            pinsAttempt2 = attemptScore;
            score += attemptScore;
            if (!(isSpare() || isStrike())) { // Neither a strike of spare, finishes the game
                this.status = FrameStatus.SCORED;
            }
        } else if (isStrike() || isSpare() && pinsBonusAttempt == -1) {
            pinsBonusAttempt = attemptScore;
            score += attemptScore;
            this.status = FrameStatus.SCORED;
        }
        return this.status;
    }

    /**
     * Manages the logic for scoring a "regular" NOT last frame (with max 2 scores in the frame).
     * @param attemptScore Score i.e. number of pins scored during the given attempt.
     * @return State of the frame after the current attempt.
     * @throws AlreadyScoredException In case the caller passes an attempt's score to an already scored frame this exception is thrown,
     *                                this would usually indicate code error.
     *                                Scores from subsequent attempts must only be passed to a frame if it has not already been scored.
     */
    private FrameStatus scoreNotLastFrame(int attemptScore) throws AlreadyScoredException {
        if (pinsAttempt1 == -1) { // Un-scored first pin
            pinsAttempt1 = attemptScore;
            score += attemptScore;
            if (isStrike()) {
                this.status = FrameStatus.ATTEMPTED;
            }
        } else if (isStrike()) {
            if (attemptsAfterStrike >= 2) { // Added for paranoia, this condition would never be true
                // except when frame score state is not finalized after 2 attempts after a strike
                throw new AlreadyScoredException("2 attempts after strike were already counted, " +
                        "this appears to be a code bug.");
            }
            score += attemptScore;
            attemptsAfterStrike += 1;
            if (attemptsAfterStrike == 2) {
                this.status = FrameStatus.SCORED;
            }
        } else if (pinsAttempt2 == -1) { // If the previous attempt wasn't a strike (previous condition), we would mark 2nd score
            pinsAttempt2 = attemptScore;
            score += attemptScore;
            if (!isSpare()) { // After 2nd attempt in frame if we don't have a spare i.e. open frame; finish frame scoring
                this.status = FrameStatus.SCORED;
            } else { // If this was a spare
                this.status = FrameStatus.ATTEMPTED;
            }
        }  else if (isSpare()) {
            score += attemptScore;
            this.status = FrameStatus.SCORED;
        }
        return this.status;
    }

    public FrameStatus getStatus() {
        return status;
    }

    public boolean isLastFrame() {
        return isLastFrame;
    }

    public int getPinsBonusAttempt() {
        return pinsBonusAttempt;
    }

    /**
     * Encapsulates the logic to calculate score
     *
     * @return score if the score has been finalized or -1 otherwise.
     */
    public int getScore() {
        if (this.status.equals(FrameStatus.SCORED)) {
            return score;
        }
        return -1;
    }

    public int getPinsAttempt1() {
        return pinsAttempt1;
    }

    public String getPinsAttempt1Str() {
        return String.valueOf(pinsAttempt1);
    }

    public int getPinsAttempt2() {
        return pinsAttempt2;
    }

    public String getPinsAttempt2Str() {
        if (pinsAttempt2 == 0) {
            return "-";
        }
        if (isStrike() && !isLastFrame()) {
            return "X";
        } else if (isSpare()) {
            return "/";
        } else if (pinsAttempt2 == -1) {
            return " ";
        }
        return String.valueOf(pinsAttempt2);
    }

    @Override
    public String toString() {
        return String.format("[Frame Score:(%s) |%s| |%s|%s]", score, getPinsAttempt1Str(), getPinsAttempt2Str(),
                // Bonus frame displayed only if last frame & player scored a strike or spare
                (isLastFrame && (isStrike() || isSpare())) ? String.format(" |%s|", pinsBonusAttempt) : "");
    }
}
