package agivdel.copiedFilesSearch;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Handlers {

    static class Directory implements Handler {
        private final String message;

        public Directory(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean isValid(String select) {
            return Files.isDirectory(Paths.get(select).normalize());
        }
    }

    static class Option implements Handler {
        private final String message;

        public Option(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean isValid(String select) {
            return select.equals("0") || select.equals("1");
        }
    }
}
