package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * Объект класса Double хранит список файлов, сортированных по времени создания
 */

public class Doubles {
    private final List<File> doubles;

    public Doubles(List<File> doubles) {
        this.doubles = doubles.stream()
                .sorted(comparing(this::getCreateTime))
                .collect(toList());
    }

    public List<File> getDoubles() {
        return doubles;
    }

    private FileTime getCreateTime(File file) {
        try {
            return Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime();
        } catch (IOException e) {
            System.err.println("IO error");
            throw new RuntimeException(e);
        }
    }
}
