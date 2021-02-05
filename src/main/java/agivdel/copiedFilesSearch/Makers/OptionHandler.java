package agivdel.copiedFilesSearch.Makers;

public class OptionHandler implements Handler {//TODO delete after test fixing
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