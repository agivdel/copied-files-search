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
    private final long lastModifiedTime;

    public TestForm(String name, long size) {
        this.name = name;
        this.size = size;
        this.createTime = setCreateTime();
        this.lastModifiedTime = this.createTime;
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
    public long getChecksum() {
        return this.size() * 31 + 17;
    }

    public static TestForm copy(Forms forms) {
        TestForm oldForm = (TestForm) forms;
        return new TestForm(oldForm.name + " - копия", oldForm.size(), oldForm.lastModifiedTime);
    }

    public static TestForm copy(Forms forms, String newName) {
        TestForm oldForm = (TestForm) forms;
        return new TestForm(newName, oldForm.size(), oldForm.lastModifiedTime);
    }

    public long createTime() {
        return this.createTime;
    }

    private long setCreateTime() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
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