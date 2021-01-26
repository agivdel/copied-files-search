package agivdel.copiedFilesSearch;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class PseudoFile extends File {
    private final String name;
    private final long size;
    private final long createTime;
    private long lastModifiedTime;

    public PseudoFile(String name, long size) {
        super(name);
        this.name = name;
        this.size = size;
        this.createTime = setCreateTime(LocalDateTime.of(LocalDate.now(), LocalTime.now()));
        this.lastModifiedTime = this.createTime;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    @Override
    public long lastModified() {
        return lastModifiedTime;
    }

    private long setCreateTime(LocalDateTime now) {
        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.now());
        ZoneId zoneId = ZoneId.systemDefault();
        return this.lastModifiedTime = time.atZone(zoneId).toEpochSecond();
    }

    @Override
    public boolean setLastModified(long time) {
        this.lastModifiedTime = time;
        return true;
    }

    public static PseudoFile copy(PseudoFile oldFile) {
        PseudoFile newFile = new PseudoFile(oldFile.getName() + " - копия", oldFile.getSize());
        long time = oldFile.lastModified() == oldFile.createTime ? oldFile.createTime : oldFile.lastModifiedTime;
        oldFile.setLastModified(time);
        newFile.setLastModified(time);
        return newFile;
    }

    @Override
    public String toString() {
        return "PseudoFile{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", createTime=" + createTime +
                ", lastModifiedTime=" + lastModifiedTime +
                '}';
    }
}
