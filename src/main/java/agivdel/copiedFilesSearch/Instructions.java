package agivdel.copiedFilesSearch;

import java.util.Objects;

public interface Instructions<IN, OUT> {
    default <OUT2> Instructions<IN, OUT2> linkWith(Instructions<OUT, OUT2> next) {
        Objects.requireNonNull(next);
        return new Instructions<IN, OUT2>() {
            @Override
            public OUT2 instruct(IN in) {
                OUT out1 = Instructions.this.instruct(in);
                OUT2 out2 = next.instruct(out1);
                return out2;
            }
        };
    }

    OUT instruct(IN in);
}