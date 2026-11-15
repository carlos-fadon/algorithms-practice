# Algorithms practice

Algorithm and data-structure exercises, each with a test harness, solved in Java
(and later in other languages).

## Layout

```
java/      Maven project: src/main for solutions, src/test for the tests
```

Exercises are grouped by topic, in packages such as `practice.strings`.

## Running the tests

Java 21 or newer. The Maven wrapper downloads Maven the first time.

```bash
cd java
./mvnw test                     # everything
./mvnw test -Dtest=BananaTest   # one exercise
```

## Exercises

| Topic | Exercise | Code | Test |
|---|---|---|---|
| Strings | How many times can "BANANA" be written with the letters of S? | [`Banana`](java/src/main/java/practice/strings/Banana.java) | [`BananaTest`](java/src/test/java/practice/strings/BananaTest.java) |
| Graphs | All roads lead to Rome: find the city every other city can reach | [`RoadsToRome`](java/src/main/java/practice/graphs/RoadsToRome.java) | [`RoadsToRomeTest`](java/src/test/java/practice/graphs/RoadsToRomeTest.java) |
| Greedy | The number below S with the largest digit sum | [`MaxDigitSum`](java/src/main/java/practice/greedy/MaxDigitSum.java) | [`MaxDigitSumTest`](java/src/test/java/practice/greedy/MaxDigitSumTest.java) |

## License

[MIT](LICENSE)
