package com.foo.bowling.engine;

import com.foo.bowling.utils.exceptions.AlreadyScoredException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FrameTest {

    Frame frame;

    @BeforeEach
    public void setup() {
        frame = new Frame(false);
    }

    @Test
    void testIllegalArgumentThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, ()-> frame.addScore(-1));
        Assertions.assertThrows(IllegalArgumentException.class, ()-> frame.addScore(11));
    }

    @Test
    void testIsStrikeTrue() throws AlreadyScoredException {
        frame.addScore(10);
        boolean result = frame.isStrike();
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(-1, frame.getScore());
        Assertions.assertEquals(10, frame.getPinsAttempt1());
        Assertions.assertEquals(-1, frame.getPinsAttempt2());
    }

    @Test
    void testIsStrikeFalse() throws AlreadyScoredException {
        frame.addScore(6);
        frame.addScore(3);
        boolean result = frame.isStrike();
        Assertions.assertEquals(false, result);
        Assertions.assertEquals(9, frame.getScore());
        Assertions.assertEquals(6, frame.getPinsAttempt1());
        Assertions.assertEquals(3, frame.getPinsAttempt2());
    }

    @Test
    void testIsSpareTrue() throws AlreadyScoredException {
        frame.addScore(2);
        frame.addScore(8);
        boolean result = frame.isSpare();
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(-1, frame.getScore());
        Assertions.assertEquals(2, frame.getPinsAttempt1());
        Assertions.assertEquals(8, frame.getPinsAttempt2());
    }

    @Test
    void testIsSpareFalse() throws AlreadyScoredException {
        frame.addScore(2);
        frame.addScore(6);
        boolean result = frame.isSpare();
        Assertions.assertEquals(false, result);
        Assertions.assertEquals(8, frame.getScore());
        Assertions.assertEquals(2, frame.getPinsAttempt1());
        Assertions.assertEquals(6, frame.getPinsAttempt2());
    }

    @Test
    void testIsResultsFinalizedTrueOpenFrame() throws AlreadyScoredException {
        frame.addScore(2);
        frame.addScore(6);
        boolean result = frame.isResultsFinalized();
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(8, frame.getScore());
        Assertions.assertEquals(2, frame.getPinsAttempt1());
        Assertions.assertEquals(6, frame.getPinsAttempt2());
    }

    @Test
    void testIsResultsFinalizedTrueAfterStrike() throws AlreadyScoredException {
        frame.addScore(10);
        frame.addScore(6);// 2 attempts after a strike
        frame.addScore(4);
        boolean result = frame.isResultsFinalized();
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(20, frame.getScore());
        Assertions.assertEquals(10, frame.getPinsAttempt1());
        Assertions.assertEquals(-1, frame.getPinsAttempt2());
    }

    @Test
    void testIsResultsFinalizedTrueSpare() throws AlreadyScoredException {
        frame.addScore(6);
        frame.addScore(4);// 1 attempt after a spare finalizes the score
        FrameStatus state = frame.addScore(5);
        boolean result = frame.isResultsFinalized();
        Assertions.assertEquals(true, result);
        Assertions.assertEquals(FrameStatus.SCORED, state);
        Assertions.assertTrue(frame.isResultsFinalized());
        Assertions.assertEquals(15, frame.getScore());
        Assertions.assertEquals(6, frame.getPinsAttempt1());
        Assertions.assertEquals(4, frame.getPinsAttempt2());
    }

    @Test
    void testIsExceptionThrownSpare() throws AlreadyScoredException {
        frame.addScore(6);
        frame.addScore(4);// 1 attempt after a spare finalizes the score
        frame.addScore(5);
        // So another attempt after it should throw
        Assertions.assertThrows(AlreadyScoredException.class, ()-> frame.addScore(5));
        Assertions.assertEquals(15, frame.getScore());
        Assertions.assertEquals(6, frame.getPinsAttempt1());
        Assertions.assertEquals(4, frame.getPinsAttempt2());
    }

    @Test
    void testIsExceptionThrownStrike() throws AlreadyScoredException {
        frame.addScore(10);
        frame.addScore(4);
        frame.addScore(5); // 2nd attempt after a strike finalizes the score
        // So another attempt after it should throw
        Assertions.assertThrows(AlreadyScoredException.class, ()-> frame.addScore(5));
        Assertions.assertEquals(19, frame.getScore());
        Assertions.assertEquals(10, frame.getPinsAttempt1());
        Assertions.assertEquals(-1, frame.getPinsAttempt2());
    }

    @Test
    void testIsExceptionThrownOpenFrame() throws AlreadyScoredException {
        frame.addScore(5);
        frame.addScore(4);// Open frame should be final now
        // So another attempt after it should throw
        Assertions.assertThrows(AlreadyScoredException.class, ()-> frame.addScore(5));
        Assertions.assertEquals(9, frame.getScore());
        Assertions.assertEquals(5, frame.getPinsAttempt1());
        Assertions.assertEquals(4, frame.getPinsAttempt2());
    }

    @Test
    void testAddScoreFirstScore() throws AlreadyScoredException {
        FrameStatus result = frame.addScore(0);
        Assertions.assertEquals(FrameStatus.NOT_FULLY_ATTEMPTED, result);
        Assertions.assertEquals(-1, frame.getScore());
    }

    @Test
    void testGetScore() {
        int result = frame.getScore();
        Assertions.assertEquals(-1, result);
    }

    @Test
    void testToStringWithAllScored() throws AlreadyScoredException {
        frame.addScore(5);
        frame.addScore(4);
        Assertions.assertEquals("[Frame Score:(9) |5| |4|]", frame.toString());
    }

    @Test
    void testToStringWithOnlyFirstScore() throws AlreadyScoredException {
        frame.addScore(5);
        frame.addScore(0);
        Assertions.assertEquals("[Frame Score:(5) |5| |-|]", frame.toString());
    }

    @Test
    void testToStringWithSpare() throws AlreadyScoredException {
        frame.addScore(5);
        frame.addScore(5);
        Assertions.assertEquals("[Frame Score:(10) |5| |/|]", frame.toString());
    }

    @Test
    void testToStringWithStrike() throws AlreadyScoredException {
        frame.addScore(10);
        Assertions.assertEquals("[Frame Score:(10) |10| |X|]", frame.toString());
    }
}