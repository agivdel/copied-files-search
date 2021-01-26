package agivdel.copiedFilesSearch;

public class PseudoChecksumCRC32 implements Checksum{
    private PseudoFile pseudoFile;

    public long get(PseudoFile pseudoFile) {
        this.pseudoFile = pseudoFile;
        return this.get();
    }

    @Override
    public long get() {
        long size = pseudoFile.getSize();
        return size * 31 + 17;
    }
}