package computing.parallel.models;

import computing.parallel.sort.util.CSVParser;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

public class Runner implements Comparable<Runner>, Serializable {
    static final long serialVersionUID = 48L;

    private final String firstName;
    private final String lastName;
    private final int racesCount;
    private final Duration pace;
    private final Duration overallTime;

    public Runner(CSVParser.CSVObject map) {
        this(
                map.get("firstName"),
                map.get("lastName"),
                map.getInt("racesCount"),
                map.getDuration("pace"),
                map.getDuration("overallTime")
        );
    }

    public Runner(String firstName, String lastName, int racesCount, Duration pace, Duration overallTime) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.racesCount = racesCount;
        this.pace = pace;
        this.overallTime = overallTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Runner runner = (Runner) o;
        return racesCount == runner.racesCount &&
                firstName.equals(runner.firstName) &&
                lastName.equals(runner.lastName) &&
                Objects.equals(pace, runner.pace) &&
                overallTime.equals(runner.overallTime);
    }

    @Override
    public String toString() {
        return String.format(
                "%s %s: %d:%d:%d",
                firstName,
                lastName,
                overallTime.toHoursPart(),
                overallTime.toMinutesPart(),
                overallTime.toSecondsPart()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, racesCount, pace, overallTime);
    }

    @Override
    public int compareTo(Runner o) {
        return this.overallTime.compareTo(o.overallTime);
    }
}
