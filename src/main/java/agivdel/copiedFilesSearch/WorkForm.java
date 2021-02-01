package agivdel.copiedFilesSearch;

import com.google.common.base.Suppliers;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.function.Supplier;

public class WorkForm implements Forms{
    private final Path path;
    private final long size;
    private final long lastModifiedTime;
    private final Supplier<Long> checksumSupplier = Suppliers.memoize(this::getChecksumPrivate)::get;

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
    public long getChecksum() {
        return checksumSupplier.get();
    }

    private long getChecksumPrivate() {
        return Checker.getChecksum(this);
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