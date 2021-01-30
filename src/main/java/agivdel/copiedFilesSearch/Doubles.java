package agivdel.copiedFilesSearch;

import java.util.List;

/**
 * Объект класса Doubles хранит список файлов
 */

public class Doubles {
    private final List<? extends Forms> doubles;

    public Doubles(List<? extends Forms> files) {
        this.doubles = files;
    }

    public List<? extends Forms> getDoubles() {
        return doubles;
    }
}