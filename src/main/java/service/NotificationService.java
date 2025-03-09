package service;

import model.Transaction;
import model.User;
import repository.TransactionRepository;

import java.util.List;

// Сервис для уведомлений

public class NotificationService {
    private final TransactionRepository transactionRepository;

    public NotificationService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Проверка превышения бюджета

    public void checkBudgetExceeded(User user) {
        List<Transaction> transactions = transactionRepository.findByUserId(user.getId());
        double totalExpense = 0;

        for (Transaction transaction : transactions) {
            if ("expense".equals(transaction.getType())) {
                totalExpense += transaction.getAmount();
            }
        }

        if (user.getMonthlyBudget() > 0 && totalExpense > user.getMonthlyBudget()) {
            System.out.println("Notification: You have exceeded your monthly budget!");
        }
    }
}