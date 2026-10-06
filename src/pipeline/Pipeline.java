package pipeline;

public class Pipeline {

    private final Source<Record> source;
    private final Stage<Record, LogRecord> stage;
    private final Sink<LogRecord> sink;

    public Pipeline(
            Source<Record> source,
            Stage<Record, LogRecord> stage,
            Sink<LogRecord> sink
    ) {
        this.source = source;
        this.stage = stage;
        this.sink = sink;
    }

    public void run() throws StageException {

        Record record;

        while ((record = source.emit()) != null) {

            stage.consume(record);

            LogRecord parsed;

            while ((parsed = stage.emit()) != null) {
                sink.consume(parsed);
            }
        }
    }
}