package agivdel.copiedFilesSearch.Makers;

import java.nio.file.Files;
import java.nio.file.Paths;

public class DirectoryInputHandler implements InputHandler {//TODO delete after test fixing
    private final String message;

    public DirectoryInputHandler(String message) {
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