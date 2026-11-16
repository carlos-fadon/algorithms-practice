package practice.random;

/**
 * The most horrible parent in the world.
 *
 * <p>You share a rectangular cake between your two children by cutting it at a random point.
 * The only source of randomness is a {@link Coin}, which you may flip as many times as you want.
 * Every possible cut must be reachable, but the distribution does not have to be uniform.
 *
 * <p>Run the tests with: {@code ./mvnw test -Dtest=CakeSplitTest}
 */
public final class CakeSplit {
    private CakeSplit() {}

    /**
     * Part 1: a cake 20 cm long. Returns where to cut it, a whole number from 0 to 20, both
     * included. Every one of the 21 values must be possible.
     */
    public static int cut20(Coin coin) {
        // TODO: your solution here
        throw new UnsupportedOperationException("not solved yet");
    }

    /**
     * Bonus 1: a cake of any size. Returns a whole number from 0 to {@code size}, both included,
     * and every one of those values must be possible.
     *
     * @throws IllegalArgumentException if {@code size} is negative
     */
    public static int cut(int size, Coin coin) {
        // TODO: your solution here
        throw new UnsupportedOperationException("not solved yet");
    }

    /**
     * Bonus 2: a decimal number from 0 to 1, both included, using at most {@code bits} flips.
     *
     * <p>{@code bits} is the extra information the problem asks about: how precise the number
     * must be. With {@code bits} flips there can be at most 2^bits different results, and a good
     * solution reaches all of them, including 0 and 1.
     *
     * <p>Questions to think about: is there a largest useful value of {@code bits}? Does it
     * depend on the computer, or on the type you return?
     */
    public static double fraction(int bits, Coin coin) {
        // TODO: your solution here
        throw new UnsupportedOperationException("not solved yet");
    }
}
