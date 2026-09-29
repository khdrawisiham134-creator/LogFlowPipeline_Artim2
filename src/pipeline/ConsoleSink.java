package pipeline;

public class ConsoleSink implements Sink<Record> {
    @Override
    public void consume(Record item) {
        System.out.println(item);
    }
}