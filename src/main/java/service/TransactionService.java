package service;

import model.Transaction; // Импорт модели транзакции
import model.Goal; // Импорт модели цели
import model.User; // Импорт модели пользователя
import repository.TransactionRepository;
import repository.UserRepository;
import util.IdGenerator;

import java.time.LocalDate;
import java.util.List;

/**
 * Сервис для работы с транзакциями
 */
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final GoalService goalService;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, GoalService goalService, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.goalService = goalService;
        this.userRepository = userRepository;
    }

    /**
     * Добавить новую транзакцию
     */
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

        // Если это расход, проверяем бюджет
        if ("expense".equals(type)) {
            User user = userRepository.findById(userId);
            if (user != null && user.getMonthlyBudget() > 0) {
                List<Transaction> transactions = transactionRepository.findByUserId(userId);
                double totalExpense = transactions.stream()
                        .filter(t -> "expense".equals(t.getType()))
                        .mapToDouble(Transaction::getAmount)
                        .sum();

                if (totalExpense > user.getMonthlyBudget()) {
                    System.out.println("Уведомление: Вы превысили месячный бюджет!");
                }
            }
        }

        return transaction;
    }

    /**
     * Получить все транзакции пользователя
     */
    public List<Transaction> getUserTransactions(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    /**
     * Удалить транзакцию
     */
    public void deleteTransaction(String transactionId) {
        transactionRepository.delete(transactionId);
    }
}