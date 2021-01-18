package agivdel.copiedFilesSearch;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Некто скопировал на компьютере какое-то количество файлов,
 * изменив у копий названия и, возможно, расширения.
 * Найти все копии.
 */

public class Main {
    Searcher searcher = new Searcher();
    Walker walker = new Walker();
    TUI tui = new TUI();

    public static void main(String[] args) throws IOException {
        new Main().run();
    }

    public void run() throws IOException {
        while (true) {
            String selectedDirectory = tui.input("dir", "To search for copied files, enter the address of the search directory, to exit press z:");
            List<File> fileList = walker.iterationFilesFrom(selectedDirectory);
            System.out.println("The number of files in this directory: " + fileList.size());
            String minSize = tui.input("zero", "Do you need to search among files with zero size? 'Yes' - 0, 'No' - 1.");
            if (minSize.equals("1")) {
                fileList = walker.removeZeroSize(fileList);
            }
            System.out.println("I'm working, don't bother me, please...");
            List<Doubles> doublesList = searcher.getDoublesList(fileList);
            tui.printAllDoubles(doublesList);
        }
    }
}