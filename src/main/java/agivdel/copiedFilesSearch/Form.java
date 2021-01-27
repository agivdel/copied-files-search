package agivdel.copiedFilesSearch;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public class Form {
    private final Path path;
    private final long size;
    private final FileTime time;

    public Form(Path path, long size, FileTime time) {
        this.path = path;
        this.size = size;
        this.time = time;
    }

    public Path getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public FileTime getTime() {
        return time;
    }
}
