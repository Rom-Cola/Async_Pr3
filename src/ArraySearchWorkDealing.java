import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class ArraySearchWorkDealing {
    private static final int MIN = 1;
    private static final int MAX = 100;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть кількість рядків: ");
        int rows = scanner.nextInt();
        System.out.println("Введіть кількість стовпчиків");
        int cols = scanner.nextInt();

        int[][] array = generateArray(rows, cols, MIN, MAX);
        printArray(array);

        int numberOfThreads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        long startTime = System.nanoTime();

        List<Callable<Integer>> tasks = new ArrayList<>();
        int rowsPerTask = (int) Math.ceil((double) rows / numberOfThreads);

        for (int i = 0; i < rows; i += rowsPerTask) {
            int startRow = i;
            int endRow = Math.min(i + rowsPerTask, rows);
            tasks.add(new SearchTask(array, startRow, endRow));
        }

        List<Future<Integer>> results = executor.invokeAll(tasks);

        Integer foundNumber = null;
        for (Future<Integer> result : results) {
            Integer value = result.get();
            if (value != null) {
                foundNumber = value;
                break;
            }
        }

        executor.shutdown();
        long endTime = System.nanoTime();

        if (foundNumber == null) {
            System.out.println("Число, яке дорівнює сумі його індексів, не знайдено.");
        } else {
            System.out.println("Знайдено число: " + foundNumber);
        }
        System.out.println("Час виконання: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    private static int[][] generateArray(int rows, int cols, int minValue, int maxValue) {
        Random random = new Random();
        int[][] array = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                array[i][j] = random.nextInt(maxValue - minValue + 1) + minValue;
            }
        }
        return array;
    }

    private static void printArray(int[][] array) {
        for (int[] row : array) {
            for (int value : row) {
                System.out.print(value + "\t");
            }
            System.out.println();
        }
    }

    static class SearchTask implements Callable<Integer> {
        private final int[][] array;
        private final int startRow;
        private final int endRow;

        SearchTask(int[][] array, int startRow, int endRow) {
            this.array = array;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public Integer call() {
            for (int i = startRow; i < endRow; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    if (array[i][j] == i + j) {
                        return array[i][j];
                    }
                }
            }
            return null;
        }
    }
}
