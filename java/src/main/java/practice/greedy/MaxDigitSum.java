package practice.greedy;

/**
 * Largest digit sum below a number.
 *
 * <p>S is a string of N digits, without leading zeros, that represents a positive integer.
 * Among all positive integers smaller than S, find one with the largest possible sum of digits,
 * and return it as a string: digits only, no leading zeros. If several numbers have that sum,
 * return any of them.
 *
 * <p>Examples:
 * <ul>
 *   <li>"52" gives "49": its digit sum, 13, is the largest below 52.</li>
 *   <li>"777" gives "699", with a digit sum of 24.</li>
 *   <li>"19" gives "9" or "18": both have a digit sum of 9.</li>
 *   <li>"1234" gives "999": the best answer can be shorter than S.</li>
 * </ul>
 *
 * <p>Limits: N is between 2 and 100,000, so S does not fit in a {@code long}, and the solution
 * must be fast. Run the tests with: {@code ./mvnw test -Dtest=MaxDigitSumTest}
 */
public final class MaxDigitSum {
    private MaxDigitSum() {}

    public static String maxDigitSumBelow(String s) {
        // TODO: your solution here
        throw new UnsupportedOperationException("not solved yet");
    }
}
