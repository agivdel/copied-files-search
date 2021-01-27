package agivdel.copiedFilesSearch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.CRC32;

public class WorkForm implements Forms{
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
    public long getCRC32() {
        CRC32 check = new CRC32();
        byte[] buf = new byte[8192];
        try (FileInputStream fis = new FileInputStream(this.toPath().toFile())) {
            while (true) {
                int length = fis.read(buf);
                if (length < 0) {
                    break;
                }
                check.update(buf, 0, length);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("IO error");
        }
        return check.getValue();
    }

    @Override
    public String toString() {
        return "WorkForm{" +
                "path=" + path +
                ", size=" + size +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }
}
