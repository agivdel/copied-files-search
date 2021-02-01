package agivdel.copiedFilesSearch;

public class OptionProcessor implements Processor{
    private final String message;

    public OptionProcessor(String message) {
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