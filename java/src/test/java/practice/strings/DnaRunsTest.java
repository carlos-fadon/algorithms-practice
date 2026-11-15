package practice.strings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DnaRunsTest {

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @DisplayName("Examples")
    @CsvSource({
        "G?G,       3",     // GGG
        "T??G,      4",     // TGGG
        "GG?TT?G,   6",     // GGGTTTG
        "GTGT,      2",     // nothing to choose
        "?GT?,      4",     // GGTT
        "TG?GT?TT,  7",     // TGGGTTTT
        "G?T?G?T,   5",     // GTTGGGT
        "??G??,     5",     // GGGGG: one run can take everything
    })
    void examples(String dna, int expected) {
        assertEquals(expected, DnaRuns.maxRunSum(dna));
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @DisplayName("Single bases")
    @CsvSource({"G, 1", "T, 1", "?, 1"})
    void singleBase(String dna, int expected) {
        assertEquals(expected, DnaRuns.maxRunSum(dna));
    }

    @Test
    @DisplayName("No unknown bases: just measure the two longest runs")
    void noUnknowns() {
        assertEquals(6, DnaRuns.maxRunSum("GGTGGGTTTG"));   // GGG and TTT: 3 + 3
        assertEquals(4, DnaRuns.maxRunSum("GGGG"));         // no T at all: 4 + 0
        assertEquals(4, DnaRuns.maxRunSum("TGGTTG"));       // GG and TT: 2 + 2
    }

    @Test
    @DisplayName("Matches a brute force over every assignment, on random sequences")
    void matchesBruteForce() {
        Random random = new Random(1953);                    // the year DNA's structure was found
        for (int t = 0; t < 3000; t++) {
            int n = 1 + random.nextInt(14);
            int unknownPercent = random.nextInt(101);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if (random.nextInt(100) < unknownPercent) sb.append('?');
                else sb.append(random.nextBoolean() ? 'G' : 'T');
            }
            String dna = sb.toString();
            assertEquals(bruteForce(dna), DnaRuns.maxRunSum(dna), dna);
        }
    }

    @Test
    @DisplayName("500,000 bases, all unknown: one run can take them all")
    void hugeAllUnknown() {
        checkFast("?".repeat(500_000), 500_000);
    }

    @Test
    @DisplayName("500,000 bases: G's, then unknowns, then T's, sums to N")
    void hugeThreeBlocks() {
        checkFast("G".repeat(100_000) + "?".repeat(300_000) + "T".repeat(100_000), 500_000);
    }

    @Test
    @DisplayName("500,000 bases alternating GTGT...: the answer is 2")
    void hugeAlternating() {
        checkFast("GT".repeat(250_000), 2);
    }

    @Test
    @DisplayName("500,000 bases: 200,000 unknowns, then GTGT...: the answer is 200,002")
    void hugeUnknownsThenAlternating() {
        // the unknowns become G's that join the first G, followed by a T run of 1
        checkFast("?".repeat(200_000) + "GT".repeat(150_000), 200_002);
    }

    // ---------------------------------------------------------------- helpers

    private static void checkFast(String dna, int expected) {
        int result = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> DnaRuns.maxRunSum(dna));
        assertEquals(expected, result);
    }

    /** Tries all 2^q ways to replace the q unknown bases. */
    private static int bruteForce(String dna) {
        int q = 0;
        for (char c : dna.toCharArray()) if (c == '?') q++;
        int best = 0;
        for (int mask = 0; mask < (1 << q); mask++) {
            char[] s = dna.toCharArray();
            int bit = 0;
            for (int i = 0; i < s.length; i++) {
                if (s[i] == '?') s[i] = (mask >> bit++ & 1) == 1 ? 'G' : 'T';
            }
            best = Math.max(best, longestRun(s, 'G') + longestRun(s, 'T'));
        }
        return best;
    }

    private static int longestRun(char[] s, char base) {
        int best = 0, current = 0;
        for (char c : s) {
            current = c == base ? current + 1 : 0;
            best = Math.max(best, current);
        }
        return best;
    }
}
