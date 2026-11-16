package practice.random;

/**
 * A coin you can flip as many times as you want. {@code true} means heads.
 *
 * <p>For real use: {@code Coin coin = new java.util.Random()::nextBoolean;}
 * The tests pass their own coins, so that every flip is under their control.
 */
@FunctionalInterface
public interface Coin {
    boolean flip();
}
