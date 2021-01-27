package agivdel.copiedFilesSearch;

import java.util.List;

/**
 * Объект класса Doubles хранит список файлов
 */

public class Doubles {
    private final List<Forms> doubles;

    public Doubles(List<Forms> files) {
        this.doubles = files;
    }

    public List<Forms> getDoubles() {
        return doubles;
    }
}