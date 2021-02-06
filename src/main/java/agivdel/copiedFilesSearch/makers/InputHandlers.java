package agivdel.copiedFilesSearch.makers;

import agivdel.copiedFilesSearch.framework.InputHandler;

import java.nio.file.Files;
import java.nio.file.Paths;

public class InputHandlers {

    public static class Directory implements InputHandler {
        private final String message;

        public Directory(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public boolean isValid(String select) {
            return Files.isDirectory(Paths.get(select).normalize());
        }
    }

    public static class Option implements InputHandler {
        private final String message;

        public Option(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return message;
        }

        @Override
        public boolean isValid(String select) {
            return select.equals("0") || select.equals("1");
        }
    }
}
