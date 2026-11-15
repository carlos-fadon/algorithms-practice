package practice.greedy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Several answers can be correct, so the tests don't compare with one expected string.
 * They check that the answer is a valid positive integer, smaller than S, with the best digit sum.
 */
class MaxDigitSumTest {

    private static final int LIMIT = 200_000;
    /** BEST[x] = the largest digit sum among 1 .. x - 1, by brute force. */
    private static final int[] BEST = new int[LIMIT + 1];

    @BeforeAll
    static void buildBruteForceTable() {
        int best = 0;
        for (int x = 1; x <= LIMIT; x++) {
            BEST[x] = best;
            best = Math.max(best, digitSum(Integer.toString(x)));
        }
    }

    @ParameterizedTest(name = "{0} -> best digit sum {1}")
    @DisplayName("Examples")
    @CsvSource({
        "52,   13",     // 49
        "777,  24",     // 699
        "19,   9",      // 9 or 18
        "1234, 27",     // 999: shorter than S
        "10,   9",      // 9: the only answer
        "300,  20",     // 299
        "110,  18",     // 99
        "2019, 28",     // 1999
        "9876, 35",     // 8999
        "1000, 27",     // 999
    })
    void examples(String s, int bestSum) {
        check(s, bestSum);
    }

    @Test
    @DisplayName("Both answers accepted when there is a tie")
    void ties() {
        String answer = MaxDigitSum.maxDigitSumBelow("19");
        assertTrue(answer.equals("9") || answer.equals("18"), "got " + answer);
    }

    @Test
    @DisplayName("Every number from 10 to 200,000 matches brute force")
    void matchesBruteForceExhaustively() {
        for (int x = 10; x <= LIMIT; x++) {
            check(Integer.toString(x), BEST[x]);
        }
    }

    @Test
    @DisplayName("Random 2- to 6-digit numbers match brute force")
    void matchesBruteForceRandomly() {
        Random random = new Random(99);
        for (int t = 0; t < 2000; t++) {
            int x = 10 + random.nextInt(LIMIT - 9);
            check(Integer.toString(x), BEST[x]);
        }
    }

    @Test
    @DisplayName("100,000 digits: 1 followed by zeros gives 99...9, one digit shorter")
    void hugePowerOfTen() {
        int n = 100_000;
        String s = "1" + "0".repeat(n - 1);
        checkFast(s, 9 * (n - 1));
    }

    @Test
    @DisplayName("100,000 digits: 2 followed by zeros gives 199...9")
    void hugeTwoThenZeros() {
        int n = 100_000;
        checkFast("2" + "0".repeat(n - 1), 1 + 9 * (n - 1));
    }

    @Test
    @DisplayName("100,000 digits: all nines gives a digit sum of 9N - 1")
    void hugeAllNines() {
        int n = 100_000;
        checkFast("9".repeat(n), 9 * n - 1);
    }

    @Test
    @DisplayName("100,000 digits: 5 followed by nines has many answers, all with digit sum 9N - 5")
    void hugeFiveThenNines() {
        int n = 100_000;
        // 499...9 and 599...98 both have digit sum 9n - 5, and so do others: any of them is fine
        checkFast("5" + "9".repeat(n - 1), 9 * n - 5);
    }

    // ---------------------------------------------------------------- helpers

    private static void checkFast(String s, int bestSum) {
        assertTimeoutPreemptively(Duration.ofSeconds(2), () -> check(s, bestSum));
    }

    /** Calls the solution and checks every property of a correct answer. */
    private static void check(String s, int bestSum) {
        String answer = MaxDigitSum.maxDigitSumBelow(s);
        String shown = s.length() <= 20 ? s : s.substring(0, 10) + "...(" + s.length() + " digits)";
        assertNotNull(answer, "null for " + shown);
        if (answer.isEmpty()) fail("empty answer for " + shown);
        for (int i = 0; i < answer.length(); i++) {
            char c = answer.charAt(i);
            if (c < '0' || c > '9') fail("not a digit: '" + c + "' in the answer for " + shown);
        }
        if (answer.charAt(0) == '0') fail("leading zero or zero in the answer for " + shown);
        assertTrue(isSmaller(answer, s), "the answer is not smaller than " + shown);
        assertEquals(bestSum, digitSum(answer), "digit sum of the answer for " + shown);
    }

    /** a < b for positive integers written without leading zeros. */
    private static boolean isSmaller(String a, String b) {
        if (a.length() != b.length()) return a.length() < b.length();
        return a.compareTo(b) < 0;
    }

    private static int digitSum(String s) {
        int sum = 0;
        for (int i = 0; i < s.length(); i++) sum += s.charAt(i) - '0';
        return sum;
    }
}
