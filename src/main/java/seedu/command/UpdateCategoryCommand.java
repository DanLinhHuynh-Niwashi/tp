package seedu.command;

import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.datastorage.Storage;
import seedu.exceptions.CategoryNotFoundException;
import seedu.exceptions.InvalidTransactionTypeException;
import seedu.message.ErrorMessages;
import seedu.message.CommandResultMessages;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.util.ArrayList;
import java.util.List;

public class UpdateCategoryCommand extends Command {
    public static final String COMMAND_WORD = "categorize";
    public static final String COMMAND_GUIDE = "categorize i/ INDEX c/ CATEGORY: " +
            "Categorize an expense at the given index";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {"i/", "c/"};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {};

    private final TransactionList transactions;
    private final CategoryList categories;

    public UpdateCategoryCommand(TransactionList transactions, CategoryList categories) {
        this.transactions = transactions;
        this.categories = categories;
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            List<String> messages = new ArrayList<>();
            messages.add(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
            messages.add(COMMAND_GUIDE);
            return messages;
        }

        String indexString = arguments.get(COMMAND_MANDATORY_KEYWORDS[0]);
        String categoryString = arguments.get(COMMAND_MANDATORY_KEYWORDS[1]);

        try {
            int transactionIndex = Integer.parseInt(indexString) - 1;
            Category newCategory = categories.findCategory(categoryString);

            if (newCategory == null) {
                throw new CategoryNotFoundException(ErrorMessages.CATEGORY_NOT_FOUND);
            }

            Transaction temp = transactions.updateCategory(transactionIndex, newCategory);
            Storage.saveTransaction(transactions.getTransactions());

            return List.of(CommandResultMessages.UPDATE_TRANSACTION_SUCCESS + temp.toString());
        } catch (NumberFormatException e) {
            return List.of(CommandResultMessages.UPDATE_TRANSACTION_FAIL + ErrorMessages.INVALID_NUMBER_FORMAT);
        } catch (CategoryNotFoundException | InvalidTransactionTypeException | IndexOutOfBoundsException e) {
            return List.of(CommandResultMessages.UPDATE_TRANSACTION_FAIL + e.getMessage());
        } catch (Exception e) {
            return List.of(ErrorMessages.UNEXPECTED_ERROR_MESSAGE + e.getMessage());
        }
    }

    /**
     * Gets the mandatory keywords for the command.
     *
     * @return An array of strings containing the mandatory keywords associated with this command.
     */
    @Override
    protected String[] getMandatoryKeywords() {
        return COMMAND_MANDATORY_KEYWORDS;
    }

    /**
     * Gets the extra keywords for the command.
     *
     * @return An array of strings containing the extra keywords associated with this command.
     */
    @Override
    protected String[] getExtraKeywords() {
        return COMMAND_EXTRA_KEYWORDS;
    }

    /**
     * Gets the word for the command.
     *
     * @return A string representing the command word.
     */
    @Override
    protected String getCommandWord() {
        return COMMAND_WORD;
    }

    /**
     * Gets the guide for the command.
     *
     * @return A string representing the command guide.
     */
    @Override
    protected String getCommandGuide() {
        return COMMAND_GUIDE;
    }
}
