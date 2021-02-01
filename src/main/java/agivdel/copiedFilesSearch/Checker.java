package agivdel.copiedFilesSearch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.Checksum;

public class Checker {
    private static Checksum check;
    private static Class<? extends Checksum> checksumImplClass;

    public static void setCheck(Checksum check) {
        Checker.check = check;
        checksumImplClass = check.getClass();
    }

    public static long getChecksum(Forms form) {
        try {
            check = checksumImplClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            //TODO дописать обработку исключения
        }
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
