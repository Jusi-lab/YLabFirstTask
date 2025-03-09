import in.ConsoleInput;
import out.ConsoleOutput;
import repository.GoalRepository;
import repository.TransactionRepository;
import repository.UserRepository;
import service.GoalService;
import service.NotificationService;
import service.TransactionService;
import service.UserService;
import admin.AdminService;

import model.User; // Импорт модели пользователя
import model.Transaction; // Импорт модели транзакции
import model.Goal; // Импорт модели цели

import java.time.LocalDate;
import java.util.List; // Импорт класса List

/**
 * Главный класс приложения
 */
public class Main {
    private static UserService userService;
    private static TransactionService transactionService;
    private static GoalService goalService;
    private static NotificationService notificationService;
    private static AdminService adminService;
    private static User currentUser;

    public static void main(String[] args) {
        initializeServices(); // Инициализация сервисов
        showMainMenu(); // Отображение главного меню
    }

    /**
     * Инициализация всех сервисов
     */
    private static void initializeServices() {
        UserRepository userRepository = new UserRepository();
        userService = new UserService(userRepository);

        TransactionRepository transactionRepository = new TransactionRepository();
        GoalRepository goalRepository = new GoalRepository();
        goalService = new GoalService(goalRepository);
        transactionService = new TransactionService(transactionRepository, goalService, userRepository); // Передаем все аргументы

        notificationService = new NotificationService(transactionRepository);
        adminService = new AdminService(userRepository);
    }

    /**
     * Отображение главного меню
     */
    private static void showMainMenu() {
        while (true) {
            ConsoleOutput.printMessage("\n=== Финансовый менеджер ===");
            ConsoleOutput.printMessage("1. Регистрация");
            ConsoleOutput.printMessage("2. Вход");
            ConsoleOutput.printMessage("3. Вход администратора");
            ConsoleOutput.printMessage("4. Создать администратора");
            ConsoleOutput.printMessage("0. Выход");

            String choice = ConsoleInput.readString("Выберите опцию: ");

            switch (choice) {
                case "1" -> registerUser(); // Регистрация пользователя
                case "2" -> loginUser(); // Вход пользователя
                case "3" -> adminLogin(); // Вход администратора
                case "4" -> createAdmin(); // Создание администратора
                case "0" -> System.exit(0); // Выход из приложения
                default -> ConsoleOutput.printMessage("Неверная опция!");
            }
        }
    }

    /**
     * Регистрация нового пользователя
     */
    private static void registerUser() {
        String name = ConsoleInput.readString("Введите имя: ");
        String email = ConsoleInput.readString("Введите email: ");
        String password = ConsoleInput.readString("Введите пароль: ");

        try {
            User user = userService.register(name, email, password);
            ConsoleOutput.printMessage("Регистрация успешна!");
        } catch (IllegalArgumentException e) {
            ConsoleOutput.printMessage("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Вход пользователя в систему
     */
    private static void loginUser() {
        String email = ConsoleInput.readString("Введите email: ");
        String password = ConsoleInput.readString("Введите пароль: ");

        User user = userService.login(email, password);
        if (user != null) {
            currentUser = user;
            ConsoleOutput.printMessage("Вход выполнен успешно! Добро пожаловать, " + user.getName());
            showUserMenu(); // Отображение меню пользователя
        } else {
            ConsoleOutput.printMessage("Неверный email или пароль!");
        }
    }

    /**
     * Вход администратора в систему
     */
    private static void adminLogin() {
        String email = ConsoleInput.readString("Введите email администратора: ");
        String password = ConsoleInput.readString("Введите пароль администратора: ");

        User admin = userService.login(email, password);
        if (admin != null && admin.isAdmin()) {
            currentUser = admin;
            ConsoleOutput.printMessage("Вход администратора выполнен успешно!");
            showAdminMenu(); // Отображение меню администратора
        } else {
            ConsoleOutput.printMessage("Неверные данные администратора!");
        }
    }

    /**
     * Создание администратора
     */
    private static void createAdmin() {
        String name = ConsoleInput.readString("Введите имя администратора: ");
        String email = ConsoleInput.readString("Введите email администратора: ");
        String password = ConsoleInput.readString("Введите пароль администратора: ");

        try {
            User admin = userService.register(name, email, password);
            admin.setAdmin(true); // Назначение прав администратора
            userService.updateUser(admin); // Обновление данных пользователя
            ConsoleOutput.printMessage("Администратор создан успешно!");
        } catch (IllegalArgumentException e) {
            ConsoleOutput.printMessage("Ошибка: " + e.getMessage());
        }
    }

    /**
     * Отображение меню пользователя
     */
    private static void showUserMenu() {
        while (true) {
            ConsoleOutput.printMessage("\n=== Меню пользователя ===");
            ConsoleOutput.printMessage("1. Добавить транзакцию");
            ConsoleOutput.printMessage("2. Просмотреть транзакции");
            ConsoleOutput.printMessage("3. Удалить транзакцию");
            ConsoleOutput.printMessage("4. Установить месячный бюджет");
            ConsoleOutput.printMessage("5. Добавить цель");
            ConsoleOutput.printMessage("6. Просмотреть цели");
            ConsoleOutput.printMessage("7. Просмотреть баланс и аналитику");
            ConsoleOutput.printMessage("0. Выйти");

            String choice = ConsoleInput.readString("Выберите опцию: ");

            switch (choice) {
                case "1" -> addTransaction(); // Добавление транзакции
                case "2" -> viewTransactions(); // Просмотр транзакций
                case "3" -> deleteTransaction(); // Удаление транзакции
                case "4" -> setMonthlyBudget(); // Установка месячного бюджета
                case "5" -> addGoal(); // Добавление цели
                case "6" -> viewGoals(); // Просмотр целей
                case "7" -> viewBalanceAndAnalytics(); // Просмотр баланса и аналитики
                case "0" -> {
                    currentUser = null; // Выход из аккаунта
                    return;
                }
                default -> ConsoleOutput.printMessage("Неверная опция!");
            }
        }
    }

    /**
     * Отображение меню администратора
     */
    private static void showAdminMenu() {
        while (true) {
            ConsoleOutput.printMessage("\n=== Меню администратора ===");
            ConsoleOutput.printMessage("1. Просмотреть всех пользователей");
            ConsoleOutput.printMessage("2. Заблокировать пользователя");
            ConsoleOutput.printMessage("3. Удалить пользователя");
            ConsoleOutput.printMessage("0. Выйти");

            String choice = ConsoleInput.readString("Выберите опцию: ");

            switch (choice) {
                case "1" -> viewAllUsers(); // Просмотр всех пользователей
                case "2" -> blockUser(); // Блокировка пользователя
                case "3" -> deleteUser(); // Удаление пользователя
                case "0" -> {
                    currentUser = null; // Выход из аккаунта
                    return;
                }
                default -> ConsoleOutput.printMessage("Неверная опция!");
            }
        }
    }

    /**
     * Добавление транзакции
     */
    private static void addTransaction() {
        double amount = ConsoleInput.readDouble("Введите сумму: ");
        String category = ConsoleInput.readString("Введите категорию: ");
        String type = ConsoleInput.readString("Введите тип (income/expense): ");
        LocalDate date = LocalDate.now(); // Текущая дата
        String description = ConsoleInput.readString("Введите описание: ");

        Transaction transaction = transactionService.addTransaction(
                currentUser.getId(),
                amount,
                category,
                type,
                date,
                description
        );
        ConsoleOutput.printMessage("Транзакция добавлена: " + transaction);
    }

    /**
     * Просмотр транзакций
     */
    private static void viewTransactions() {
        List<Transaction> transactions = transactionService.getUserTransactions(currentUser.getId());
        if (transactions.isEmpty()) {
            ConsoleOutput.printMessage("Транзакции не найдены.");
        } else {
            for (Transaction transaction : transactions) {
                ConsoleOutput.printMessage(transaction.toString());
            }
        }
    }

    /**
     * Удаление транзакции
     */
    private static void deleteTransaction() {
        String transactionId = ConsoleInput.readString("Введите ID транзакции для удаления: ");
        transactionService.deleteTransaction(transactionId);
        ConsoleOutput.printMessage("Транзакция удалена.");
    }

    /**
     * Установка месячного бюджета
     */
    private static void setMonthlyBudget() {
        double budget = ConsoleInput.readDouble("Введите месячный бюджет: ");
        currentUser.setMonthlyBudget(budget);
        ConsoleOutput.printMessage("Месячный бюджет установлен: " + budget);
    }

    /**
     * Добавление цели
     */
    private static void addGoal() {
        String name = ConsoleInput.readString("Введите название цели: ");
        double targetAmount = ConsoleInput.readDouble("Введите целевую сумму: ");

        Goal goal = goalService.addGoal(currentUser.getId(), name, targetAmount);
        ConsoleOutput.printMessage("Цель добавлена: " + goal);
    }

    /**
     * Просмотр целей
     */
    private static void viewGoals() {
        List<Goal> goals = goalService.getUserGoals(currentUser.getId());
        if (goals.isEmpty()) {
            ConsoleOutput.printMessage("Цели не найдены.");
        } else {
            for (Goal goal : goals) {
                ConsoleOutput.printMessage(goal.toString());
            }
        }
    }

    /**
     * Просмотр баланса и аналитики
     */
    /**
     * Просмотр баланса и аналитики
     */
    private static void viewBalanceAndAnalytics() {
        List<Transaction> transactions = transactionService.getUserTransactions(currentUser.getId());
        double totalIncome = 0;
        double totalExpense = 0;

        // Подсчет общего дохода и расхода
        for (Transaction transaction : transactions) {
            if ("income".equals(transaction.getType())) {
                totalIncome += transaction.getAmount();
            } else if ("expense".equals(transaction.getType())) {
                totalExpense += transaction.getAmount();
            }
        }

        // Расчет баланса
        double balance = totalIncome - totalExpense;

        // Форматирование чисел до двух знаков после запятой
        ConsoleOutput.printMessage(String.format("Текущий баланс: %.2f", balance));
        ConsoleOutput.printMessage(String.format("Общий доход: %.2f", totalIncome));
        ConsoleOutput.printMessage(String.format("Общий расход: %.2f", totalExpense));

        // Проверка и вывод использования бюджета
        if (currentUser.getMonthlyBudget() > 0) {
            double budgetUsage = (totalExpense / currentUser.getMonthlyBudget()) * 100;
            ConsoleOutput.printMessage(String.format("Использование бюджета: %.2f%%", budgetUsage));
        }
    }

    /**
     * Просмотр всех пользователей (администратор)
     */
    private static void viewAllUsers() {
        List<User> users = adminService.getAllUsers();
        if (users.isEmpty()) {
            ConsoleOutput.printMessage("Пользователи не найдены.");
        } else {
            for (User user : users) {
                ConsoleOutput.printMessage(user.toString());
            }
        }
    }

    /**
     * Блокировка пользователя (администратор)
     */
    private static void blockUser() {
        String userId = ConsoleInput.readString("Введите ID пользователя для блокировки: ");
        adminService.blockUser(userId);
    }

    /**
     * Удаление пользователя (администратор)
     */
    private static void deleteUser() {
        String userId = ConsoleInput.readString("Введите ID пользователя для удаления: ");
        adminService.deleteUser(userId);
    }
}