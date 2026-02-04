import java.util.List;
import java.util.Scanner;

/**
 * MainApplication
 *
 * Console user interface for HabitTrackerPlus.
 * Loads saved data at startup and saves data on exit.
 */
public class MainApplication {

    public static void main(String[] args) {

        TrackerService trackerService = new TrackerService();
        FileStorageService storage = new FileStorageService("habits.csv", "logs.csv");

        // Load saved data if files exist
        try {
            trackerService.loadData(storage.loadHabits(), storage.loadLogs());
            System.out.println("Data loaded successfully.");
        } catch (Exception ex) {
            System.out.println("No saved data found. Starting fresh.");
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("HabitTrackerPlus");
            System.out.println("1 Add Habit");
            System.out.println("2 List Habits");
            System.out.println("3 Log Habit Completion (today)");
            System.out.println("4 View Current Streak");
            System.out.println("5 Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {

                System.out.print("Habit name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Frequency (DAILY or WEEKLY): ");
                String freqInput = scanner.nextLine().trim().toUpperCase();

                Frequency frequency;
                try {
                    frequency = Frequency.valueOf(freqInput);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Invalid frequency. Defaulting to DAILY.");
                    frequency = Frequency.DAILY;
                }

                Habit habit = trackerService.addHabit(name, frequency);
                System.out.println("Added: " + habit);

            } else if (choice.equals("2")) {

                List<Habit> habits = trackerService.listHabits();

                if (habits.isEmpty()) {
                    System.out.println("No habits yet.");
                } else {
                    System.out.println("Habits:");
                    for (Habit habit : habits) {
                        System.out.println(habit);
                    }
                }

            } else if (choice.equals("3")) {

                System.out.print("Enter Habit ID (e.g., H0001): ");
                String habitId = scanner.nextLine().trim();

                try {
                    trackerService.logCompletion(habitId);
                    System.out.println("Logged completion for " + habitId + " (today).");
                } catch (IllegalArgumentException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }

            } else if (choice.equals("4")) {

                System.out.print("Enter Habit ID (e.g., H0001): ");
                String habitId = scanner.nextLine().trim();

                int streak = trackerService.getCurrentStreak(habitId);
                System.out.println("Current streak for " + habitId + ": " + streak + " day(s)");

            } else if (choice.equals("5")) {

                running = false;
                System.out.println("Exiting...");

            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        // Save data on exit
        try {
            storage.saveHabits(trackerService.listHabits());
            storage.saveLogs(trackerService.getAllLogsFlattened());
            System.out.println("Data saved successfully.");
        } catch (Exception ex) {
            System.out.println("Failed to save data: " + ex.getMessage());
        }

        scanner.close();
        System.out.println("Goodbye.");
    }
}
