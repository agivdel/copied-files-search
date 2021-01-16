package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;

import static java.util.stream.Collectors.*;

/**
 * 1.получили лист файлов
 * 2.лист файлов преобразовали в стрим дублей, где каждый дубль - лист файлов с совпадающими временем последнего редактирования
 * 3.каждый дубль разделили на несколько по контрольной сумме, где каждый итоговый дубль - лист файлов с одинаковой контрольной суммой
 * 4.каждый новый дубль, полученный на стадии №3, добавили в общий стрим дублей
 */

public class Searcher {

    public void run() throws IOException {
        while (true) {
            String selectedDirectory = input("To search for copied files, enter the address of the search directory, to exit press z:");
            List<File> fileList = iterationFilesFrom(selectedDirectory);
            System.out.println("Число файлов в данной директории: " + fileList.size());
            String minSize = input("Do you need to search among files with zero size? Yes - 0, No - 1");

            System.out.println("I'm working, don't bother me, please...");
            List<Doubles> doubles = getDoublesList(fileList);
            printAllDoubles(doubles);

        }
    }
    private String input(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        while (true) {
            String select = scanner.nextLine();
            if (select.equalsIgnoreCase("z")) {
                System.exit(0);
            }
            if (select.equals("0") || select.equals("1")) {
                return select;
            }
            if (Files.exists(Path.of(select))) {
                return select;
            }
        }
    }

    private List<File> iterationFilesFrom(String selectedDirectory) {
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            return pathStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(toList());
        } catch (IOException e) {
            //TODO дописать обработку исключения
            throw new RuntimeException(e);
        }
    }

    //группировка файлов по времени последнего редактирования и контрольной сумме CRC32
    private List<Doubles> getDoublesList(List<File> fileList) {
        return getTimeDoubles(fileList).flatMap(d -> {
            try {
                return splitByChecksum(d.getDoubles());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Stream.empty();
        }).collect(Collectors.toList());
    }

    //каждый лист соответствует определенному времени последнего редактирования
    private Stream<Doubles> getTimeDoubles(List<File> fileList) {
        return fileList
                .stream()
                .collect(groupingBy(File::lastModified))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    //каждый лист - своя контрольная сумма (по CRC32); равна для файлов (не копий) одного (в том числе нулевого) размера
    private Stream<Doubles> splitByChecksum(List<File> fileList) throws IOException {
        return fileList.stream()
                .collect(groupingBy(this::getCRC32))
                .values()
                .stream()
                .filter(l -> l.size() > 1)
                .map(Doubles::new);
    }

    private Long getCRC32(File file) {
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

    private void printAllDoubles(List<Doubles> doublesList) throws IOException {
        for (Doubles doubles : doublesList) {
            System.out.println("\nфайл с копиями (последнее время изменения " + Files.getLastModifiedTime(doubles.getDoubles().get(0).toPath()) + "):");
            doubles.getDoubles().stream().map(File::getName).forEach(System.out::println);
        }
        System.out.println("\nВсего файлов-оригиналов, имеющих копии: " + doublesList.size());
    }
}