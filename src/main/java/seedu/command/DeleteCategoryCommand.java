package seedu.command;

import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.datastorage.Storage;
import seedu.exceptions.CategoryNotFoundException;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.main.UI;
import seedu.message.ErrorMessages;
import seedu.message.CommandResultMessages;
import seedu.transaction.Expense;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the "delete-category" command to delete an existing category.
 * <p>
 * This command allows users to delete a category from the existing list of categories.
 * </p>
 */
public class DeleteCategoryCommand extends Command {
    /** Command word for invoking this command. */
    public static final String COMMAND_WORD = "delete-category";
    /** Command usage guide for users. */
    public static final String COMMAND_GUIDE = "delete-category CATEGORY_NAME: Delete an existing category";
    /** Mandatory keywords required for the command to execute correctly. */
    public static final String[] COMMAND_MANDATORY_KEYWORDS = { "" };

    /** The list of categories from which the category will be deleted. */
    private CategoryList categoryList;
    /** The list of transactions to do follow-up clean-ups. */
    private TransactionList transactionList;
    /** UI for middle messages*/
    private UI ui = new UI();
    /**
     * Constructs a DeleteCategoryCommand with the specified CategoryList.
     *
     * @param categoryList The list of categories from which the category will be deleted.
     */
    public DeleteCategoryCommand(CategoryList categoryList, TransactionList transactionList) {
        this.categoryList = categoryList;
        this.transactionList = transactionList;
    }

    /**
     * Default constructor for DeleteCategoryCommand.
     */
    public DeleteCategoryCommand() {
        // Default constructor
    }

    /**
     * Set UI for testing
     */
    public void setUI(UI ui) {
        this.ui = ui;
    }

    /**
     * Executes the "delete-category" command to delete a category from the list.
     *
     * @return A list of strings containing either error messages or success messages.
     */
    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            List<String> messages = new ArrayList<>();
            messages.add(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
            messages.add(COMMAND_GUIDE);
            return messages;
        }
        String categoryName = arguments.get("");
        Category temp = new Category(categoryName);

        List<Transaction> transactionListByCategory = transactionList.getExpensesByCategory(temp);

        boolean isContinue = true;
        if (!transactionListByCategory.isEmpty()) {
            isContinue = updateExistingTransactions(transactionListByCategory, temp);
        }

        if (isContinue) {
            return deleteCategory(categoryName);
        } else {
            return List.of(CommandResultMessages.ACTION_CANCEL);
        }
    }

    /**
     * Performs the deletion
     *
     * @return A list of message
     */
    List<String> deleteCategory(String categoryName) {
        try {
            Category temp = categoryList.deleteCategory(categoryName);

            if (temp == null) {
                throw new CategoryNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
            }

            Storage.saveCategory(categoryList.getCategories());
            return List.of(CommandResultMessages.DELETE_CATEGORY_SUCCESS + temp.getName());
        } catch (CategoryNotFoundException e) {
            return List.of(CommandResultMessages.DELETE_CATEGORY_FAIL + e.getMessage());
        }
    }
    /**
     * Does follow-up confirmation to set category for remaining transactions
     *
     * @return false if user choose to cancel the action, true if user proceed
     */
    private boolean updateExistingTransactions
    (List<Transaction> transactionListByCategory, Category category) {
        ui.printMessage("These expenses need modification: ");
        for (Transaction transaction : transactionListByCategory) {
            ui.printMessage(transaction.toString());
        }
        ui.printMessage("Type 'no' to cancel, " +
                "'skip' to remove the category of these expenses," +
                "or enter a category name to re-categorize: ");
        Category temp = processUserInput(category);
        if (temp == null) {
            return false;
        } else {
            for (Transaction transaction : transactionListByCategory) {
                ((Expense) transaction).setCategory(temp);
            }
            Storage.saveTransaction(transactionList.getTransactions());
            return true;
        }
    }

    //@@author YukeeHong
    /**
     * Processes the user input to re-categorize, or skip, or cancel the command
     *
     * @return null if the user cancel the command, empty category if they skip,
     *      and a Category if they re-categorize
     */
    private Category processUserInput(Category deletedCategory){
        Category temp = null;
        while (true) {
            String response = ui.getUserInput().trim();
            if (response.isEmpty()) {
                continue;
            }

            // If the user enter a new category and proceed to 'yes'
            if (response.equalsIgnoreCase("yes") && temp!=null) {
                try {
                    categoryList.addCategory(temp);
                } catch (InvalidDescriptionFormatException e) {
                    ui.printMessage(CommandResultMessages.ADD_CATEGORY_FAIL + e.getMessage());
                    ui.printMessage(ErrorMessages.INVALID_DESCRIPTION_GUIDE);
                    ui.printMessage("Type 'no' to cancel, " +
                            "'skip' to remove the category of these expenses," +
                            "or enter a category name to re-categorize: ");
                    continue;
                } catch (InvalidCategoryNameException e) {
                    ui.printMessage(CommandResultMessages.ADD_CATEGORY_FAIL + e.getMessage());
                    ui.printMessage("Type 'no' to cancel, " +
                            "'skip' to remove the category of these expenses," +
                            "or enter a category name to re-categorize: ");
                    continue;
                }
                ui.printMessage("New category '" + temp.getName() + "' created.");
                return temp;
            } else if (response.equalsIgnoreCase("no")) {
                return null;
            } else if (response.equalsIgnoreCase("skip")) {
                return new Category("");
            } else {
                temp = categoryList.findCategory(response);
                if (temp == null) {
                    ui.printMessage("Category '" + response + "' does not exist. Current categories:");
                    for (Category category:categoryList.getCategories()) {
                        ui.printMessage(category.toString());
                    }
                    ui.printMessage("Type 'yes' to create a new category, or enter an existing category name. " +
                            "Type 'no' to cancel, skip' to remove the category of these expenses: ");
                    // Temporarily save the inserted category
                    temp = new Category(response);
                } else if (temp.equals(deletedCategory)){
                    ui.printMessage("You are inputting the same category of your deletion. Current category list: ");
                    for (Category category:categoryList.getCategories()) {
                        ui.printMessage(category.toString());
                    }
                    ui.printMessage("Type 'no' to cancel, " +
                            "'skip' to remove the category of these expenses," +
                            "or enter a category name to re-categorize: ");
                } else {
                    return temp;
                }
            }
        }
    }
    /**
     * Returns the mandatory keywords for this command.
     *
     * @return An array of mandatory keywords.
     */
    @Override
    protected String[] getMandatoryKeywords() {
        return COMMAND_MANDATORY_KEYWORDS;
    }

    /**
     * Returns any extra keywords supported by this command.
     *
     * @return An array of extra keywords.
     */
    @Override
    protected String[] getExtraKeywords() {
        return new String[0];
    }

    /**
     * Returns the word used to invoke this command.
     *
     * @return The command word.
     */
    @Override
    protected String getCommandWord() {
        return COMMAND_WORD;
    }

    /**
     * Returns a guide on how to use this command.
     *
     * @return The command usage guide.
     */
    @Override
    protected String getCommandGuide() {
        return COMMAND_GUIDE;
    }
}
