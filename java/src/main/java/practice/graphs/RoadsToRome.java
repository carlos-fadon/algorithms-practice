package practice.graphs;

/**
 * All roads lead to Rome.
 *
 * <p>There are N + 1 cities, numbered from 0 to N, and N one-way roads: road K goes from city
 * {@code a[K]} to city {@code b[K]}. Ignoring the directions, the network is connected.
 *
 * <p>Rome is the city that can be reached from every other city by following the roads in their
 * direction. Return its number, or -1 if there is no such city.
 *
 * <p>Examples:
 * <ul>
 *   <li>a = [2, 3, 1], b = [1, 1, 0] gives 0: roads 2 -> 1 -> 0 and 3 -> 1.</li>
 *   <li>a = [0, 1, 3, 4], b = [1, 2, 2, 2] gives 2.</li>
 *   <li>a = [1, 1], b = [0, 2] gives -1: from city 1 you can go to 0 or to 2, but 0 and 2 can't
 *       reach each other.</li>
 * </ul>
 *
 * <p>Limits: N is between 1 and 200,000, so the solution must be fast.
 * Run the tests with: {@code ./mvnw test -Dtest=RoadsToRomeTest}
 */
public final class RoadsToRome {
    private RoadsToRome() {}

    /** N is {@code a.length}; the cities are 0..N. Must not modify {@code a} or {@code b}. */
    public static int findRome(int[] a, int[] b) {
        // TODO: your solution here
        throw new UnsupportedOperationException("not solved yet");
    }
}
