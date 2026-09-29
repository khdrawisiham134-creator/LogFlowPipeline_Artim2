package pipeline;

import java.io.BufferedReader;
import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        String inputData = "Hello from Pipeline Core!\nThis is a test line.\nProject is running successfully.";

        BufferedReader reader = new BufferedReader(new StringReader(inputData));

        Source<Record> source = new FileLineSource(reader);
        Sink<Record> sink = new ConsoleSink();

        Pipeline pipeline = new Pipeline(source, sink);

        try {
            pipeline.run();
        } catch (StageException e) {
            e.printStackTrace();
        }
    }
}