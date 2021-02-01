package agivdel.copiedFilesSearch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.groupingBy;

/**
 * Группировка файлов по времени последнего редактирования, затем по контрольной сумме CRC32.
 */

public class Searcher {
    private static Checksum check;

    public static void setCheck(Checksum check) {
        Searcher.check = check;
    }

    public List<Doubles> getDoublesByTimeFirst(List<Forms> files) {
        return splitByTime(new Doubles(files))
                .flatMap(this::splitByChecksum)
                .collect(toList());
    }

    public List<Doubles> getDoublesByChecksumFirst(List<Forms> files) {
        return splitByChecksum(new Doubles(files))
                .flatMap(this::splitByTime)
                .collect(toList());
    }

    private Stream<Doubles> splitByTime(Doubles doubles) {
        return doubles.getDoubles()
                .stream()
                .collect(groupingBy(Forms::lastModified))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    //каждый doubles - своя контрольная сумма (по CRC32); равна для файлов (не копий) одного (в том числе нулевого) размера
    private Stream<Doubles> splitByChecksum(Doubles doubles) {
        return doubles.getDoubles()
                .stream()
//                .collect(groupingBy(Forms::getChecksum))
                .collect(groupingBy(this::getChecksum))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    private long getChecksum(Forms form) {
//        CRC32 check = new CRC32();
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