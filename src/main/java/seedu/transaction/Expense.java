package seedu.transaction;

import seedu.category.Category;
import seedu.utils.DateTimeUtils;

public class Expense extends Transaction {
    private final Category category;

    public Expense(double amount, String description, String date, Category category) throws Exception {
        super(amount, description, date);
        this.category = category;
    }

    public Expense(double amount, String description, String date) throws Exception {
        super(amount, description, date);
        this.category = new Category("");
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String getTransactionType() {
        return "Expense";
    }

    @Override
    public String toString() {
        String categoryString = (category != null) ? ", category=" + category.getName() : "";
        return "Expense [amount=" + amount + ", description=" + description + ", date="
                + DateTimeUtils.getDateTimeString(date) + categoryString + "]";
    }
}
