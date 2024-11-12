package seedu.command;

import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.datastorage.Storage;
import seedu.exceptions.FutureTransactionException;
import seedu.exceptions.InvalidAmountFormatException;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDateFormatException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.main.UI;
import seedu.message.ErrorMessages;
import seedu.message.CommandResultMessages;
import seedu.message.InfoMessages;
import seedu.transaction.Expense;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddExpenseCommand extends AddTransactionCommand {
    public static final String COMMAND_WORD = "add-expense";
    public static final String COMMAND_GUIDE = "add-expense [DESCRIPTION] a/ AMOUNT [d/ DATE] [c/ CATEGORY]:" +
            " Add an expense";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {"a/"};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {"d/", "c/"};

    private final UI ui;
    private final CategoryList categoryList;

    public AddExpenseCommand(TransactionList transactions, UI ui, CategoryList categoryList) {
        super(transactions);
        this.ui = ui;
        this.categoryList = categoryList;
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            List<String> messages = new ArrayList<>();
            messages.add(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
            messages.add(COMMAND_GUIDE);
            return messages;
        }

        String expenseName = null;
        try {
            expenseName = parseDescription(arguments);
        } catch (InvalidDescriptionFormatException e) {
            return List.of(CommandResultMessages.ADD_TRANSACTION_FAIL + e.getMessage(),
                    ErrorMessages.INVALID_DESCRIPTION_GUIDE);
        }

        Double amount;
        try {
            amount = parseAmount(arguments.get(COMMAND_MANDATORY_KEYWORDS[0]));
        } catch (InvalidAmountFormatException e) {
            return List.of(CommandResultMessages.ADD_TRANSACTION_FAIL + e.getMessage());
        }

        String dateString;
        try {
            dateString = parseDate(arguments.get(COMMAND_EXTRA_KEYWORDS[0]));
        } catch (InvalidDateFormatException | FutureTransactionException e) {
            return List.of(CommandResultMessages.ADD_TRANSACTION_FAIL + e.getMessage());
        }

        Category category = handleCategoryInput(arguments.get(COMMAND_EXTRA_KEYWORDS[1]));
        Transaction temp = null;
        try {
            temp = (!Objects.equals(category.getName(), "")) ?
                    createTransaction(amount, expenseName, dateString, category) :
                    createTransaction(amount, expenseName, dateString);
        } catch (Exception e) {
            return List.of(CommandResultMessages.ADD_TRANSACTION_FAIL + e.getMessage());
        }
        transactions.addTransaction(temp);
        Storage.saveTransaction(transactions.getTransactions());
        List<String> messages = new ArrayList<>();
        messages.add(CommandResultMessages.ADD_TRANSACTION_SUCCESS + temp.toString());
        messages.add(InfoMessages.CURRENT_LIST);
        List<Transaction> transactionList = transactions.getTransactions();
        for (Transaction transaction: transactionList) {
            messages.add(transactionList.indexOf(transaction) + 1 +". "+transaction.toString());
        }
        return messages;
    }

    private Category handleCategoryInput(String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) {
            ui.printMessage("No category specified. Enter a category or type 'no' to skip. Current categories: ");
            for (Category category:categoryList.getCategories()) {
                ui.printMessage(category.toString());
            }
            return processUserInput("");
        }

        if (!categoryList.getCategories().contains(new Category(categoryName))){
            ui.printMessage("Category '" + categoryName + "' does not exist. Current categories:");
            for (Category category:categoryList.getCategories()) {
                ui.printMessage(category.toString());
            }
            ui.printMessage("Type 'yes' to create a new category, or enter an existing category name. " +
                    "Type 'no' to skip: ");
            return processUserInput(categoryName);
        } else {
            return new Category(categoryName);
        }
    }

    /**
     * Processes the user input to find valid category, or add a new one, or set as empty
     *
     * @return empty category if they type 'no',
     *      and a Category if they choose a valid category/ create a new category
     */
    private Category processUserInput(String categoryName){
        Category temp = null;
        if (!categoryName.isEmpty()) {
            temp = new Category(categoryName);
        }

        while (true) {
            String response = ui.getUserInput().trim();
            if (response.isEmpty()) {
                continue;
            }

            // If the user enter a new category and proceed to 'yes'
            if (response.equalsIgnoreCase("yes") && temp!=null) {
                try {
                    categoryList.addCategory(temp);
                    Storage.saveCategory(categoryList.getCategories());
                } catch (InvalidDescriptionFormatException e) {
                    ui.printMessage(CommandResultMessages.ADD_CATEGORY_FAIL + e.getMessage());
                    ui.printMessage(ErrorMessages.INVALID_DESCRIPTION_GUIDE);
                    ui.printMessage("Enter a category or type 'no' to skip. Current categories: ");
                    for (Category category:categoryList.getCategories()) {
                        ui.printMessage(category.toString());
                    }
                    continue;
                } catch (InvalidCategoryNameException e) {
                    ui.printMessage(CommandResultMessages.ADD_CATEGORY_FAIL + e.getMessage());
                    ui.printMessage("Enter a category or type 'no' to skip. Current categories: ");
                    for (Category category:categoryList.getCategories()) {
                        ui.printMessage(category.toString());
                    }
                    continue;
                }
                ui.printMessage("New category '" + temp.getName() + "' created.");
                return temp;
            } else if (response.equalsIgnoreCase("no")) {
                return new Category("");
            } else {
                temp = categoryList.findCategory(response);
                if (temp != null) {
                    return temp;
                } else {
                    ui.printMessage("Category '" + response + "' does not exist. Current categories:");
                    for (Category category:categoryList.getCategories()) {
                        ui.printMessage(category.toString());
                    }
                    ui.printMessage("Type 'yes' to create a new category, or enter an existing category name. " +
                            "Type 'no' to skip: ");
                    // Temporarily save the inserted category
                    temp = new Category(response);
                }
            }
        }
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
