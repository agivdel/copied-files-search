package agivdel.copiedFilesSearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Walker {
    public interface FileScanner {
        List<Form> scan (String name);
    }

    public static List<Form> allFilesFrom(String selectedDirectory) {
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

    private static Form toForm(Path path) {
        long size = 0;
        long time = 0;
        try {
            size = Files.size(path);
            time = Files.getLastModifiedTime(path).toMillis() / 1000;
        } catch (IOException e) {
            //TODO дописать обработку исключения
            e.printStackTrace();
        }
        return new WorkForm(path, size, time);
    }

    public interface ZeroRemover {
        List<Form> remove(List<Form> files);
    }

    public static List<Form> removeZeroSizeForm(List<Form> files) {
        return files.stream()
                .filter(f -> f.size() != 0)
                .collect(toList());
    }
}