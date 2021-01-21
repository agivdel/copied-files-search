package agivdel.copiedFilesSearch;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import java.util.zip.CRC32;

import static java.util.stream.Collectors.*;

/**
 * Группировка файлов по времени последнего редактирования, затем по контрольной сумме CRC32.
 */

public class Searcher {

    public List<Doubles> getDoublesByTimeFirst(List<File> files) {
        return splitByTime(new Doubles(files))
                .flatMap(this::splitByChecksum)
                .collect(toList());
    }

    public List<Doubles> getDoublesByChecksumFirst(List<File> files) {
        return splitByChecksum(new Doubles(files))
                .flatMap(this::splitByTime)
                .collect(toList());
    }

    private Stream<Doubles> splitByTime(Doubles doubles) {
        return doubles.getDoubles().stream()
                .collect(groupingBy(File::lastModified))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    //каждый doubles - своя контрольная сумма (по CRC32); равна для файлов (не копий) одного (в том числе нулевого) размера
    private Stream<Doubles> splitByChecksum(Doubles doubles) {
        return doubles.getDoubles().stream()
                .collect(groupingBy(this::getCRC32))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    private Long getCRC32(File file) {
        System.out.println("CRC32 from Searcher");
        CRC32 check = new CRC32();
        byte[] buf = new byte[8000];//для чтения блоками по 8 КБ
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