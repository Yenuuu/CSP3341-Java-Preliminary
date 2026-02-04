import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TrackerService {

    private final List<Habit> habits;
    private int nextIdNumber;

    public TrackerService() {
        this.habits = new ArrayList<>();
        this.nextIdNumber = 1;
    }

    public Habit addHabit(String name, Frequency frequency) {
        String habitId = String.format("H%04d", nextIdNumber);
        nextIdNumber++;

        Habit habit = new Habit(habitId, name, frequency, LocalDate.now());
        habits.add(habit);
        return habit;
    }

    public List<Habit> listHabits() {
        return new ArrayList<>(habits);
    }

    public Habit findHabitById(String habitId) {
        for (Habit habit : habits) {
            if (habit.getHabitId().equalsIgnoreCase(habitId)) {
                return habit;
            }
        }
        return null;
    }
}
