package agivdel.copiedFilesSearch;

import java.io.File;
import java.util.List;

public class Doubles {
    private final List<File> doubles;

    public Doubles(List<File> doubles) {
        this.doubles = doubles;
    }

    public List<File> getDoubles() {
        return doubles;
    }

    @Override
    public String toString() {
        return "\nDouble{" + doubles + "}";
    }
}
