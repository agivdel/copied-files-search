package agivdel.copiedFilesSearch;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Walker {
    public static List<Forms> allFilesFrom(String selectedDirectory) {
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .map(Walker::toForm)
                    .collect(toList());
        } catch (IOException e) {
            //TODO дописать обработку исключения
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        FileScanner allFilesFrom = Walker::allFilesFrom;

        FileScanner allFilesFrom2 = name -> allFilesFrom(name);

        FileScanner allFilesFrom3 = new FileScanner() {
            @Override
            public List<Forms> scan(String name) {
                return allFilesFrom(name);
            }
        };
    }

    @FunctionalInterface
    interface FileScanner {
        List<Forms> scan(String name);
    }

    private static Forms toForm(Path path) {
        long size = 0;
        long time = 0;
        try {
            size = Files.size(path);
            time = Files.getLastModifiedTime(path).toMillis() / 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WorkForm(path, size, time);
    }

    public static List<Forms> removeZeroSizeForm(List<Forms> files) {
        return files.stream()
                .filter(f -> f.size() != 0)
                .collect(toList());
    }
}