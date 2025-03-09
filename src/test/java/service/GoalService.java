package service;

import model.Goal;
import repository.GoalRepository;
import util.IdGenerator;
import java.util.List;

public class GoalService {
    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    public Goal addGoal(String userId, String name, double targetAmount) {
        Goal goal = new Goal(
                IdGenerator.generateId(),
                userId,
                name,
                targetAmount
        );
        goalRepository.save(goal);
        return goal;
    }

    public List<Goal> getUserGoals(String userId) {
        return goalRepository.findByUserId(userId);
    }

    public void updateGoalProgress(String goalId, double amount) {
        Goal goal = goalRepository.findById(goalId);
        if (goal != null) {
            goal.setCurrentAmount(goal.getCurrentAmount() + amount);
            goalRepository.save(goal);
        }
    }
}