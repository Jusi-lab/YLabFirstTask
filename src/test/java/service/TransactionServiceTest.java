package service;

import model.Transaction;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.TransactionRepository;
import repository.UserRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private GoalService goalService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addTransaction_Income_UpdatesGoalProgress() {
        // Arrange
        String userId = "1";
        double amount = 1000;
        String category = "Зарплата";
        String type = "income";
        LocalDate date = LocalDate.now();
        String description = "Аванс";
        when(goalService.getUserGoals(userId)).thenReturn(Collections.emptyList());

        // Act
        Transaction transaction = transactionService.addTransaction(userId, amount, category, type, date, description);

        // Assert
        assertThat(transaction).isNotNull();
        assertThat(transaction.getAmount()).isEqualTo(amount);
        assertThat(transaction.getType()).isEqualTo(type);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void getUserTransactions_ReturnsTransactions() {
        // Arrange
        String userId = "1";
        Transaction transaction = new Transaction("1", userId, 1000, "Зарплата", "income", LocalDate.now(), "Аванс");
        when(transactionRepository.findByUserId(userId)).thenReturn(List.of(transaction));

        // Act
        List<Transaction> transactions = transactionService.getUserTransactions(userId);

        // Assert
        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0)).isEqualTo(transaction);
    }

    @Test
    void deleteTransaction_DeletesTransaction() {
        // Arrange
        String transactionId = "1";

        // Act
        transactionService.deleteTransaction(transactionId);

        // Assert
        verify(transactionRepository, times(1)).delete(transactionId);
    }
}