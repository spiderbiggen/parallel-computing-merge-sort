package computing.parallel.sort.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

public class CSVParser {
    private CSVParser() {
        // only static methods
    }

    public static <T> List<T> parse(InputStream in, Function<CSVObject, T> converter) throws IOException {
        return parse(in, converter, ',');
    }

    public static <T> List<T> parse(InputStream in, Function<CSVObject, T> converter, char delimiter) throws IOException {
        List<T> result = new ArrayList<>();
        String[] headers = null;
        try (InputStreamReader reader = new InputStreamReader(in);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            if (bufferedReader.ready()) {
                var line = bufferedReader.readLine();
                if (line.isBlank()) return null;
                headers = line.split(String.valueOf(delimiter));
            }
            // How 🤔
            if (headers == null || headers.length == 0) return null;

            while (bufferedReader.ready()) {
                CSVObject map = parseLine(headers, bufferedReader, delimiter);
                result.add(converter.apply(map));
                map.clear();
            }
        }
        return result;
    }

    private static CSVObject parseLine(String[] headers, BufferedReader reader, char delimiter) throws IOException {
        int i = 0;
        boolean escaped = false, string = false;
        CSVObject object = new CSVObject();
        StringBuilder current = new StringBuilder();
        while (reader.ready() && i < headers.length) {
            int character = reader.read();
            if (escaped) {
                current.append((char) character);
                escaped = false;
                continue;
            }
            if (character == '\\') {
                escaped = true;
                continue;
            }
            if (string) {
                if (character == '"') {
                    string = false;
                    continue;
                }
                current.append((char) character);
                continue;
            }
            if (character == '"') {
                string = true;
                continue;
            }
            if (character == delimiter || character == '\n') {
                object.put(headers[i++], current.toString());
                current.setLength(0); // clear string builder;
                continue;
            }
            current.append((char) character);
        }
        if (current.length() != 0 && i < headers.length) {
            object.put(headers[i], current.toString());
        }
        return object;
    }

    public static class CSVObject extends HashMap<String, String> {

        public float getFloat(String key) {return getFloat(key, 0F);}

        public float getFloat(String key, float def) {
            try {
                return Float.parseFloat(this.get(key));
            } catch (NumberFormatException e) {
                return def;
            }
        }

        public int getInt(String key) {
            return getInt(key, 0);
        }

        public int getInt(String key, int def) {
            return parseInt(this.get(key), def);
        }

        public Duration getDuration(String key) {
            final var value = get(key);
            try {
                var duration = Duration.ZERO;
                var values = value.split(":");
                duration = duration.plusSeconds(parseInt(arrayValueAt(values, values.length - 1), 0));
                duration = duration.plusMinutes(parseInt(arrayValueAt(values, values.length - 2), 0));
                duration = duration.plusHours(parseInt(arrayValueAt(values, values.length - 3), 0));
                duration = duration.plusDays(parseInt(arrayValueAt(values, values.length - 4), 0));
                return duration;
            } catch (DateTimeParseException | NullPointerException e) {
                e.printStackTrace();
                return null;
            }
        }

        private int parseInt(String value, int def) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return def;
            }
        }

        private <T> T arrayValueAt(T[] arr, int index) {
            if (index >= arr.length || index < 0) return null;
            return arr[index];
        }
    }
}
