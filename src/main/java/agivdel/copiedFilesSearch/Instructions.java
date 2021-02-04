package agivdel.copiedFilesSearch;

import java.util.Objects;

public interface Instructions<IN, OUT> {
    default <OUT2> Instructions<IN, OUT2> linkWith(Instructions<OUT, OUT2> next) {
        Objects.requireNonNull(next);
        return new Instructions<IN, OUT2>() {
            @Override
            public OUT2 instruct(IN in) {
                OUT out = Instructions.this.instruct(in);
                OUT2 instruct = next.instruct(out);
                return instruct;
            }
        };
    }

    OUT instruct(IN in);
}