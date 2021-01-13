package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class Searcher {

    public void run() throws IOException {
        while (true) {
            System.out.println("Для поиска скопированных файлов введите адрес директории поиска, для выхода нажмите z:");
            String selectedDirectory = input();
            List<File> fileList = iterationFilesFrom(selectedDirectory);
            System.out.println("Число файлов в данной директории: " + fileList.size());
//            List<List<File>> timeDoubles = getTimeDoubles(fileList);
//            List<List<List<File>>> sizeDoubles = getSizeDoubles(timeDoubles);
//            printThis(sizeDoubles);
            System.out.println("с учетом контрольных сумм: \n");
            Map<Long, List<File>> map = getFileListChecksum(fileList);
            map.values().forEach(l -> {
                l.stream().map(File::getName).forEach(System.out::println);
                System.out.println("___________");
            });
            System.out.println();
            System.out.println("с учетом времени последнего изменения: \n");
            map.forEach((k, v) -> {
                Map<Long, List<File>> timeMap = v.stream().collect(Collectors.groupingBy(File::lastModified));
                v = timeMap.values().stream().filter(l -> l.size() > 1).flatMap(Collection::stream).collect(Collectors.toList());
                v.stream().map(File::getName).forEach(System.out::println);
                System.out.println("___________");
            });
            System.out.println("печать всей карты:");
            System.out.println(map);
        }
    }

    private void printThis(List<List<List<File>>> sizeDoubles) throws IOException {
        for (List<List<File>> oneTime : sizeDoubles) {
            if (oneTime.isEmpty()) continue;
            System.out.println("Время " + Files.getLastModifiedTime(oneTime.get(0).get(0).toPath()));
            for (List<File> sizes : oneTime) {
                if (sizes.isEmpty()) continue;
                System.out.println("- Размер " + Files.size(sizes.get(0).toPath()) + " bytes");
                sizes.stream().map(File::getName).forEach(System.out::println);
            }
        }
        System.out.println("\nВсего файлов, имеющих копии: " + sizeDoubles.size());
    }

    private String input() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String selectedDirectory = scanner.nextLine();
            if (selectedDirectory.equals("z")) {
                System.exit(0);
            }
            if (Files.exists(Path.of(selectedDirectory))) {
                return selectedDirectory;
            }
        }
    }

    //не подходит для файлов нулевого размера
    private Map<Long, List<File>> getFileListChecksum(List<File> fileList) throws IOException {
        Map<Long, List<File>> crcMap = new HashMap<>();
        for (File file : fileList) {
            Long key;
            key = getFileCRC32(file);
            crcMap.putIfAbsent(key, new ArrayList<>());
            List<File> list = crcMap.get(key);
            list.add(file);
            crcMap.put(key, list);
        }
        return crcMap.entrySet().stream().filter(e -> e.getValue().size() > 1).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Long getFileCRC32(File file) throws IOException {
        CRC32 check = new CRC32();
        byte[] buf = new byte[8000];//для чтения блоками по 8 КБ
        int length = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (true) {
            assert fis != null;
            length = fis.read(buf);
            if (length < 0) {
                break;
            }
            check.update(buf, 0, length);
        }
        fis.close();
        return check.getValue();
    }

    private List<File> iterationFilesFrom(String selectedDirectory) {
        List<File> fileList = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            fileList = pathStream.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO дописать обработку исключения
        }
        return fileList;
    }

    //Вар.№1
    private List<List<File>> getTimeDoubles(List<File> fileList) {
        Map<Long, List<File>> map = fileList.stream().collect(Collectors.groupingBy(File::lastModified));
        return map.values().stream().filter(l -> l.size() > 1).collect(Collectors.toList());
    }

    //Вар.№2
    private Stream<Stream<File>> getTimeDoublesStream(List<File> fileList) {
        Map<Long, List<File>> map = fileList.stream().collect(Collectors.groupingBy(File::lastModified));
        return map.values().stream().filter(l -> l.size() > 1).map(Collection::stream);
    }

    //TODO и это
    private List<List<List<File>>> getSizeDoubles(List<List<File>> timeDoubles) throws IOException {
        List<List<List<File>>> sizeDoubles = new ArrayList<>();
        for (List<File> oneTimeDoubles : timeDoubles) {
            Map<Long, List<File>> sizeMap = new HashMap<>();
            List<File> oneSizePaths;
            for (File file : oneTimeDoubles) {
                long size = Files.size(file.toPath());
                sizeMap.putIfAbsent(size, new ArrayList<>());
                oneSizePaths = sizeMap.get(size);
                oneSizePaths.add(file);
                sizeMap.put(size, oneSizePaths);
            }
            List<List<File>> sizes = sizeMap.values().stream().filter(l -> l.size() > 1).collect(Collectors.toList());
            sizeDoubles.add(sizes);
        }
        return sizeDoubles;
    }
}