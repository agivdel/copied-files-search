package agivdel.copiedFilesSearch;

import java.io.IOException;

/**
 * Некто скопировал на компьютере какое-то количество файлов,
 * изменив у копий названия и, возможно, расширения.
 * Найти все копии.
 */

public class Main {

    public static void main(String[] args) throws IOException {
        new Searcher().run();
    }
}
