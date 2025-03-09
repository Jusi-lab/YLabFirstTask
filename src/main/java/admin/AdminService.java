package admin;

import model.User;
import repository.UserRepository;

import java.util.List;

// Сервис для администратора

public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Получение списка всех пользователей

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Блокировка пользователя

    public void blockUser(String userId) {
        User user = userRepository.findById(userId);
        if (user != null) {
            user.setAdmin(false);
            userRepository.save(user);
            System.out.println("User blocked: " + user.getEmail());
        } else {
            System.out.println("User not found!");
        }
    }

    /**
     * Удаление пользователя
     */
    public void deleteUser(String userId) {
        userRepository.delete(userId);
        System.out.println("User deleted: " + userId);
    }
}