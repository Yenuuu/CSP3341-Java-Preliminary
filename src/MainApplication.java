import java.util.List;
import java.util.Scanner;

public class MainApplication {

    public static void main(String[] args) {

        TrackerService trackerService = new TrackerService();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("HabitTrackerPlus");
            System.out.println("1 Add Habit");
            System.out.println("2 List Habits");
            System.out.println("3 Exit");
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
                running = false;
                System.out.println("Goodbye.");

            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}
