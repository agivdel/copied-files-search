package agivdel.copiedFilesSearch;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.lang.System.*;

public class Walker {

    //static?
    public List<Forms> iterationFilesFrom(String selectedDirectory) {
        out.println("counting files...");
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .map(this::toForm)
                    .collect(toList());
        } catch (IOException e) {
            //TODO дописать обработку исключения
            throw new RuntimeException(e);
        }
    }

    //static?
    private Forms toForm(Path path) {
        long size = 0;
        long time = 0;
        try {
            size = Files.size(path);
            time = Files.getLastModifiedTime(path).toMillis()/1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new WorkForm(path, size, time);
    }

    //static?
    public List<Forms> removeZeroSizeForm(List<Forms> files) {
        out.println("deleting files with zero size...");
        return files.stream()
                .filter(f -> f.size() != 0)
                .collect(toList());
    }
}
