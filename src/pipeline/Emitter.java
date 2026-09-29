package pipeline;

public interface Emitter<T> {
    T emit() throws StageException;
}