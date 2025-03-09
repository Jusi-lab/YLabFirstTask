package service;

import model.Goal;
import repository.GoalRepository;
import util.IdGenerator;
import java.util.List;

/**
 * Сервис для работы с целями
 */
public class GoalService {
    private final GoalRepository goalRepository;

    public GoalService(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    /**
     * Добавить новую цель
     */
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

    /**
     * Получить все цели пользователя
     */
    public List<Goal> getUserGoals(String userId) {
        return goalRepository.findByUserId(userId);
    }

    /**
     * Удалить цель
     */
    public void deleteGoal(String goalId) {
        goalRepository.delete(goalId);
    }

    /**
     * Обновить прогресс цели при добавлении дохода
     */
    public void updateGoalProgress(String goalId, double amount) {
        Goal goal = goalRepository.findById(goalId);
        if (goal != null) {
            goal.setCurrentAmount(goal.getCurrentAmount() + amount);
            goalRepository.save(goal);
        }
    }
}