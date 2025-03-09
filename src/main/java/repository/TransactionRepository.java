package repository;

import model.Transaction;
import java.util.*;

//Хранилище транзакций в памяти

public class TransactionRepository {
    private final Map<String, Transaction> transactions = new HashMap<>();

    public void save(Transaction transaction) {
        transactions.put(transaction.getId(), transaction);
    }

    public Transaction findById(String id) {
        return transactions.get(id);
    }

    public List<Transaction> findByUserId(String userId) {
        List<Transaction> userTransactions = new ArrayList<>();
        for (Transaction transaction : transactions.values()) {
            if (transaction.getUserId().equals(userId)) {
                userTransactions.add(transaction);
            }
        }
        return userTransactions;
    }

    public void delete(String transactionId) {
        transactions.remove(transactionId);
    }
}