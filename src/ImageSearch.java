import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ImageSearch {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Введіть шлях до директорії, у якій хочете знайти зображення: ");
        String directoryPath = scanner.nextLine();

        ForkJoinPool pool = new ForkJoinPool();
        File rootDir = new File(directoryPath);

        if (!rootDir.exists() || !rootDir.isDirectory()) {
            System.out.println("Вказано некоректну директорію.");
            return;
        }

        long startTime = System.nanoTime();
        ImageSearchTask task = new ImageSearchTask(rootDir);
        List<File> images = pool.invoke(task);
        long endTime = System.nanoTime();

        System.out.println("Знайдено файлів зображень: " + images.size());
        if (!images.isEmpty()) {
            try {
                Desktop.getDesktop().open(images.get(images.size() - 1));
                System.out.println("Відкрито останнє зображення: " + images.get(images.size() - 1));
            } catch (IOException e) {
                System.out.println("Помилка відкриття файлу.");
            }
        }
        System.out.println("Час виконання: " + (endTime - startTime) / 1_000_000 + " ms");
    }

    static class ImageSearchTask extends RecursiveTask<List<File>> {
        private final File directory;

        ImageSearchTask(File directory) {
            this.directory = directory;
        }

        @Override
        protected List<File> compute() {
            List<File> images = new ArrayList<>();
            List<ImageSearchTask> tasks = new ArrayList<>();

            File[] files = directory.listFiles();
            if (files == null) return images;

            for (File file : files) {
                if (file.isDirectory()) {
                    ImageSearchTask task = new ImageSearchTask(file);
                    task.fork();
                    tasks.add(task);
                } else if (isImageFile(file)) {
                    images.add(file);
                }
            }

            for (ImageSearchTask task : tasks) {
                images.addAll(task.join());
            }
            return images;
        }

        private boolean isImageFile(File file) {
            String name = file.getName().toLowerCase();
            return name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".gif");
        }
    }
}
