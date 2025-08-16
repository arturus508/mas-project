package com.example.healthfitness.service;

import com.example.healthfitness.model.Task;
import com.example.healthfitness.model.User;
import com.example.healthfitness.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class WeeklyReviewService {

    private final TaskRepository tasks;

    public WeeklyReviewService(TaskRepository tasks) {
        this.tasks = tasks;
    }

    public WeekWindow currentWeekWindow() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.with(DayOfWeek.MONDAY);
        LocalDate end = start.plusDays(6);
        return new WeekWindow(start, end);
    }

    public TasksSummary tasksSummary(User user, LocalDate start, LocalDate end) {
        List<Task> weekly = tasks.findByUserAndDateBetweenOrderByDateAsc(user, start, end);
        long total = weekly.size();
        long doneCount = tasks.countCompletedInRange(user, start, end);
        int pct = total == 0 ? 0 : (int) Math.round((doneCount * 100.0) / total);
        return new TasksSummary((int) doneCount, (int) total, pct);
    }

    public HabitsSummary habitsPlaceholder() {
        return new HabitsSummary(new int[]{0,0,0,0,0,0,0}, 0);
    }

    public Quote quoteOfTheDay() {
        String[][] data = new String[][]{
                {"Discipline equals freedom.", "Jocko Willink"},
                {"You do not rise to the level of your goals. You fall to the level of your systems.", "James Clear"},
                {"What gets measured gets managed.", "Peter Drucker"},
                {"Action is the foundational key to all success.", "Pablo Picasso"},
                {"The only bad workout is the one that didn’t happen.", "Unknown"}
        };
        LocalDate today = LocalDate.now();
        int idx = Math.floorMod(today.hashCode(), data.length);
        return new Quote(data[idx][0], data[idx][1]);
    }

    public record WeekWindow(LocalDate start, LocalDate end) {}
    public record TasksSummary(int done, int total, int percent) {}
    public record HabitsSummary(int[] heat7, int streak7) {}
    public record Quote(String text, String author) {}
}
