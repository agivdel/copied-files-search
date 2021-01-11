package agivdel.copiedFilesSearch;

import java.nio.file.Path;
import java.nio.file.attribute.FileTime;

public class FileAndTime {
    public Path filePath;
    public FileTime fileTime;

    public FileAndTime(Path filePath, FileTime fileTime) {
        this.filePath = filePath;
        this.fileTime = fileTime;
    }

    public Path getFilePath() {
        return filePath;
    }

    public FileTime getFileTime() {
        return fileTime;
    }

    @Override
    public String toString() {
        return "FileAndTime{" +
                "filePath=" + filePath +
                ", fileTime=" + fileTime +
                '}';
    }
}
