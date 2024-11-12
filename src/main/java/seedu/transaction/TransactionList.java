package seedu.transaction;

import seedu.category.Category;
import seedu.exceptions.InvalidTransactionTypeException;
import seedu.message.ErrorMessages;
import seedu.utils.DateTimeUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Represents a list of transactions, including expenses and incomes.
 * Provides methods to add, delete, and search transactions, as well as update their categories.
 * The class also maintains an inverted index for efficient keyword searching.
 * Original Implementation is by @YukeeHong in commit SHA 392a1fbce1f760236c968b3edfca0f4b8d3b81e7
 */
public class TransactionList {
    private static final Logger logger = Logger.getLogger("TransactionList");
    private  ArrayList<Transaction> transactions;
    private final Map<String, List<Transaction>> invertedIndex;

    /**
     * Constructs an empty TransactionList.
     */
    public TransactionList() {
        transactions = new ArrayList<>();
        invertedIndex = new HashMap<>();
    }

    /**
     * Returns the number of transactions in the list.
     *
     * @return The size of the transactions list.
     */
    public int size() {
        return transactions.size();
    }

    /**
     * Sets the transactions list and rebuilds the inverted index.
     *
     * @param transactions The list of transactions to set.
     */
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        // Rebuild inverted index from loaded transactions
        for (Transaction transaction : transactions) {
            updateInvertedIndex(transaction, true);
        }
    }

    /**
     * Adds a transaction to the list and updates the inverted index.
     * Also sorts the transactions by their date.
     *
     * @param transaction The transaction to add.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        updateInvertedIndex(transaction, true);
        // Sort transactions by date after adding
        logger.log(Level.INFO, "Transaction added: " + transaction);

        transactions.sort((t1, t2) -> {
            LocalDateTime dateTime1 = t1.getDate();
            LocalDateTime dateTime2 = t2.getDate();
            return DateTimeUtils.compareDateTime(dateTime1, dateTime2) ? -1 : 1;
        });
    }

    /**
     * Deletes a transaction at the specified index and updates the inverted index.
     *
     * @param index The index of the transaction to delete.
     * @return The deleted transaction, or null if the index is invalid.
     */
    public Transaction deleteTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            Transaction transaction = transactions.get(index);
            updateInvertedIndex(transaction, false);
            Transaction removed = transactions.remove(index);

            logger.log(Level.INFO, "Transaction removed: " + removed);
            return removed;
        } else {
            logger.log(Level.INFO, "Invalid transaction index!");
            return null;
        }
    }

    /**
     * Returns the list of all transactions.
     *
     * @return The list of transactions.
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Returns a list of transactions that occurred between the specified start and end times.
     *
     * @param startTime The start time for filtering transactions.
     * @param endTime The end time for filtering transactions.
     * @return A list of transactions between the specified times.
     */
    public  List<Transaction> getTransactionsBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return transactions.stream()
                .filter(transaction -> (transaction.getDate().isEqual(startTime) ||
                        transaction.getDate().isAfter(startTime)) &&
                        (transaction.getDate().isEqual(endTime) || transaction.getDate().isBefore(endTime)))
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of expense transactions that belong to the specified category.
     *
     * @param category The category to filter expenses by.
     * @return A list of expenses in the specified category.
     */
    public List<Transaction> getExpensesByCategory(Category category) {
        return transactions.stream()
                .filter(transaction -> transaction instanceof Expense)
                .map(transaction -> (Expense) transaction)
                .filter(expense -> expense.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    /**
     * Searches for transactions in the list that match the given keywords.
     *
     * @param keywords A list of keywords to search for in transaction descriptions.
     * @return A list of transactions that match the keywords, sorted by relevance.
     */
    public List<Transaction> searchTransactionsByKeywords(List<String> keywords) {
        Map<Transaction, Integer> relevanceMap = new HashMap<>();
        // Loop through each keyword
        for (String keyword : keywords) {
            // Convert the keyword to lowercase for case-insensitive matching
            String lowerCaseKeyword = keyword.toLowerCase();

            // Iterate over the inverted index and check if the key contains the keyword
            for (Map.Entry<String, List<Transaction>> entry : invertedIndex.entrySet()) {
                String key = entry.getKey().toLowerCase();
                if (key.contains(lowerCaseKeyword)) {
                    // If the key contains the keyword, increment the relevance count for each transaction
                    List<Transaction> matchingTransactions = entry.getValue();
                    for (Transaction transaction : matchingTransactions) {
                        relevanceMap.put(transaction, relevanceMap.getOrDefault(transaction, 0) + 1);
                    }
                }
            }
        }

        return relevanceMap.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Returns the total spending of today by summing all expense transactions.
     *
     * @return The total amount spent today.
     */
    public double getTodaySpending() {
        LocalDate today = LocalDate.now();
        return transactions.stream()
                .filter(t -> t instanceof Expense && t.getDate().isAfter(today.atStartOfDay()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    public double getMonthSpending() {
        LocalDate startOfMonth = YearMonth.now().atDay(1);
        LocalDateTime startDate = startOfMonth.atStartOfDay();
        return transactions.stream()
                .filter(transaction -> transaction instanceof Expense && transaction.getDate().isAfter(startDate))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    public double getMonthIncome() {
        LocalDate startOfMonth = YearMonth.now().atDay(1);
        LocalDateTime startDate = startOfMonth.atStartOfDay();
        return transactions.stream()
                .filter(transaction -> transaction instanceof Income && transaction.getDate().isAfter(startDate))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
    /**
     * Updates the category of an expense transaction at the specified index.
     *
     * @param index The index of the transaction to update.
     * @param newCategory The new category to set.
     * @return The updated transaction.
     * @throws IndexOutOfBoundsException If the index is invalid.
     * @throws InvalidTransactionTypeException If the transaction is not an expense
     */
    public Transaction updateCategory(int index, Category newCategory)
            throws IndexOutOfBoundsException, InvalidTransactionTypeException {
        if (!(index >= 0 && index < transactions.size())) {
            throw new IndexOutOfBoundsException(ErrorMessages.INDEX_OUT_OF_BOUNDS + transactions.size());
        }

        Transaction transaction = transactions.get(index);
        if (!(transaction instanceof Expense)) {
            throw new InvalidTransactionTypeException(ErrorMessages.NOT_AN_EXPENSE);
        }

        ((Expense) transaction).setCategory(newCategory);
        return transaction;
    }

    /**
     * Updates the inverted index upon addition or deletion of a transaction.
     *
     * @param transaction The transaction to add or remove from the inverted index.
     * @param isAdding True if adding the transaction, false if removing it.
     */
    private void updateInvertedIndex(Transaction transaction, boolean isAdding) {
        String[] words = transaction.getDescription().toLowerCase().split(" ");
        for (String word : words) {
            if (isAdding) {
                invertedIndex.computeIfAbsent(word.toLowerCase(), k -> new ArrayList<>()).add(transaction);
            } else {
                List<Transaction> transactionsForWord = invertedIndex.get(word);
                transactionsForWord.remove(transaction);
                if (transactionsForWord.isEmpty()) {
                    invertedIndex.remove(word);

                }
            }
        }
    }
}
