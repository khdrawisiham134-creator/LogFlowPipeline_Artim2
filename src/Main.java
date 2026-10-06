package pipeline;

import java.io.BufferedReader;
import java.io.StringReader;

public class Main {

    public static void main(String[] args) {

        String inputData =
                "127.0.0.1 - - [06/Oct/2026:18:00:00 +0300] \"GET /index.html HTTP/1.1\" 200 123 \"Mozilla/5.0\"\n"
                        + "192.168.1.10 - - [06/Oct/2026:18:01:00 +0300] \"POST /login HTTP/1.1\" 404 50 \"Chrome\"\n";

        BufferedReader reader =
                new BufferedReader(new StringReader(inputData));

        Source<Record> source =
                new FileLineSource(reader);

        ParserStage parser =
                new ParserStage();

        ConsoleSink sink =
                new ConsoleSink();

        Pipeline pipeline =
                new Pipeline(source, parser, sink);

        try {
            pipeline.run();

            System.out.println(
                    "Invalid lines: " + parser.getInvalidCount()
            );

        } catch (StageException e) {
            e.printStackTrace();
        }
    }
}