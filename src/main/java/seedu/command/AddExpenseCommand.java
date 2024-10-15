package seedu.command;

import seedu.category.Category;
import seedu.transaction.Expense;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddExpenseCommand extends AddTransactionCommand {
    public static final String COMMAND_WORD = "add-expense";
    public static final String COMMAND_GUIDE = "add-expense [DESCRIPTION] [a/ AMOUNT] [d/ DATE] [c/ hCATEGORY]";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {"a/"};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {"d/", "c/"};

    public static final String ERROR_MESSAGE = "Error creating Income!";

    public AddExpenseCommand(TransactionList transactions) {
        super(transactions);
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            return List.of(LACK_ARGUMENTS_ERROR_MESSAGE);
        }

        // Handle missing description
        String expenseName = arguments.get("");
        if (expenseName == null || expenseName.isEmpty()) {
            expenseName = "";
        }

        // Retrieve and parse amount
        String amountString = arguments.get(COMMAND_MANDATORY_KEYWORDS[0]);
        double amount;
        try {
            amount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            return List.of("Invalid amount");
        }

        // Handle missing date
        String dateString = arguments.get(COMMAND_EXTRA_KEYWORDS[0]);
        if (dateString == null || dateString.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            dateString = LocalDateTime.now().format(formatter);
        }

        // Handle category
        String categoryString = arguments.get(COMMAND_EXTRA_KEYWORDS[1]);
        Category category = null;
        if (categoryString != null && !categoryString.isEmpty()) {
            category = new Category(categoryString);
        }

        Transaction transaction;
        if (category != null) {
            try {
                transaction = createTransaction(amount, expenseName, dateString, category);
            } catch (Exception e) {
                return List.of(ERROR_MESSAGE);
            }
        } else {
            try {
                transaction = createTransaction(amount, expenseName, dateString);
            } catch (Exception e) {
                return List.of(ERROR_MESSAGE + ": " + e.getMessage());
            }
        }
        transactions.addTransaction(transaction);

        return List.of("Expense added successfully!");
    }

    @Override
    protected Transaction createTransaction(double amount, String description, String date) throws Exception {
        return new Expense(amount, description, date);
    }

    protected Transaction createTransaction(double amount, String description,
                                            String date, Category category) throws Exception {
        return new Expense(amount, description, date, category);
    }

    @Override
    protected String[] getMandatoryKeywords() {
        return COMMAND_MANDATORY_KEYWORDS;
    }

    @Override
    protected String[] getExtraKeywords() {
        return COMMAND_EXTRA_KEYWORDS;
    }

    @Override
    protected String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    protected String getCommandGuide() {
        return COMMAND_GUIDE;
    }
}
