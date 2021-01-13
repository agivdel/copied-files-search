package agivdel.copiedFilesSearch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//сравнение имен - ненадежно
//сравнение расширений - ненадежно
//сравнение размеров - ненадежно
//сравнение побайтово - ненадежно
//сравнение времени последнего изменения

public class Searcher {

    public void run() throws IOException {
        while (true) {
            System.out.println("Для поиска скопированных файлов введите адрес директории поиска, для выхода нажмите z:");
            String selectedDirectory = input();
            List<File> fileList = iterationFilesFrom(selectedDirectory);
            System.out.println("Число файлов в данной директории: " + fileList.size());
            System.out.println(fileList);
            List<List<Path>> timeDoubles = findTimeDoubles(fileList);
            List<List<List<Path>>> sizeDoubles = findSizeDoubles(timeDoubles);
            printThis(sizeDoubles);
        }
    }

    private void printThis(List<List<List<Path>>> sizeDoubles) throws IOException {
        for (List<List<Path>> oneTime : sizeDoubles) {
            if (oneTime.isEmpty()) continue;
            System.out.println("Время " + Files.getLastModifiedTime(oneTime.get(0).get(0)));
            for (List<Path> sizes : oneTime) {
                if (sizes.isEmpty()) continue;
                System.out.println("- Размер " + Files.size(sizes.get(0)) + " bytes");
                sizes.stream().map(Path::getFileName).forEach(System.out::println);
            }
        }
        System.out.println("\nВсего копий различных файлов: " + sizeDoubles.size());
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

    private List<File> iterationFilesFrom(String selectedDirectory) throws IOException {
        List<File> fileList = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(selectedDirectory))) {
            fileList = pathStream.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO дописать обработку исключения
        }
        return fileList;
    }

    private List<List<Path>> findTimeDoubles(List<File> fileList) throws IOException {
        Map<FileTime, List<Path>> timeFileMap = new HashMap<>();
        List<Path> filePathList;
        for (File file : fileList) {
            FileTime fileTime = Files.getLastModifiedTime(file.toPath());
            timeFileMap.putIfAbsent(fileTime, new ArrayList<>());
            filePathList = timeFileMap.get(fileTime);
            filePathList.add(file.toPath());
            timeFileMap.put(fileTime, filePathList);
        }
        return timeFileMap.values().stream().filter(l -> l.size() > 1).collect(Collectors.toList());
    }

    private List<List<List<Path>>> findSizeDoubles(List<List<Path>> timeDoubles) throws IOException {
        List<List<List<Path>>> sizeDoubles = new ArrayList<>();
        for (List<Path> oneTimeDoubles : timeDoubles) {
            Map<Long, List<Path>> sizeMap = new HashMap<>();
            List<Path> oneSizePaths;
            for (Path path : oneTimeDoubles) {
                long size = Files.size(path);
                sizeMap.putIfAbsent(size, new ArrayList<>());
                oneSizePaths = sizeMap.get(size);
                oneSizePaths.add(path);
                sizeMap.put(size, oneSizePaths);
            }
            List<List<Path>> sizes = sizeMap.values().stream().filter(l -> l.size() > 1).collect(Collectors.toList());
            sizeDoubles.add(sizes);
        }
        return sizeDoubles;
    }
}