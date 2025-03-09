package service;

import model.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.GoalRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GoalServiceTest {
    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private GoalService goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addGoal_NewGoal_Success() {
        // Arrange
        String userId = "1";
        String name = "Новый ноутбук";
        double targetAmount = 5000;

        // Используем doAnswer для мокирования метода save
        doAnswer(invocation -> {
            Goal savedGoal = invocation.getArgument(0);
            return null; // Поскольку метод save возвращает void, возвращаем null
        }).when(goalRepository).save(any(Goal.class));

        // Act
        Goal goal = goalService.addGoal(userId, name, targetAmount);

        // Assert
        assertThat(goal).isNotNull();
        assertThat(goal.getName()).isEqualTo(name);
        assertThat(goal.getTargetAmount()).isEqualTo(targetAmount);
        verify(goalRepository, times(1)).save(goal);
    }

    @Test
    void updateGoalProgress_ValidGoal_UpdatesCurrentAmount() {
        // Arrange
        String goalId = "1";
        double amount = 1000;
        Goal goal = new Goal(goalId, "1", "Новый ноутбук", 5000);
        when(goalRepository.findById(goalId)).thenReturn(goal);

        // Act
        goalService.updateGoalProgress(goalId, amount);

        // Assert
        assertThat(goal.getCurrentAmount()).isEqualTo(amount);
        verify(goalRepository, times(1)).save(goal);
    }
}