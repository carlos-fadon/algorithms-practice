package practice.strings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BananaTest {

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @DisplayName("Examples")
    @CsvSource({
        "XANBNAYA,          1",
        "BAANNNABANAAB,     2",
        "ABBAZZ,            0",
        "BANANA,            1",
        "ANANAB,            1",      // order does not matter
        "BANANABANANA,      2",
        "BBBBBANANA,        1",      // extra B are useless
        "BANANAAAAAAA,      1",      // extra A are useless
        "BANAN,             0",      // one A missing
        "AAAAAANNNNBB,      2",
        "XYZ,               0",
    })
    void examples(String s, int expected) {
        assertEquals(expected, Banana.countBanana(s));
    }

    @Test
    @DisplayName("Empty string gives 0")
    void emptyString() {
        assertEquals(0, Banana.countBanana(""));
    }

    @Test
    @DisplayName("The scarcest letter decides: B, A (needs 3) or N (needs 2)")
    void limitingLetter() {
        assertEquals(3, Banana.countBanana("B".repeat(3) + "A".repeat(100) + "N".repeat(100)));
        assertEquals(4, Banana.countBanana("B".repeat(100) + "A".repeat(14) + "N".repeat(100)));
        assertEquals(5, Banana.countBanana("B".repeat(100) + "A".repeat(100) + "N".repeat(11)));
    }

    @Test
    @DisplayName("Matches a brute-force simulation on random strings")
    void matchesBruteForce() {
        Random random = new Random(2026);
        String alphabet = "BANXY";
        for (int t = 0; t < 2000; t++) {
            int n = random.nextInt(40);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
            String s = sb.toString();
            assertEquals(bruteForce(s), Banana.countBanana(s), s);
        }
    }

    @Test
    @DisplayName("Handles a million letters quickly")
    void largeInput() {
        String s = "BANANA".repeat(200_000);               // 1.2 million letters
        int result = assertTimeoutPreemptively(Duration.ofSeconds(1), () -> Banana.countBanana(s));
        assertEquals(200_000, result);
    }

    /** Writes BANANA again and again, crossing out one letter at a time, until a letter is missing. */
    private static int bruteForce(String s) {
        List<Character> letters = new ArrayList<>();
        for (char c : s.toCharArray()) letters.add(c);
        int count = 0;
        while (true) {
            for (char c : "BANANA".toCharArray()) {
                if (!letters.remove(Character.valueOf(c))) return count;
            }
            count++;
        }
    }
}
