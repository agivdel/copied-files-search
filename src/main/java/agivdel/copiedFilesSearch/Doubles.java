package agivdel.copiedFilesSearch;

import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

public class Doubles {
    private final FileTime fileTime;
    private final List<String> doubles;

    public Doubles(FileTime fileTime, List<String> doubles) {
        this.fileTime = fileTime;
        this.doubles = doubles;
    }

    public FileTime getFileTime() {
        return fileTime;
    }

    public List<String> getDoubles() {
        return doubles;
    }

    @Override
    public String toString() {
        return "Double{" +
                "fileTime=" + fileTime +
                ", doubles=" + doubles +
                '}';
    }

    public static Doubles mergeNames(Doubles d1, Doubles d2) {
        List<String> newDoublesList = new ArrayList<>(d1.getDoubles());
        newDoublesList.addAll(d2.getDoubles());
        return new Doubles(d1.getFileTime(), newDoublesList);
    }
}
