package agivdel.copiedFilesSearch;

import java.io.File;

@FunctionalInterface
public interface Checksum {
    long get(File file);
}