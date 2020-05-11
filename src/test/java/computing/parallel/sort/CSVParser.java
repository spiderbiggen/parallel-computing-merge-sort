package computing.parallel.sort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CSVParser {
    private CSVParser() {
        // only static methods
    }

    public static <T> List<T> parse(File file, Function<CSVObject, T> converter) throws IOException {
        try (InputStream stream = new FileInputStream(file)) {
            return parse(stream, converter);
        }
    }

    public static <T> List<T> parse(InputStream in, Function<CSVObject, T> converter) throws IOException {
        List<T> result = new ArrayList<>();
        String[] headers = null;
        try (InputStreamReader reader = new InputStreamReader(in);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            if (bufferedReader.ready()) {
                var line = bufferedReader.readLine();
                if (line.isBlank()) return null;
                headers = line.split(";");
            }
            // How 🤔
            if (headers == null || headers.length == 0) return null;

            CSVObject map = new CSVObject();
            while (bufferedReader.ready()) {
                var line = bufferedReader.readLine();
                if (line.isBlank()) continue;
                String[] parts = line.split(";");
                if (parts.length < 1) continue;

                for (int i = 0; i < parts.length && i < headers.length; i++) {
                    map.put(headers[i], parts[i]);
                }
                result.add(converter.apply(map));
                map.clear();
            }
        }
        return result;
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
