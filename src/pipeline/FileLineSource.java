package pipeline;

import java.io.BufferedReader;
import java.io.IOException;

public class FileLineSource implements Source<Record> {
    private final BufferedReader reader;

    public FileLineSource(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public Record emit() throws StageException {
        try {
            String line = reader.readLine();
            if (line == null) {
                return null;
            }
            return new Record(line);
        } catch (IOException e) {
            throw new StageException("Error reading line from file", e);
        }
    }
}