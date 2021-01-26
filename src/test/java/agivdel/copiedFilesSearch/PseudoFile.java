package agivdel.copiedFilesSearch;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class PseudoFile extends File {
    private final String name;
    private final long size;
    private long lastModifiedTime;

    public PseudoFile(String name, long size) {
        super(name);
        this.name = name;
        this.size = size;
        this.setLastModifiedTime(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    private void setLastModifiedTime(LocalDateTime now) {
        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        ZoneId zoneId = ZoneId.systemDefault();
        this.lastModifiedTime = time.atZone(zoneId).toEpochSecond();
    }

    private void setLastModifiedTime(long time) {
        this.lastModifiedTime = time;
    }

    public long lastModified() {
        return lastModifiedTime;
    }

    public static PseudoFile copy(PseudoFile oldFile) {
        PseudoFile newFile = new PseudoFile(oldFile.getName() + " - копия", oldFile.getSize());
        long time = newFile.lastModified();
        oldFile.setLastModified(time);
        return newFile;
    }

    @Override
    public String toString() {
        return "PseudoFile{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }
}
