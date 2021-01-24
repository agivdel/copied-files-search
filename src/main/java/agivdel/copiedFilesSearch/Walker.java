package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.lang.System.*;

public class Walker {

    public List<File> iterationFilesFrom(String selectedDirectory) {
        out.println("counting files...");
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(toList());
        } catch (IOException e) {
            //TODO дописать обработку исключения
            throw new RuntimeException(e);
        }
    }

    public List<File> removeZeroSize(List<File> fileList) {
        out.println("deleting files with zero size...");
        return fileList.stream()
                .filter(f -> {
                    try {
                        return Files.size(f.toPath()) != 0;
                    } catch (IOException e) {
                        System.err.println("IO error");
                    }
                    return false;
                })
                .collect(toList());
    }
}
