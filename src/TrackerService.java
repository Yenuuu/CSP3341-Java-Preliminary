import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TrackerService
 *
 * Core business logic of HabitTrackerPlus.
 * Responsible for:
 *  - Managing habits
 *  - Recording completion logs
 *  - Calculating streaks
 *  - Providing data export and import for persistence
 */
public class TrackerService {

    private final List<Habit> habits;

    private final Map<String, List<HabitLogEntry>> habitLogs;

    private int nextIdNumber;

    public TrackerService() {
        this.habits = new ArrayList<>();
        this.habitLogs = new HashMap<>();
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

    public void logCompletion(String habitId) {
        Habit habit = findHabitById(habitId);

        if (habit == null) {
            throw new IllegalArgumentException("Habit not found");
        }

        habitLogs.putIfAbsent(habitId, new ArrayList<>());
        habitLogs.get(habitId).add(new HabitLogEntry(habitId, LocalDate.now(), true));
    }

    public int getCurrentStreak(String habitId) {
        List<HabitLogEntry> logs = habitLogs.get(habitId);

        if (logs == null || logs.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        for (int i = logs.size() - 1; i >= 0; i--) {
            HabitLogEntry entry = logs.get(i);

            if (entry.getDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }

        return streak;
    }

    /**
     * Flattens all logs from the habitLogs map into a single list.
     * Used by FileStorageService so logs can be saved easily.
     */
    public List<HabitLogEntry> getAllLogsFlattened() {
        List<HabitLogEntry> all = new ArrayList<>();

        for (List<HabitLogEntry> list : habitLogs.values()) {
            all.addAll(list);
        }

        return all;
    }

    /**
     * Loads habits and logs into the service at startup.
     * Also recalculates nextIdNumber so new habits continue correctly.
     */
    public void loadData(List<Habit> loadedHabits, List<HabitLogEntry> loadedLogs) {

        // Replace current habits with loaded habits
        habits.clear();
        habits.addAll(loadedHabits);

        // Rebuild habitLogs map from loaded logs
        habitLogs.clear();
        for (HabitLogEntry entry : loadedLogs) {
            habitLogs.putIfAbsent(entry.getHabitId(), new ArrayList<>());
            habitLogs.get(entry.getHabitId()).add(entry);
        }

        // Recalculate nextIdNumber based on highest existing habit ID
        nextIdNumber = 1;
        for (Habit habit : habits) {
            String numeric = habit.getHabitId().substring(1); // remove 'H'
            int idNum = Integer.parseInt(numeric);

            if (idNum >= nextIdNumber) {
                nextIdNumber = idNum + 1;
            }
        }
    }
}
