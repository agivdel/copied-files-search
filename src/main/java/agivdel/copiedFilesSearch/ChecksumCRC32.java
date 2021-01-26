package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCRC32 implements Checksum {
    private File file;

    public long get(File file) {
        this.file = file;
        return this.get();
    }

    @Override
    public long get() {
        CRC32 check = new CRC32();
        byte[] buf = new byte[8192];//для чтения блоками по 8 КБ
        try (FileInputStream fis = new FileInputStream(file)) {
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
}
