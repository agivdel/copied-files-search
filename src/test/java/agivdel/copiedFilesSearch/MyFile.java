package agivdel.copiedFilesSearch;

import java.nio.file.Path;
import java.nio.file.Paths;

public class MyFile implements Forms {

    final long size;
    final long crc32;
    final long time;
    final String name;

    public MyFile(String name, long size, long crc32, long time) {
        this.name = name;
        this.size = size;
        this.crc32 = crc32;
        this.time = time;
    }

    @Override
    public Path toPath() {
        return Paths.get(name);
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public long lastModified() {
        return time;
    }

    @Override
    public long getCRC32() {
        return crc32;
    }

    public static MyFile notEmpty(String name, long crc32) {
        return new MyFile(name, 1, crc32, 100500);
    }
}
