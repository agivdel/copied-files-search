package agivdel.copiedFilesSearch;

import java.io.File;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;

public class Doubles {
//    private final FileTime fileTime;
    private final List<File> doubles;

    public Doubles(/*FileTime fileTime,*/ List<File> doubles) {
//        this.fileTime = fileTime;
        this.doubles = doubles;
    }

//    public FileTime getFileTime() {
//        return fileTime;
//    }

    public List<File> getDoubles() {
        return doubles;
    }

    public long size() {
        return this.doubles.size();
    }

    public boolean isEmpty() {
        return this.doubles.isEmpty();
    }

    @Override
    public String toString() {
        return "Double{" +
                /*"fileTime=" + fileTime +*/
                ", doubles=" + doubles +
                '}';
    }

    public static Doubles merge(Doubles d1, Doubles d2) {
        List<File> newDoublesList = new ArrayList<>(d1.getDoubles());
        newDoublesList.addAll(d2.getDoubles());
        return new Doubles(/*d1.getFileTime(),*/ newDoublesList);
    }
}
