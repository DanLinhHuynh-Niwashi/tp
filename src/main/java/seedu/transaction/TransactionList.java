package seedu.transaction;

import seedu.category.Category;
import seedu.utils.DateTimeUtils;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TransactionList {
    private static final Logger logger = Logger.getLogger("TransactionList");
    private static ArrayList<Transaction> transactions;
    private final Map<String, List<Transaction>> invertedIndex;

    public TransactionList() {
        transactions = new ArrayList<>();
        invertedIndex = new HashMap<>();
    }

    public int size() {
        return transactions.size();
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        // Rebuild inverted index from loaded transactions
        for (Transaction transaction : transactions) {
            updateInvertedIndex(transaction, true);
        }
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        updateInvertedIndex(transaction, true);
        // Sort transactions by date after adding
        // Sort transactions using the custom compareDateTime method
        logger.log(Level.INFO,"Transaction added: " + transaction);

        transactions.sort((t1, t2) -> {
            LocalDateTime dateTime1 = t1.getDate();
            LocalDateTime dateTime2 = t2.getDate();
            return DateTimeUtils.compareDateTime(dateTime1, dateTime2) ? -1 : 1;
        });


    }

    public Transaction deleteTransaction(int index) {
        if (index >= 0 && index < transactions.size()) {
            Transaction transaction = transactions.get(index);
            updateInvertedIndex(transaction, false);
            Transaction removed = transactions.remove(index);

            logger.log(Level.INFO,"Transaction removed: " + removed);
            return removed;
        } else {
            logger.log(Level.INFO, "Invalid transaction index!");
            return null;
        }
    }

    public ArrayList<Transaction> getTransactions() {
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

    // Method to search for transactions in transactionList using one or multiple keywords.
    public List<Transaction> searchTransactionsByKeywords (List<String> keywords) {
        Map<Transaction,Integer> relevanceMap = new HashMap<>();
        for (String keyword : keywords) {
            List<Transaction> matchingTransaction = invertedIndex.get(keyword.toLowerCase());
            if (matchingTransaction != null) {
                for (Transaction transaction : matchingTransaction) {
                    relevanceMap.put(transaction, relevanceMap.getOrDefault(transaction, 0) + 1);
                }
            }
        }

        return relevanceMap.entrySet().stream()
                .sorted((e1,e2) -> e2.getValue().compareTo(e1.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    //get the spending of today
    public static double getTodaySpending() {
        LocalDate today = LocalDate.now();
        return transactions.stream()
                .filter(t -> t instanceof Expense && t.getDate().toLocalDate().isEqual(today))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }


    public boolean updateCategory(int index, Category newCategory) {
        if (index >= 0 && index < transactions.size()) {
            Transaction transaction = transactions.get(index);
            if (transaction instanceof Expense) {
                ((Expense) transaction).setCategory(newCategory);
                return true;
            }
        }
        return false;
    }




    // Update the invertedIndex upon addition or deletion of event
    private void updateInvertedIndex(Transaction transaction, boolean isAdding) {
        String[] words = transaction.getDescription().toLowerCase().split(" ");
        for (String word : words) {
            if(isAdding) {
                invertedIndex.computeIfAbsent(word.toLowerCase(), k -> new ArrayList<>()).add(transaction);
            } else {
                List<Transaction> transactionsForWord = invertedIndex.get(word.toLowerCase());
                if (transactionsForWord != null) {
                    transactionsForWord.remove(transaction);
                    if (transactionsForWord.isEmpty()) {
                        invertedIndex.remove(word.toLowerCase());

                    }
                }
            }
        }
    }
}

