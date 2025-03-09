package service;

import model.User;
import repository.UserRepository;
import util.IdGenerator;

// Сервис для работы с пользователями

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Регистрация нового пользователя

    public User register(String name, String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }

        User user = new User(
                IdGenerator.generateId(),
                name,
                email,
                password
        );
        userRepository.save(user);
        return user;
    }

    //Аутентификация пользователя

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    // Обновление данных пользователя

    public void updateUser(User user) {
        userRepository.save(user);
    }
}