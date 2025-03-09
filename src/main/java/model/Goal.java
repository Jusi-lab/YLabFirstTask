package model;

//Модель цели (накопления)

public class Goal {
    private String id;
    private String userId;
    private String name;
    private double targetAmount;
    private double currentAmount;

    public Goal(String id, String userId, String name, double targetAmount) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
    }

    // Геттеры и сеттеры
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getTargetAmount() { return targetAmount; }
    public void setTargetAmount(double targetAmount) { this.targetAmount = targetAmount; }
    public double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(double currentAmount) { this.currentAmount = currentAmount; }

    @Override
    public String toString() {
        return String.format(
                "Goal{id=%s, name='%s', targetAmount=%.2f, currentAmount=%.2f}",
                id, name, targetAmount, currentAmount
        );
    }
}