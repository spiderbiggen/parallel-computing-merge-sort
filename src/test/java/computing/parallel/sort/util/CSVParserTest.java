package computing.parallel.sort.util;

import computing.parallel.sort.Runner;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

public class CSVParserTest {

    @Test
    public void parseWithDelimiter() {
    }

    @Test
    public void parseWithDefaultDelimiter() throws IOException {
        String items = "firstName,lastName,overallTime,pace,racesCount\n" +
                "Peggy,Jewitt,03:28:46,07:58,6\n" +
                "\"Lynne\",Rule,05:12:43,11:56,1\n" +
                "Vicky,\"Olsen,F\",05:39:37,12:58,1\n" +
                "Evelyn,\\\\Deliz,05:30:54,12:38,81\n" +
                "Beverly,\\\"Hough,04:07:41,09:27,1";
        try(InputStream targetStream = new ByteArrayInputStream(items.getBytes())) {
            System.out.println(CSVParser.parse(targetStream, Runner::new));
        }
    }
}