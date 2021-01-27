package agivdel.copiedFilesSearch;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileRepository {

    void create(Path path, String fileName) {
        Path filePath = path.resolve(fileName);
        try {
            Files.createFile(filePath);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    String read(Path path) {
        try {
            return new String(Files.readAllBytes(path));
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    String update(Path path, String newContent) {
        try {
            Files.write(path, newContent.getBytes());
            return newContent;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    void delete(Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
