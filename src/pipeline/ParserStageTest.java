package pipeline;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserStageTest {

    @Test
    void validLine() throws Exception {
        ParserStage parser = new ParserStage();

        parser.consume(new Record(
                "127.0.0.1 - - [06/Oct/2026:18:00:00 +0300] " +
                        "\"GET /index.html HTTP/1.1\" 200 123 \"Mozilla/5.0\""
        ));

        LogRecord result = parser.emit();

        assertNotNull(result);
        assertEquals("127.0.0.1", result.clientIp());
        assertEquals("GET", result.method());
        assertEquals("/index.html", result.path());
        assertEquals(200, result.status());
        assertEquals(123, result.bytes());
        assertEquals("Mozilla/5.0", result.userAgent());
    }

    @Test
    void missingField() throws Exception {
        ParserStage parser = new ParserStage();

        parser.consume(new Record(
                "127.0.0.1 - - [06/Oct/2026:18:00:00 +0300] " +
                        "\"GET /index.html HTTP/1.1\" 200"
        ));

        assertNull(parser.emit());
        assertEquals(1, parser.getInvalidCount());
    }

    @Test
    void badTimestamp() throws Exception {
        ParserStage parser = new ParserStage();

        parser.consume(new Record(
                "127.0.0.1 - - [BAD-TIME] " +
                        "\"GET /index.html HTTP/1.1\" 200 123 \"Mozilla/5.0\""
        ));

        assertNull(parser.emit());
        assertEquals(1, parser.getInvalidCount());
    }

    @Test
    void badStatusCode() throws Exception {
        ParserStage parser = new ParserStage();

        parser.consume(new Record(
                "127.0.0.1 - - [06/Oct/2026:18:00:00 +0300] " +
                        "\"GET /index.html HTTP/1.1\" ABC 123 \"Mozilla/5.0\""
        ));

        assertNull(parser.emit());
        assertEquals(1, parser.getInvalidCount());
    }

    @Test
    void emptyLine() throws Exception {
        ParserStage parser = new ParserStage();

        parser.consume(new Record(""));

        assertNull(parser.emit());
        assertEquals(1, parser.getInvalidCount());
    }

    @Test
    void extraSpaces() throws Exception {
        ParserStage parser = new ParserStage();

        parser.consume(new Record(
                "127.0.0.1  - - [06/Oct/2026:18:00:00 +0300] " +
                        "\"GET /index.html HTTP/1.1\" 200 123 \"Mozilla/5.0\""
        ));

        assertNull(parser.emit());
        assertEquals(1, parser.getInvalidCount());
    }

    @Test
    void userAgentWithSpaces() throws Exception {
        ParserStage parser = new ParserStage();

        parser.consume(new Record(
                "127.0.0.1 - - [06/Oct/2026:18:00:00 +0300] " +
                        "\"GET /index.html HTTP/1.1\" 200 123 " +
                        "\"Mozilla/5.0 (Windows NT 10.0; Win64; x64)\""
        ));

        LogRecord result = parser.emit();

        assertNotNull(result);
        assertEquals(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64)",
                result.userAgent()
        );
    }

    @Test
    void queryString() throws Exception {
        ParserStage parser = new ParserStage();

        parser.consume(new Record(
                "127.0.0.1 - - [06/Oct/2026:18:00:00 +0300] " +
                        "\"GET /search?q=java&page=2 HTTP/1.1\" 200 321 \"Chrome\""
        ));

        LogRecord result = parser.emit();

        assertNotNull(result);
        assertEquals("/search?q=java&page=2", result.path());
    }
}