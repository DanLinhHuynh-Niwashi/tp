package seedu.transaction;

/**
 * Represents an income transaction.
 * Extends the abstract Transaction class and provides specific implementations for income-related transactions.
 */
public class Income extends Transaction {

    /**
     * Default constructor required for Gson deserialization.
     */
    public Income() {
        super();
    }

    /**
     * Constructs an Income object with the specified amount, description, and date.
     *
     * @param amount The amount of the income.
     * @param description The description of the income.
     * @param dateTimeString The date and time of the income as a string.
     */
    public Income(double amount, String description, String dateTimeString) {
        super(amount, description, dateTimeString);
    }

    /**
     * Returns the type of the transaction.
     *
     * @return A string representing the transaction type, which is "Income".
     */
    @Override
    public String getTransactionType() {
        return "Income";
    }

    /**
     * Returns a string representation of the income transaction.
     *
     * @return A string containing the amount, description, and date of the income.
     */
    @Override
    public String toString() {
        return "Income [amount=" + amount + ", description=" + description + ", date="
                + dateTimeString + "]";
    }
}
