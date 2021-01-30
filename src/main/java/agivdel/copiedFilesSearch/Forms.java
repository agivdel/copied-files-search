package agivdel.copiedFilesSearch;

import java.nio.file.Path;

public interface Forms {
    Path toPath();
    long size();
    long lastModified();
    long getCRC32(); //дискуссионно
}