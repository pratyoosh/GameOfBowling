package com.foo.bowling;

import org.junit.jupiter.api.Test;

import static com.github.stefanbirkner.systemlambda.SystemLambda.catchSystemExit;
import static com.github.stefanbirkner.systemlambda.SystemLambda.withTextFromSystemIn;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameOfBowlingTest {
    @Test
    void testMain() throws Exception {
        final int exitCode = catchSystemExit(()-> {
                withTextFromSystemIn("10", "10", "10", "10", "10", "10", "10", "10", "10", "10",  "10", "10")
                        .execute(() -> GameOfBowling.main(new String[] {}));
            });
        assertTrue(exitCode == 0, "Exit code success");
    }
}
