package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Searcher {

    public void run() throws IOException {
        //выбрать директорию для обхода (например, получить строку с консоли)
        directoryChoose();
        // обойти файлы в директории:
        //- с помощью рекурсии,
        //или
        //- с помощью лямбды.
        //обработать файл
    }

    //выбор директории обхода
    private void directoryChoose() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String pathToSearch = scanner.nextLine();
        toProcessFilesFrom(pathToSearch);
    }


    //обход файлов с помощью рекурсии,
    private void toProcessFilesFrom2(File folder) throws IOException {
        File[] folderEntries = folder.listFiles();
        for (File entry : folderEntries) {
            if (entry.isDirectory()) {
                toProcessFilesFrom(entry.getAbsolutePath());
                continue;
            }
            //обработка файла
            toProcess(entry);
        }
    }

    //обход файлов с помощью рекурсии,
    private void toProcessFilesFrom(String pathForSearch) throws IOException {
        List<File> fileList = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(pathForSearch))) {
            fileList = pathStream.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO дописать обработку исключения
        }
        System.out.printf("Число файлов в директории %s: %d%n", pathForSearch , fileList.size());
        Map<FileTime, Integer> timeMap = new HashMap<>();
        Map<FileTime, List<String>> fileMap = new HashMap<>();
        List<String> nameList;
        for (File file : fileList) {
            //обработка файла
//            toProcess(file);
            FileTime fileTime = Files.getLastModifiedTime(file.toPath());
//            System.out.println(file.getName() + ", время последнего редактирования: " + fileTime);;
            FileAndTime fileAndTime = new FileAndTime(file.toPath(), fileTime);
            timeMap.merge(fileTime, 1, Integer::sum);
//            fileMap.put(fileTime, fileAndTime);
            nameList = fileMap.get(fileTime);
            if (nameList == null) {
                nameList = new ArrayList<>();
            }
            nameList.add(file.getName());
            fileMap.put(fileTime, nameList);

        }
        List<FileTime> doubles = timeMap
                .entrySet()
                .stream()
                .filter(f -> f.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        System.out.println("Число файлов с дублями: " + doubles.size());
        System.out.println(doubles);
        for (FileTime fileTime : doubles) {
            System.out.println("Время изменения: " + fileTime + ", файлы: " + fileMap.get(fileTime));
        }

    }

    private void toProcess(File file) throws IOException {
        //проверка файла на копию
        System.out.println(file.getName());
        System.out.println(Files.getLastModifiedTime(file.toPath()));//сравнение времени последнего изменения

        //сравнение имен - ненадежно
        //сравнение расширений - ненадежно
        //сравнение размеров - ненадежно
        //сравнение побитово - ненадежно
        //сравнение времени последнего изменения


    }
}
