package agivdel.copiedFilesSearch;

import java.io.File;
import java.util.List;

/**
 * Объект класса Double хранит список файлов
 */

public class Doubles {
    private final List<File> doubles;

    public Doubles(List<File> files) {
        this.doubles = files;
    }

    public List<File> getDoubles() {
        return doubles;
    }
}
