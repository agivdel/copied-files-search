package agivdel.copiedFilesSearch.Makers;

public class OptionInputHandler implements InputHandler {//TODO delete after test fixing
    private final String message;

    public OptionInputHandler(String message) {
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