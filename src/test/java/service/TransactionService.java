package service;

import model.Transaction;
import model.User;
import repository.TransactionRepository;
import repository.UserRepository;
import util.IdGenerator;
import model.Goal;

import java.time.LocalDate;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final GoalService goalService;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, GoalService goalService, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.goalService = goalService;
        this.userRepository = userRepository;
    }

    public Transaction addTransaction(String userId, double amount, String category, String type, LocalDate date, String description) {
        Transaction transaction = new Transaction(
                IdGenerator.generateId(),
                userId,
                amount,
                category,
                type,
                date,
                description
        );
        transactionRepository.save(transaction);

        // Если это доход, обновляем прогресс целей
        if ("income".equals(type)) {
            List<Goal> goals = goalService.getUserGoals(userId);
            for (Goal goal : goals) {
                goalService.updateGoalProgress(goal.getId(), amount);
            }
        }

        return transaction;
    }

    public List<Transaction> getUserTransactions(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    public void deleteTransaction(String transactionId) {
        transactionRepository.delete(transactionId);
    }
}