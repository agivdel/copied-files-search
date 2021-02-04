package agivdel.copiedFilesSearch;

import java.util.List;

/**
 * Объект класса Doubles хранит список файлов
 */

public class Doubles {
    private final List<Form> doubles;

    public Doubles(List<Form> files) {
        this.doubles = files;
    }

    public List<Form> getDoubles() {
        return doubles;
    }
}