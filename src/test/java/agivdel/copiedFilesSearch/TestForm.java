package agivdel.copiedFilesSearch;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TestForm implements Forms {
    private final String name;
    private final long size;
    private final long createTime;
    private long lastModifiedTime;

    public TestForm(String name, long size) {
        this.name = name;
        this.size = size;
        this.createTime = setCreateTime();
    }

    public TestForm(String name, long size, long time) {
        this.name = name;
        this.size = size;
        this.createTime = setCreateTime();
        this.lastModifiedTime = time;
    }

    @Override
    public Path toPath() {
        return Paths.get(this.name);
    }

    @Override
    public long size() {
        return this.size;
    }

    @Override
    public long lastModified() {
        return this.lastModifiedTime;
    }

    @Override
    public long getCRC32() {
        return this.size() * 31 + 17;
    }

    public static TestForm copy(TestForm oldForm) {
        TestForm newForm = new TestForm(oldForm.name + " - копия", oldForm.size());
        long time = oldForm.createTime == oldForm.lastModifiedTime ?
                oldForm.createTime :
                oldForm.lastModifiedTime;
        oldForm.setLastModified(time);
        newForm.setLastModified(time);
        return newForm;
    }

    private long setCreateTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
    }

    private void setLastModified(long time) {
        this.lastModifiedTime = time;
    }

    @Override
    public String toString() {
        return "TestForm{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", createTime=" + FileTime.fromMillis(createTime * 1000) +
                ", lastModifiedTime=" + FileTime.fromMillis(lastModifiedTime * 1000) +
                "}";
    }
}