import java.time.LocalDate;

public class Habit {

    private final String habitId;
    private String name;
    private Frequency frequency;
    private final LocalDate createdAt;

    public Habit(String habitId, String name, Frequency frequency, LocalDate createdAt) {
        this.habitId = habitId;
        this.name = name;
        this.frequency = frequency;
        this.createdAt = createdAt;
    }

    public String getHabitId() {
        return habitId;
    }

    public String getName() {
        return name;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return habitId + " | " + name + " | " + frequency + " | created " + createdAt;
    }
}
