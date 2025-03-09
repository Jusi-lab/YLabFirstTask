package repository;

import model.User;
import java.util.*;

//Хранилище пользователей в памяти

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, User> emailIndex = new HashMap<>();

    public void save(User user) {
        users.put(user.getId(), user);
        emailIndex.put(user.getEmail(), user);
    }

    public User findById(String id) {
        return users.get(id);
    }

    public User findByEmail(String email) {
        return emailIndex.get(email);
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public void delete(String userId) {
        User user = users.remove(userId);
        if (user != null) {
            emailIndex.remove(user.getEmail());
        }
    }
}