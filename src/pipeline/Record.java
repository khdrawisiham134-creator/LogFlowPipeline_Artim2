package pipeline;

public class Record {
    private final String line;

    public Record(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    @Override
    public String toString() {
        return line;
    }
}