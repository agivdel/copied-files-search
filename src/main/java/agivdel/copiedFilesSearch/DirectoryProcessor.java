package agivdel.copiedFilesSearch;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DirectoryProcessor implements Processor{
    private final String message;

    public DirectoryProcessor(String message) {
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