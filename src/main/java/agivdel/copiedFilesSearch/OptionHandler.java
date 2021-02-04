package agivdel.copiedFilesSearch;

public class OptionHandler implements Handler {
    private final String message;

    public OptionHandler(String message) {
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