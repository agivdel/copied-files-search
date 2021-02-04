package agivdel.copiedFilesSearch;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public class WorkForm implements Form {
    private final Path path;
    private final long size;
    private final long lastModifiedTime;

    public WorkForm(Path path, long size, long lastModifiedTime) {
        this.path = path;
        this.size = size;
        this.lastModifiedTime = lastModifiedTime;
    }

    @Override
    public Path toPath() {
        return path;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public long lastModified() {
        return lastModifiedTime;
    }

    @Override
    public String toString() {
        return  "WorkForm{" +
                "path=" + path +
                ", size=" + size +
                ", lastModifiedTime=" + FileTime.fromMillis(lastModifiedTime * 1000) +
                "}";
    }
}