package pipeline;

public interface Stage<I, O> extends Emitter<O>, Sink<I> {
}