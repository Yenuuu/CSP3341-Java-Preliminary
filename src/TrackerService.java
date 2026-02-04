import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TrackerService
 *
 * This class contains the core business logic of the HabitTrackerPlus application.
 * It is responsible for:
 *  - Managing habits
 *  - Recording habit completion logs
 *  - Calculating streaks
 *
 * This design follows the Service pattern, keeping logic separate
 * from the user interface (MainApplication).
 */
public class TrackerService {

    /**
     * List of all habits created by the user.
     * Uses ArrayList for dynamic resizing and ordered storage.
     */
    private final List<Habit> habits;

    /**
     * Stores habit completion logs.
     * Key   -> habitId
     * Value -> list of log entries for that habit
     *
     * This structure allows efficient lookup of logs per habit.
     */
    private final Map<String, List<HabitLogEntry>> habitLogs;

    /**
     * Counter used to generate unique habit IDs.
     */
    private int nextIdNumber;

    /**
     * Constructor initializes collections and ID counter.
     */
    public TrackerService() {
        this.habits = new ArrayList<>();
        this.habitLogs = new HashMap<>();
        this.nextIdNumber = 1;
    }

    /**
     * Creates and adds a new habit to the system.
     *
     * @param name       Name of the habit
     * @param frequency Frequency (DAILY or WEEKLY)
     * @return The newly created Habit object
     */
    public Habit addHabit(String name, Frequency frequency) {

        // Generate a unique habit ID (e.g., H0001)
        String habitId = String.format("H%04d", nextIdNumber);
        nextIdNumber++;

        // Create habit object
        Habit habit = new Habit(habitId, name, frequency, LocalDate.now());

        // Store habit
        habits.add(habit);

        return habit;
    }

    /**
     * Returns a copy of all habits.
     * Returning a copy protects internal data from modification.
     *
     * @return List of habits
     */
    public List<Habit> listHabits() {
        return new ArrayList<>(habits);
    }

    /**
     * Finds a habit by its ID.
     *
     * @param habitId Habit ID to search for
     * @return Habit if found, otherwise null
     */
    public Habit findHabitById(String habitId) {
        for (Habit habit : habits) {
            if (habit.getHabitId().equalsIgnoreCase(habitId)) {
                return habit;
            }
        }
        return null;
    }

    /**
     * Records completion of a habit for the current date.
     *
     * @param habitId ID of the habit being completed
     * @throws IllegalArgumentException if habit does not exist
     */
    public void logCompletion(String habitId) {

        Habit habit = findHabitById(habitId);

        // Defensive programming: ensure habit exists
        if (habit == null) {
            throw new IllegalArgumentException("Habit not found");
        }

        // Create log list if this habit has no previous logs
        habitLogs.putIfAbsent(habitId, new ArrayList<>());

        // Add completion log for today
        habitLogs.get(habitId)
                .add(new HabitLogEntry(habitId, LocalDate.now(), true));
    }

    /**
     * Calculates the current streak for a DAILY habit.
     * The streak counts consecutive completed days up to today.
     *
     * @param habitId Habit ID
     * @return Number of consecutive days completed
     */
    public int getCurrentStreak(String habitId) {

        List<HabitLogEntry> logs = habitLogs.get(habitId);

        // No logs means no streak
        if (logs == null || logs.isEmpty()) {
            return 0;
        }

        int streak = 0;
        LocalDate expectedDate = LocalDate.now();

        // Traverse logs backwards (most recent first)
        for (int i = logs.size() - 1; i >= 0; i--) {

            HabitLogEntry entry = logs.get(i);

            // If the log matches the expected date, streak continues
            if (entry.getDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                // Break when a day is missed
                break;
            }
        }

        return streak;
    }
}
