package pipeline;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserStage implements Stage<Record, LogRecord> {

    private final Queue<LogRecord> output = new ArrayDeque<>();
    private int invalidCount = 0;

    private static final Pattern PATTERN = Pattern.compile(
            "^(\\S+) \\S+ \\S+ \\[([^]]+)\\] \"(\\S+) (.*?) HTTP/[^\\\"]+\" (\\d{3}) (\\S+) \"([^\"]*)\"$"
    );

    @Override
    public void consume(Record item) throws StageException {

        if (item == null || item.getLine() == null
                || item.getLine().trim().isEmpty()) {
            invalidCount++;
            return;
        }

        String line = item.getLine().trim();

        Matcher matcher = PATTERN.matcher(line);

        if (!matcher.matches()) {
            invalidCount++;
            return;
        }

        try {
            String clientIp = matcher.group(1);
            String timestampText = matcher.group(2);
            String method = matcher.group(3);
            String path = matcher.group(4);

            int status = Integer.parseInt(matcher.group(5));

            String bytesText = matcher.group(6);
            long bytes = bytesText.equals("-")
                    ? 0
                    : Long.parseLong(bytesText);

            String userAgent = matcher.group(7);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd/MMM/yyyy:HH:mm:ss Z",
                            Locale.ENGLISH
                    );

            Instant timestamp =
                    ZonedDateTime.parse(timestampText, formatter)
                            .toInstant();

            LogRecord record = new LogRecord(
                    timestamp,
                    clientIp,
                    method,
                    path,
                    status,
                    bytes,
                    userAgent,
                    Map.of(),
                    line
            );

            output.add(record);

        } catch (Exception e) {
            invalidCount++;
        }
    }

    @Override
    public LogRecord emit() throws StageException {
        return output.poll();
    }

    public int getInvalidCount() {
        return invalidCount;
    }
}