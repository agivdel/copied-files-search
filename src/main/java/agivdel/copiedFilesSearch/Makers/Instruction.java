package agivdel.copiedFilesSearch.Makers;

import java.util.Objects;

public interface Instruction<IN, OUT> {
    default <OUT2> Instruction<IN, OUT2> andThen(Instruction<OUT, OUT2> next) {
        Objects.requireNonNull(next);
        return new Instruction<IN, OUT2>() {
            @Override
            public OUT2 instruct(IN in) {
                OUT out1 = Instruction.this.instruct(in);
                OUT2 out2 = next.instruct(out1);
                return out2;
            }
        };
    }

    OUT instruct(IN in);
}