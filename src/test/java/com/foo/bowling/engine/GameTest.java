package com.foo.bowling.engine;

import com.foo.bowling.utils.exceptions.AlreadyScoredException;
import com.foo.bowling.utils.exceptions.MaximumFrameAttemptException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class GameTest {
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    @ParameterizedTest(name = "{index} ==> For game with attempted scores '{0}' expected result is {1}")
    @CsvSource({
            // Courtesy: https://www.kidslearntobowl.com/how-to-keep-score/
            "'6,2,7,2,3,4,8,2,9,0,10,10,10,6,3,8,2,7', 153",
            // Max score in the game
            "'10,10,10,10,10,10,10,10,10,10,10,10', 300",
            // Courtesy: https://templatelab.com/wp-content/uploads/2021/03/bowling-score-sheet-08.jpg
            "'5,5,4,5,8,2,10,0,10,10,6,2,10,4,6,10,10,0', 169",
            "'5,5,4,0,8,1,10,0,10,10,10,10,4,6,10,10,5', 186",
            // Courtesy: https://www.tutorialspoint.com/ten_pin_bowling/ten_pin_bowling_scoring.htm
            "'10,8,2,9,1,8,0,10,10,9,1,9,1,10,10,9,1', 202",
            "'10,10,10,10,10,10,10,10,6,4,10,10,10', 276"

    })
    void testGame(String csvAttemptScores, Integer expectedGameScore) throws MaximumFrameAttemptException, AlreadyScoredException {
        List<Integer> attempts = Arrays.asList(csvAttemptScores.split(",")).stream()
                .map(String::trim)
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (Integer attemptScore: attempts) {
            game.addAttemptScore(attemptScore);
        }
        Assertions.assertEquals(expectedGameScore, game.getGameScore());
        Assertions.assertThrows(MaximumFrameAttemptException.class, () -> game.addNewScoringFrame());
    }

    @Test
    public void testIllegalScoreThrows() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> game.addAttemptScore(12));
    }
}