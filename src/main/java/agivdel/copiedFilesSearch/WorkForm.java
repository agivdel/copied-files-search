package agivdel.copiedFilesSearch;

import com.google.common.base.Suppliers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.function.Supplier;
import java.util.zip.CRC32;

public class WorkForm implements Forms {
    private final Path path;
    private final long size;
    private final long lastModifiedTime;
    private final Supplier<Long> crc32Supplier = Suppliers.memoize(this::getCRC32Int)::get;

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
        return crc32Supplier.get();
    }

    private long getCRC32Int() {
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
            //ФУ
            System.err.println("File not found");
        } catch (IOException e) {
            //ФУ
            System.err.println("IO error");
        }
        return check.getValue();
    }

    @Override
    public String toString() {
        return "WorkForm{" +
                "path=" + path +
                ", size=" + size +
                ", lastModifiedTime=" + FileTime.fromMillis(lastModifiedTime * 1000) +
                "}";
    }
}
