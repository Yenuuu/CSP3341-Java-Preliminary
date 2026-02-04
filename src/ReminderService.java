import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ReminderService
 *
 * Uses a scheduled background thread to remind users
 * about habits that may not yet be completed today.
 *
 * Demonstrates Java concurrency using ScheduledExecutorService.
 */
public class ReminderService {

    private final ScheduledExecutorService scheduler;
    private final TrackerService trackerService;

    public ReminderService(TrackerService trackerService) {
        this.trackerService = trackerService;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Starts reminders at a fixed interval.
     */
    public void startReminders() {
        scheduler.scheduleAtFixedRate(() -> {
            System.out.println();
            System.out.println("Reminder: Keep tracking your habits today!");
            List<Habit> habits = trackerService.listHabits();
            System.out.println("You have " + habits.size() + " habit(s) to maintain.");
            System.out.print("> ");
        }, 10, 30, TimeUnit.SECONDS);
    }

    /**
     * Stops the reminder thread cleanly.
     */
    public void stopReminders() {
        scheduler.shutdown();
    }
}
