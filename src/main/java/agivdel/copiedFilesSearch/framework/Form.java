package agivdel.copiedFilesSearch.framework;

import java.nio.file.Path;

public interface Form {
    Path toPath();
    long size();
    long lastModified();
}