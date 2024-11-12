package seedu.transaction;

import seedu.category.Category;


/**
 * Represents an expense transaction.
 * Extends the abstract Transaction class and provides specific implementations for expense-related transactions.
 */
public class Expense extends Transaction {
    private Category category;

    /**
     * Default constructor required for Gson deserialization.
     */
    public Expense() {
        super();
    }

    /**
     * Constructs an Expense object with the specified amount, description, date, and category.
     *
     * @param amount The amount of the expense.
     * @param description The description of the expense.
     * @param dateTimeString The date and time of the expense as a string.
     * @param category The category of the expense.
     */
    public Expense(double amount, String description, String dateTimeString, Category category) {
        super(amount, description, dateTimeString);
        this.category = category;
    }

    /**
     * Constructs an Expense object with the specified amount, description, and date.
     * Initializes the category as an empty string.
     *
     * @param amount The amount of the expense.
     * @param description The description of the expense.
     * @param dateTimeString The date and time of the expense as a string.
     */
    public Expense(double amount, String description, String dateTimeString) {
        super(amount, description, dateTimeString);
        this.category = new Category("");
    }

    /**
     * Returns the category of the expense.
     *
     * @return The category of the expense.
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets the category of the expense.
     *
     * @param category The new category of the expense.
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Returns the type of the transaction.
     *
     * @return A string representing the transaction type, which is "Expense".
     */
    @Override
    public String getTransactionType() {
        return "Expense";
    }

    /**
     * Returns a string representation of the expense transaction.
     *
     * @return A string containing the amount, description, date, and category of the expense.
     */
    @Override
    public String toString() {
        String categoryString = (category != null) ? ", category=" + category.getName() : "";
        return "Expense [amount=" + amount + ", description=" + description + ", date="
                + dateTimeString + categoryString + "]";
    }
}
