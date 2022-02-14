package com.foo.bowling.engine;

import com.foo.bowling.utils.exceptions.AlreadyScoredException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FrameTestLastFrame {
    Frame frame;

    @BeforeEach
    public void setup() {
        frame = new Frame(true);
    }

    @Test
    public void testOpenFrame() throws AlreadyScoredException {
        frame.addScore(6);
        frame.addScore(3);
        Assertions.assertEquals(FrameStatus.SCORED, frame.getStatus());
        Assertions.assertTrue(frame.isResultsFinalized());
        Assertions.assertEquals(9, frame.getScore());
    }

    @Test
    public void testSpareFrameIncomplete() throws AlreadyScoredException {
        frame.addScore(6);
        frame.addScore(4);
        Assertions.assertEquals(FrameStatus.NOT_FULLY_ATTEMPTED, frame.getStatus());
    }

    @Test
    public void testSpareFrameComplete() throws AlreadyScoredException {
        frame.addScore(6);
        frame.addScore(4);
        frame.addScore(5);
        Assertions.assertEquals(FrameStatus.SCORED, frame.getStatus());
        Assertions.assertTrue(frame.isResultsFinalized());
        Assertions.assertEquals(15, frame.getScore());
        Assertions.assertEquals(5, frame.getPinsBonusAttempt());
    }

    @Test
    public void testStrikeFrameIncomplete() throws AlreadyScoredException {
        frame.addScore(10);
        frame.addScore(4);
        Assertions.assertEquals(FrameStatus.NOT_FULLY_ATTEMPTED, frame.getStatus());
    }

    @Test
    public void testStrikeFrameComplete() throws AlreadyScoredException {
        frame.addScore(10);
        frame.addScore(4);
        frame.addScore(5);
        Assertions.assertEquals(FrameStatus.SCORED, frame.getStatus());
        Assertions.assertTrue(frame.isResultsFinalized());
        Assertions.assertEquals(19, frame.getScore());
        Assertions.assertEquals(5, frame.getPinsBonusAttempt());
    }

    @Test
    public void testScoredFrameThrowsAdd() throws AlreadyScoredException {
        frame.addScore(10);
        frame.addScore(4);
        frame.addScore(5);
        Assertions.assertEquals(FrameStatus.SCORED, frame.getStatus());
        Assertions.assertTrue(frame.isResultsFinalized());
        Assertions.assertThrows(AlreadyScoredException.class, () -> frame.addScore(5));
    }

    @Test
    void testToStringStrike() throws AlreadyScoredException {
        frame.addScore(10);
        frame.addScore(4);
        frame.addScore(3);
        Assertions.assertEquals("[Frame Score:(17) |10| |4| |3|]", frame.toString());
    }

    @Test
    void testToStringSpare() throws AlreadyScoredException {
        frame.addScore(1);
        frame.addScore(9);
        frame.addScore(3);
        Assertions.assertEquals("[Frame Score:(13) |1| |/| |3|]", frame.toString());
    }

    @Test
    void testToStringOpenFrame() throws AlreadyScoredException {
        frame.addScore(1);
        frame.addScore(4);
        Assertions.assertEquals("[Frame Score:(5) |1| |4|]", frame.toString());
    }
}
