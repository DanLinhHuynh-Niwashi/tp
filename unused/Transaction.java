//@@author YukeeHong-unused

//unused because need to be refractored to fit other classes

class Transaction {
    private double amount;
    private String description;
    private String date;
    private Category category;
    public Transaction(double amount, String description, String date, Category category) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.category = category;
    }
    public double getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public String getDate() {
        return date;
    }
    public Category getCategory() {
        return category;
    }
    @Override
    public String toString() {
        return "Transaction [amount=" + amount + ", description=" + description + ", date=" + date + ", category=" + category.getName() + "]";
    }
}