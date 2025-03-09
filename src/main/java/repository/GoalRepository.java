package repository;

import model.Goal;
import java.util.*;

//Хранилище целей в памяти
public class GoalRepository {
    private final Map<String, Goal> goals = new HashMap<>();

    public void save(Goal goal) {
        goals.put(goal.getId(), goal);
    }

    public Goal findById(String id) {
        return goals.get(id);
    }

    public List<Goal> findByUserId(String userId) {
        List<Goal> userGoals = new ArrayList<>();
        for (Goal goal : goals.values()) {
            if (goal.getUserId().equals(userId)) {
                userGoals.add(goal);
            }
        }
        return userGoals;
    }

    public void delete(String goalId) {
        goals.remove(goalId);
    }
}