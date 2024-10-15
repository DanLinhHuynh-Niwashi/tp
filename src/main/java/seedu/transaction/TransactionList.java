package seedu.transaction;

import seedu.category.Category;
import seedu.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TransactionList {
    private static Logger logger = Logger.getLogger("TransactionList");
    private final List<Transaction> transactions;

    public TransactionList() {
        transactions = new ArrayList<>();
        initializeDefaultTransactions();
    }

    public int size() {
        return transactions.size();
    }

    private void initializeDefaultTransactions() {
        // Optionally initialize with some default transactions if needed.
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        // Sort transactions by date after adding
        // Sort transactions using the custom compareDateTime method
        transactions.sort((t1, t2) -> {
            LocalDateTime dateTime1 = t1.getDate();
            LocalDateTime dateTime2 = t2.getDate();
            return DateTimeUtils.compareDateTime(dateTime1, dateTime2) ? -1 : 1;
        });
        logger.log(Level.INFO,"Transaction added: " + transaction);
    }

    public Transaction deleteTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            Transaction removed = transactions.remove(index);
            logger.log(Level.INFO,"Transaction removed: " + removed);
            return removed;
        } else {
            logger.log(Level.INFO, "Invalid transaction index!");
            return null;
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    // Method to get transactions between a start time and an end time
    public List<Transaction> getTransactionsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return transactions.stream()
                .filter(transaction -> (transaction.getDate().isEqual(startTime) ||
                        transaction.getDate().isAfter(startTime)) &&
                        (transaction.getDate().isEqual(endTime) || transaction.getDate().isBefore(endTime)))
                .collect(Collectors.toList());
    }

    // Method to get transactions of a specific category for expenses
    public List<Transaction> getExpensesByCategory(Category category) {
        return transactions.stream()
                .filter(transaction -> transaction instanceof Expense)
                .map(transaction -> (Expense) transaction)
                .filter(expense -> expense.getCategory().equals(category))
                .collect(Collectors.toList());
    }
}

