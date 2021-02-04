package agivdel.copiedFilesSearch;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DirectoryHandler implements Handler {
    private final String message;

    public DirectoryHandler(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean isValid(String select) {
        return Files.isDirectory(Paths.get(select).normalize());
    }
}