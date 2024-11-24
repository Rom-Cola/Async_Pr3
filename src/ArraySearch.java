import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ArraySearch {
    private static final int MIN = 1;
    private static final int MAX = 100;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введіть кількість рядків: ");
        int rows = scanner.nextInt();
        System.out.println("Введіть кількість стовпчиків");
        int cols = scanner.nextInt();

        int[][] array = generateArray(rows, cols, MIN, MAX);
        printArray(array);

        ForkJoinPool pool = new ForkJoinPool();

        long startTime = System.nanoTime();
        SearchTask task = new SearchTask(array, 0, rows);
        Integer result = pool.invoke(task);
        long endTime = System.nanoTime();

        if (result == null) {
            System.out.println("Число, яке дорівнює сумі його індексів, не знайдено.");
        } else {
            System.out.println("Знайдено число: " + result);
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

    static class SearchTask extends RecursiveTask<Integer> {
        private static final int THRESHOLD = 5;
        private final int[][] array;
        private final int startRow;
        private final int endRow;

        SearchTask(int[][] array, int startRow, int endRow) {
            this.array = array;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        protected Integer compute() {
            if (endRow - startRow <= THRESHOLD) {
                for (int i = startRow; i < endRow; i++) {
                    for (int j = 0; j < array[i].length; j++) {
                        if (array[i][j] == i + j) {
                            return array[i][j];
                        }
                    }
                }
                return null;
            } else {
                int mid = (startRow + endRow) / 2;
                SearchTask task1 = new SearchTask(array, startRow, mid);
                SearchTask task2 = new SearchTask(array, mid, endRow);
                task1.fork();
                Integer result = task2.compute();
                if (result != null) return result;
                return task1.join();
            }
        }
    }
}
