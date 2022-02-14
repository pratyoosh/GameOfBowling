package com.foo.bowling.application;

import com.foo.bowling.utils.Constants;
import com.foo.bowling.utils.exceptions.AlreadyScoredException;
import com.foo.bowling.engine.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOut;
import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

class GameOfBowlingConsoleRunnerTest {
    GameOfBowlingConsoleRunner gameOfBowlingConsoleRunner;
    Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        gameOfBowlingConsoleRunner = Mockito.spy(new GameOfBowlingConsoleRunner(game));
    }

    @Test
    @DisplayName("No score entered, successful run")
    void testRunEmptySuccessfulRun() throws Exception {
        final int expectedGameScore = 0;
        doReturn(true).when(gameOfBowlingConsoleRunner).readScore();
        String messages = tapSystemOut(() -> {
            final int returnCode = gameOfBowlingConsoleRunner.run();
            assertTrue(returnCode != -1);
        });
        assertEquals(String.format(Constants.WELCOME_MESSAGE + "\n" + Constants.GAME_OVER_MESSAGE, expectedGameScore), messages);
    }

    @Test
    @DisplayName("No score entered, successful run")
    void testRunEmptyRunFailure() throws Exception {
        Mockito.doThrow(AlreadyScoredException.class).when(gameOfBowlingConsoleRunner).readScore();
        String messages = tapSystemOut(() -> {
            final int returnCode = gameOfBowlingConsoleRunner.run();
            assertTrue(returnCode == -1);
        });
        assertEquals(Constants.WELCOME_MESSAGE + "\n" , messages);
    }

    @Test
    @DisplayName("No score entered, successful run")
    void testReadScoreWithValidInput() throws Exception {
        final String invalidNumberInput = "4AD";
        String messages = tapSystemOut(() ->
            withTextFromSystemIn("4AD", "4")
                    .execute(()-> {
                        boolean ret = gameOfBowlingConsoleRunner.readScore();
                        assertEquals(false, ret);
                    })
        );
        assertTrue(messages.contains(String.format(Constants.INVALID_ENTRY_MESSAGE, invalidNumberInput)));
        assertTrue(messages.contains(game.toString()));
    }
}
