package service;

import model.User;
import repository.UserRepository;
import util.IdGenerator;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }
        return user;
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User findById(String userId) {
        return userRepository.findById(userId);
    }
}