import java.util.List;
import java.util.Scanner;

/**
 * MainApplication
 *
 * This class represents the presentation layer of the HabitTrackerPlus application.
 * It is responsible only for:
 *  - Displaying menus
 *  - Reading user input
 *  - Calling appropriate service methods
 *
 * All business logic is delegated to TrackerService and ReminderService.
 * This demonstrates good separation of concerns.
 */
public class MainApplication {

    public static void main(String[] args) {

        // Core service containing application logic
        TrackerService trackerService = new TrackerService();

        // File persistence service (stores data in CSV files)
        FileStorageService storage =
                new FileStorageService("habits.csv", "logs.csv");

        // Reminder service demonstrating concurrency
        ReminderService reminderService =
                new ReminderService(trackerService);

        // Attempt to load previously saved data at startup
        try {
            trackerService.loadData(
                    storage.loadHabits(),
                    storage.loadLogs()
            );
            System.out.println("Data loaded successfully.");
        } catch (Exception ex) {
            // First run or missing files
            System.out.println("No saved data found. Starting fresh.");
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        // Main program loop
        while (running) {

            // Display menu options
            System.out.println();
            System.out.println("HabitTrackerPlus");
            System.out.println("1 Add Habit");
            System.out.println("2 List Habits");
            System.out.println("3 Log Habit Completion (today)");
            System.out.println("4 View Current Streak");
            System.out.println("5 Exit");
            System.out.println("6 Start Reminders");
            System.out.println("7 Stop Reminders");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            // Add a new habit
            if (choice.equals("1")) {

                System.out.print("Habit name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Frequency (DAILY or WEEKLY): ");
                String freqInput = scanner.nextLine().trim().toUpperCase();

                Frequency frequency;
                try {
                    frequency = Frequency.valueOf(freqInput);
                } catch (IllegalArgumentException ex) {
                    // Handle invalid enum input
                    System.out.println("Invalid frequency. Defaulting to DAILY.");
                    frequency = Frequency.DAILY;
                }

                Habit habit = trackerService.addHabit(name, frequency);
                System.out.println("Added: " + habit);

            }
            // List all habits
            else if (choice.equals("2")) {

                List<Habit> habits = trackerService.listHabits();

                if (habits.isEmpty()) {
                    System.out.println("No habits yet.");
                } else {
                    System.out.println("Habits:");
                    for (Habit habit : habits) {
                        System.out.println(habit);
                    }
                }

            }
            // Log completion of a habit for today
            else if (choice.equals("3")) {

                System.out.print("Enter Habit ID (e.g., H0001): ");
                String habitId = scanner.nextLine().trim();

                try {
                    trackerService.logCompletion(habitId);
                    System.out.println("Logged completion for " + habitId + " (today).");
                } catch (IllegalArgumentException ex) {
                    // Demonstrates exception handling
                    System.out.println("Error: " + ex.getMessage());
                }

            }
            // View current streak for a habit
            else if (choice.equals("4")) {

                System.out.print("Enter Habit ID (e.g., H0001): ");
                String habitId = scanner.nextLine().trim();

                int streak = trackerService.getCurrentStreak(habitId);
                System.out.println("Current streak for " + habitId +
                        ": " + streak + " day(s)");

            }
            // Exit the program
            else if (choice.equals("5")) {

                running = false;
                System.out.println("Exiting application...");

            }
            // Start reminder service (concurrency)
            else if (choice.equals("6")) {

                reminderService.startReminders();
                System.out.println("Reminders started.");

            }
            // Stop reminder service
            else if (choice.equals("7")) {

                reminderService.stopReminders();
                System.out.println("Reminders stopped.");

            }
            // Handle invalid menu option
            else {
                System.out.println("Invalid option. Try again.");
            }
        }

        // Save data before exiting
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
