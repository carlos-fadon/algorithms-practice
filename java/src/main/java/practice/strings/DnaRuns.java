package practice.strings;

/**
 * Longest G run plus longest T run in a DNA sequence with unknown bases.
 *
 * <p>The sequence contains only 'G', 'T' and '?'. Every '?' is an unknown base that must be
 * replaced by 'G' or by 'T'. Choose the replacements so that the length of the longest
 * continuous run of 'G' plus the length of the longest continuous run of 'T' is as large as
 * possible, and return that sum.
 *
 * <p>Examples:
 * <ul>
 *   <li>"G?G" gives 3: "GGG" has a G run of 3 and no T, while "GTG" only gives 1 + 1.</li>
 *   <li>"T??G" gives 4: "TGGG" has runs of 1 and 3.</li>
 *   <li>"GG?TT?G" gives 6: for example "GGGTTTG", with runs of 3 and 3.</li>
 *   <li>"GTGT" gives 2: there is nothing to choose.</li>
 * </ul>
 *
 * <p>Limits: the length N is between 1 and 500,000, so the solution must be fast.
 * Run the tests with: {@code ./mvnw test -Dtest=DnaRunsTest}
 */
public final class DnaRuns {
    private DnaRuns() {}

    public static int maxRunSum(String dna) {
        // TODO: your solution here
        throw new UnsupportedOperationException("not solved yet");
    }
}
