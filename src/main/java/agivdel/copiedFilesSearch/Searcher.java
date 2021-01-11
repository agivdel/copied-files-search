package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Searcher {

    public void run() {
        //выбрать директорию для обхода (например, получить строку с консоли)
        directoryChoose();
        // обойти файлы в директории:
        //- с помощью рекурсии,
        //или
        //- с помощью лямбды.
        //обработать файл
    }

    //выбор директории обхода
    private void directoryChoose() {
        Scanner scanner = new Scanner(System.in);
        String pathToSearch = scanner.nextLine();
        toProcessFilesFrom2(pathToSearch);
    }


    //обход файлов с помощью рекурсии,
    private void toProcessFilesFrom(File folder) {
        File[] folderEntries = folder.listFiles();
        for (File entry : folderEntries) {
            if (entry.isDirectory()) {
                toProcessFilesFrom(entry);
                continue;
            }
            //обработка файла
            toProcess(entry);
        }
    }

    //обход файлов с помощью рекурсии,
    private void toProcessFilesFrom2(String pathForSearch) {
        List<File> fileList = null;
        try (Stream<Path> pathStream = Files.walk(Paths.get(pathForSearch))) {
            fileList = pathStream.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            //TODO дописать обработку исключения
        }
        System.out.printf("Число файлов в директории %s: %d%n", pathForSearch , fileList.size());
        for (File file : fileList) {
            //обработка файла
            toProcess(file);
        }
    }

    private void toProcess(File file) {
        //проверка файла на копию
        System.out.println(file.getName());
    }
}
