package com.foo.bowling.application;

import com.foo.bowling.utils.Constants;
import com.foo.bowling.utils.exceptions.AlreadyScoredException;
import com.foo.bowling.utils.exceptions.MaximumFrameAttemptException;
import com.foo.bowling.engine.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class GameOfBowlingConsoleRunner {
    private static final Logger log = LoggerFactory.getLogger(GameOfBowlingConsoleRunner.class);
    private final Game game;
    public GameOfBowlingConsoleRunner(final Game game) {
        this.game = game;
    }

    /**
     * Runs the game to completion for a single player, player is prompted to enter their status.
     * @return 0 if the game completes successfully.
     */
    public int run() {
        System.out.println(Constants.WELCOME_MESSAGE);
        boolean gameCompleted = false;
        try {
            while(!gameCompleted) {
                gameCompleted = readScore();
            }
        } catch (MaximumFrameAttemptException | AlreadyScoredException e) {
            log.error("FATAL: Game allowed user to enter more input than expected," +
                    " resulting into exception, this may be a code error", e);
            return -1;
        }
        System.out.print(String.format(Constants.GAME_OVER_MESSAGE, game.getGameScore()));
        return 0;
    }

    /**
     * Read an individual attempt score from console &amp; add it to the game score.
     * @return true if the game was completed.
     * @throws MaximumFrameAttemptException Thrown when maximum frames for the player in the game have been reached.
     * @throws AlreadyScoredException This exception is thrown when a score is added to an already scored frame.
     */
    public boolean readScore() throws MaximumFrameAttemptException, AlreadyScoredException {
        System.out.println(String.format(Constants.SCORE_ENTRY_MESSAGE));
        boolean validInput = false;
        int inputScore = 0;
        while(!validInput) {
            Scanner input = new Scanner(System.in);
            String str = input.nextLine();
            try {
                inputScore = Integer.parseInt(str);
                if (inputScore >= 0 && inputScore <= 10) {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                log.error("Invalid number entered in input {}", input);
            }
            if(!validInput) {
                System.out.println(String.format(Constants.INVALID_ENTRY_MESSAGE, str));
            }
        }
        boolean isGameOver = game.addAttemptScore(inputScore);
        System.out.println(game.toString());
        return isGameOver;
    }
}
