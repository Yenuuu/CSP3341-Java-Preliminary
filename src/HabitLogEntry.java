import java.time.LocalDate;

public class HabitLogEntry {

    private final String habitId;
    private final LocalDate date;
    private final boolean completed;

    public HabitLogEntry(String habitId, LocalDate date, boolean completed) {
        this.habitId = habitId;
        this.date = date;
        this.completed = completed;
    }

    public String getHabitId() {
        return habitId;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isCompleted() {
        return completed;
    }
}
