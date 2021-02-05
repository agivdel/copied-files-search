package agivdel.copiedFilesSearch.framework;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Checker {
    public static long getAdler32(Form form) {
        Checksum check = new Adler32();
        return getChecksum(form, check);
    }

    public static long getCRC32(Form form) {
        Checksum check = new CRC32();
        return getChecksum(form, check);
    }

    private static long getChecksum(Form form, Checksum check) {
        byte[] buf = new byte[8192];
        try (FileInputStream fis = new FileInputStream(form.toPath().toFile())) {
            while (true) {
                int length = fis.read(buf);
                if (length < 0) {
                    break;
                }
                check.update(buf, 0, length);
            }
        } catch (FileNotFoundException e) {
            //TODO дописать обработку исключения
            System.err.println("File not found");
        } catch (IOException e) {
            //TODO дописать обработку исключения
            System.err.println("IO error");
        }
        return check.getValue();
    }
}