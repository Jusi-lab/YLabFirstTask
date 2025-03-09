package service;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_NewUser_Success() {
        // Arrange
        String name = "Иван Иванов";
        String email = "ivan@example.com";
        String password = "password123";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        User user = userService.register(name, email, password);

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void register_ExistingEmail_ThrowsException() {
        // Arrange
        String name = "Иван Иванов";
        String email = "ivan@example.com";
        String password = "password123";
        when(userRepository.findByEmail(email)).thenReturn(new User("1", name, email, password));

        // Act & Assert
        assertThatThrownBy(() -> userService.register(name, email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Пользователь с таким email уже существует");
    }

    @Test
    void login_ValidCredentials_ReturnsUser() {
        // Arrange
        String email = "ivan@example.com";
        String password = "password123";
        User user = new User("1", "Иван Иванов", email, password);
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        User loggedInUser = userService.login(email, password);

        // Assert
        assertThat(loggedInUser).isNotNull();
        assertThat(loggedInUser.getEmail()).isEqualTo(email);
        assertThat(loggedInUser.getPassword()).isEqualTo(password);
    }

    @Test
    void login_InvalidCredentials_ReturnsNull() {
        // Arrange
        String email = "ivan@example.com";
        String password = "wrongPassword";
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        User loggedInUser = userService.login(email, password);

        // Assert
        assertThat(loggedInUser).isNull();
    }
}