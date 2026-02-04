import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * FileStorageService
 *
 * Saves and loads HabitTrackerPlus data using simple CSV style text files.
 * This gives strong evidence for:
 * IO, exceptions, data types, and program state persistence.
 */
public class FileStorageService {

    private final String habitsFilePath;
    private final String logsFilePath;

    public FileStorageService(String habitsFilePath, String logsFilePath) {
        this.habitsFilePath = habitsFilePath;
        this.logsFilePath = logsFilePath;
    }

    public void saveHabits(List<Habit> habits) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(habitsFilePath))) {
            for (Habit habit : habits) {
                String line = habit.getHabitId() + "," +
                        escape(habit.getName()) + "," +
                        habit.getFrequency() + "," +
                        habit.getCreatedAt();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public List<Habit> loadHabits() throws IOException {
        List<Habit> habits = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(habitsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 4);
                if (parts.length < 4) {
                    continue;
                }

                String habitId = parts[0];
                String name = unescape(parts[1]);
                Frequency frequency = Frequency.valueOf(parts[2]);
                LocalDate createdAt = LocalDate.parse(parts[3]);

                habits.add(new Habit(habitId, name, frequency, createdAt));
            }
        }

        return habits;
    }

    public void saveLogs(List<HabitLogEntry> logs) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logsFilePath))) {
            for (HabitLogEntry entry : logs) {
                String line = entry.getHabitId() + "," +
                        entry.getDate() + "," +
                        entry.isCompleted();
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public List<HabitLogEntry> loadLogs() throws IOException {
        List<HabitLogEntry> logs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(logsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", 3);
                if (parts.length < 3) {
                    continue;
                }

                String habitId = parts[0];
                LocalDate date = LocalDate.parse(parts[1]);
                boolean completed = Boolean.parseBoolean(parts[2]);

                logs.add(new HabitLogEntry(habitId, date, completed));
            }
        }

        return logs;
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\").replace(",", "\\,");
    }

    private String unescape(String text) {
        return text.replace("\\,", ",").replace("\\\\", "\\");
    }
}
