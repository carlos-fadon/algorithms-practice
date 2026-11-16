package practice.random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Randomness can't be tested by looking at a few results. Instead, these tests explore every
 * possible sequence of coin flips, breadth first, and collect every result the method can return.
 */
class CakeSplitTest {

    // ---------------------------------------------------------------- part 1

    @Test
    @DisplayName("Part 1: every cut from 0 to 20 is possible, and nothing else")
    void cut20ReachesEverything() {
        Set<Integer> results = explore(CakeSplit::cut20, 24, 21);
        assertEquals(rangeSet(0, 20), results);
    }

    @Test
    @DisplayName("Part 1: a real random coin always gives a cut between 0 and 20")
    void cut20WithRandomCoin() {
        Random random = new Random(20);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int t = 0; t < 100_000; t++) {
                int c = CakeSplit.cut20(random::nextBoolean);
                assertTrue(0 <= c && c <= 20, "cut " + c);
            }
        });
    }

    // ---------------------------------------------------------------- bonus 1

    @ParameterizedTest(name = "size {0}")
    @DisplayName("Bonus 1: every cut from 0 to size is possible, and nothing else")
    @ValueSource(ints = {0, 1, 2, 3, 7, 8, 20, 33, 100, 255, 256})
    void cutReachesEverything(int size) {
        Set<Integer> results = explore(coin -> CakeSplit.cut(size, coin), 26, size + 1);
        assertEquals(rangeSet(0, size), results);
    }

    @ParameterizedTest(name = "size {0}")
    @DisplayName("Bonus 1: huge cakes stay in range, without overflow")
    @ValueSource(ints = {1_000_000, 1 << 30, Integer.MAX_VALUE})
    void hugeCakes(int size) {
        Random random = new Random(size);
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
            for (int t = 0; t < 10_000; t++) {
                int c = CakeSplit.cut(size, random::nextBoolean);
                assertTrue(0 <= c && c <= size, "cut " + c + " for size " + size);
            }
        });
    }

    @Test
    @DisplayName("Bonus 1: a negative size is rejected")
    void negativeSize() {
        assertThrows(IllegalArgumentException.class, () -> CakeSplit.cut(-1, () -> true));
    }

    // ---------------------------------------------------------------- bonus 2

    @ParameterizedTest(name = "{0} bits")
    @DisplayName("Bonus 2: 2^bits different numbers between 0 and 1, including 0 and 1")
    @ValueSource(ints = {1, 2, 3, 8, 12, 16})
    void fractionUsesAllItsPrecision(int bits) {
        Set<Double> results = explore(coin -> CakeSplit.fraction(bits, coin), bits, 1 << bits);
        for (double x : results) {
            assertTrue(0.0 <= x && x <= 1.0, "result " + x + " is outside [0, 1]");
        }
        assertTrue(results.contains(0.0), "0 must be possible");
        assertTrue(results.contains(1.0), "1 must be possible");
        assertEquals(1 << bits, results.size(), "number of different results with " + bits + " flips");
    }

    @Test
    @DisplayName("Bonus 2: 53 bits with a real random coin stays between 0 and 1")
    void fractionManyBits() {
        Random random = new Random(53);
        for (int t = 0; t < 10_000; t++) {
            double x = CakeSplit.fraction(53, random::nextBoolean);
            assertTrue(0.0 <= x && x <= 1.0, "result " + x);
        }
    }

    // ---------------------------------------------------------------- the explorer

    /** Thrown by the scripted coin when a run needs more flips than its prefix provides. */
    private static final class NeedMoreFlips extends RuntimeException {
        NeedMoreFlips() {
            super(null, null, false, false);
        }
    }

    /**
     * Runs {@code method} once for every sequence of flips it can consume, up to {@code maxFlips}
     * flips, and returns every result. Each run gets a coin that replays a fixed prefix of flips;
     * if the method asks for more, the prefix is extended with heads and with tails, and both are
     * tried later, shortest sequences first. The search stops when {@code stopAfter} different
     * results have been found, or when no sequence of at most {@code maxFlips} flips is left.
     * A method that retries until it is happy has infinitely many flip sequences, so stopping
     * early matters: every value it can return shows up after only a few flips.
     */
    private static <T extends Comparable<T>> Set<T> explore(Function<Coin, T> method, int maxFlips,
                                                            int stopAfter) {
        Set<T> results = new TreeSet<>();
        ArrayDeque<boolean[]> prefixes = new ArrayDeque<>();
        prefixes.add(new boolean[0]);
        long runs = 0;
        while (!prefixes.isEmpty() && results.size() < stopAfter) {
            boolean[] prefix = prefixes.poll();
            int[] used = {0};
            Coin scripted = () -> {
                if (used[0] == prefix.length) throw new NeedMoreFlips();
                return prefix[used[0]++];
            };
            if (++runs > 3_000_000) fail("too many different flip sequences: is the method stopping?");
            try {
                results.add(method.apply(scripted));
            } catch (NeedMoreFlips e) {
                if (prefix.length < maxFlips) {
                    prefixes.add(extend(prefix, true));
                    prefixes.add(extend(prefix, false));
                }
            }
        }
        return results;
    }

    private static boolean[] extend(boolean[] prefix, boolean flip) {
        boolean[] longer = java.util.Arrays.copyOf(prefix, prefix.length + 1);
        longer[prefix.length] = flip;
        return longer;
    }

    private static Set<Integer> rangeSet(int from, int to) {
        Set<Integer> s = new TreeSet<>();
        for (int i = from; i <= to; i++) s.add(i);
        return s;
    }
}
