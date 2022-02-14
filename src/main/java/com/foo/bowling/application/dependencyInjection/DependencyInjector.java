package com.foo.bowling.application.dependencyInjection;

import com.foo.bowling.application.GameOfBowlingConsoleRunner;
import com.foo.bowling.engine.Frame;
import com.foo.bowling.engine.Game;

public class DependencyInjector {
    public static GameOfBowlingConsoleRunner getRunner() {
        return new GameOfBowlingConsoleRunner(getGame());
    }
    public static Game getGame() {
        return new Game();
    }
    public static Frame getNewFrame(boolean isLastFrame) {
        return new Frame(isLastFrame);
    }
}
