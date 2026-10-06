package pipeline;

public class ConsoleSink implements Sink<LogRecord> {

    @Override
    public void consume(LogRecord item) throws StageException {

        System.out.println(
                "LogRecord{" +
                        "timestamp=" + item.timestamp() +
                        ", clientIp='" + item.clientIp() + '\'' +
                        ", method='" + item.method() + '\'' +
                        ", path='" + item.path() + '\'' +
                        ", status=" + item.status() +
                        ", bytes=" + item.bytes() +
                        ", userAgent='" + item.userAgent() + '\'' +
                        ", attributes=" + item.attributes() +
                        '}'
        );
    }
}