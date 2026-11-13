package practice.graphs;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoadsToRomeTest {

    @Test
    @DisplayName("Examples from the description")
    void examples() {
        assertEquals(0, RoadsToRome.findRome(new int[] {2, 3, 1}, new int[] {1, 1, 0}));
        assertEquals(2, RoadsToRome.findRome(new int[] {0, 1, 3, 4}, new int[] {1, 2, 2, 2}));
        assertEquals(-1, RoadsToRome.findRome(new int[] {1, 1}, new int[] {0, 2}));
    }

    @Test
    @DisplayName("Two cities, one road")
    void singleRoad() {
        assertEquals(1, RoadsToRome.findRome(new int[] {0}, new int[] {1}));
        assertEquals(0, RoadsToRome.findRome(new int[] {1}, new int[] {0}));
    }

    @Test
    @DisplayName("Rome can have any number, and the roads can come in any order")
    void anyNumberAnyOrder() {
        // star: every city has a direct road to city 4
        assertEquals(4, RoadsToRome.findRome(new int[] {3, 0, 2, 1}, new int[] {4, 4, 4, 4}));
        // chain 3 -> 0 -> 2 -> 1, roads listed out of order
        assertEquals(1, RoadsToRome.findRome(new int[] {2, 3, 0}, new int[] {1, 0, 2}));
    }

    @Test
    @DisplayName("No Rome when the roads point away from each other")
    void noRome() {
        // 0 <- 1 -> 2 <- 3: cities 0 and 2 are both dead ends
        assertEquals(-1, RoadsToRome.findRome(new int[] {1, 1, 3}, new int[] {0, 2, 2}));
        // 0 -> 1 <- 2 -> 3: cities 1 and 3 are both dead ends
        assertEquals(-1, RoadsToRome.findRome(new int[] {0, 2, 2}, new int[] {1, 1, 3}));
    }

    @Test
    @DisplayName("The input arrays are not modified")
    void inputUnchanged() {
        int[] a = {0, 1, 3, 4};
        int[] b = {1, 2, 2, 2};
        RoadsToRome.findRome(a, b);
        assertArrayEquals(new int[] {0, 1, 3, 4}, a);
        assertArrayEquals(new int[] {1, 2, 2, 2}, b);
    }

    @Test
    @DisplayName("Matches a brute-force search on random road networks")
    void matchesBruteForce() {
        Random random = new Random(753);
        for (int t = 0; t < 3000; t++) {
            int n = 1 + random.nextInt(12);
            int[][] roads = randomNetwork(n, random);
            int expected = bruteForce(roads[0], roads[1]);
            assertEquals(expected, RoadsToRome.findRome(roads[0], roads[1]),
                    () -> "a = " + java.util.Arrays.toString(roads[0])
                        + ", b = " + java.util.Arrays.toString(roads[1]));
        }
    }

    @Test
    @DisplayName("200,000 roads in one long chain, quickly and without a stack overflow")
    void longChain() {
        int n = 200_000;
        int[] a = new int[n];
        int[] b = new int[n];
        for (int k = 0; k < n; k++) {      // k -> k + 1, so Rome is city n
            a[k] = k;
            b[k] = k + 1;
        }
        int result = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> RoadsToRome.findRome(a, b));
        assertEquals(n, result);
    }

    @Test
    @DisplayName("200,000 roads into one city, then with one and two roads reversed")
    void largeStar() {
        int n = 200_000;
        int[] a = new int[n];
        int[] b = new int[n];
        for (int k = 0; k < n; k++) {      // every city 1..n has a road to city 0
            a[k] = k + 1;
            b[k] = 0;
        }
        assertEquals(0, assertTimeoutPreemptively(Duration.ofSeconds(2), () -> RoadsToRome.findRome(a, b)));

        int x = n / 2 + 1;                 // reverse one road: 0 -> x
        a[n / 2] = 0;
        b[n / 2] = x;
        // everyone still reaches 0, and 0 now leads to x: Rome moves to x
        assertEquals(x, assertTimeoutPreemptively(Duration.ofSeconds(2), () -> RoadsToRome.findRome(a, b)));

        a[n / 2 + 1] = 0;                  // reverse a second one: 0 -> x + 1
        b[n / 2 + 1] = x + 1;
        // x and x + 1 are both dead ends now, so there is no Rome
        assertEquals(-1, assertTimeoutPreemptively(Duration.ofSeconds(2), () -> RoadsToRome.findRome(a, b)));
    }

    /**
     * A random connected network of n roads: a random tree on n + 1 cities. Half of the time all
     * roads point towards a random root (so Rome exists), otherwise each direction is random.
     */
    private static int[][] randomNetwork(int n, Random random) {
        int cities = n + 1;
        int[] label = new int[cities];                 // random city numbers
        for (int i = 0; i < cities; i++) label[i] = i;
        for (int i = cities - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = label[i]; label[i] = label[j]; label[j] = tmp;
        }
        boolean towardsRoot = random.nextBoolean();
        int[] a = new int[n];
        int[] b = new int[n];
        for (int child = 1; child < cities; child++) { // node 0 is the root of the tree
            int parent = random.nextInt(child);
            boolean up = towardsRoot || random.nextBoolean();
            a[child - 1] = label[up ? child : parent];
            b[child - 1] = label[up ? parent : child];
        }
        for (int k = n - 1; k > 0; k--) {              // shuffle the order of the roads
            int j = random.nextInt(k + 1);
            int ta = a[k]; a[k] = a[j]; a[j] = ta;
            int tb = b[k]; b[k] = b[j]; b[j] = tb;
        }
        return new int[][] {a, b};
    }

    /** Tries every city: walks the roads backwards from it and checks that every city is reached. */
    private static int bruteForce(int[] a, int[] b) {
        int cities = a.length + 1;
        List<List<Integer>> incoming = new ArrayList<>();
        for (int i = 0; i < cities; i++) incoming.add(new ArrayList<>());
        for (int k = 0; k < a.length; k++) incoming.get(b[k]).add(a[k]);
        for (int rome = 0; rome < cities; rome++) {
            boolean[] seen = new boolean[cities];
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            seen[rome] = true;
            queue.add(rome);
            int reached = 1;
            while (!queue.isEmpty()) {
                for (int from : incoming.get(queue.poll())) {
                    if (!seen[from]) {
                        seen[from] = true;
                        reached++;
                        queue.add(from);
                    }
                }
            }
            if (reached == cities) return rome;
        }
        return -1;
    }
}
