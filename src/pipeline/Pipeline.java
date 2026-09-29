package pipeline;

public class Pipeline {
    private final Source<Record> source;
    private final Sink<Record> sink;

    public Pipeline(Source<Record> source, Sink<Record> sink) {
        this.source = source;
        this.sink = sink;
    }

    public void run() throws StageException {
        Record record;
        while ((record = source.emit()) != null) {
            sink.consume(record);
        }
    }
}