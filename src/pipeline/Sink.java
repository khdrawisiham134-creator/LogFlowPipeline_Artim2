package pipeline;

public interface Sink<T> {
    void consume(T item) throws StageException;
}