package seedu.command;

import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.datastorage.Storage;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.message.ErrorMessages;
import seedu.message.CommandResultMessages;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the "add-category" command to add a new category.
 * <p>
 * This command allows users to add a new category to the existing list of categories.
 * </p>
 */
public class AddCategoryCommand extends Command {
    /** Command word for invoking this command. */
    public static final String COMMAND_WORD = "add-category";
    /** Command usage guide for users. */
    public static final String COMMAND_GUIDE = "add-category CATEGORY_NAME: Add a new category";
    /** Mandatory keywords required for the command to execute correctly. */
    public static final String[] COMMAND_MANDATORY_KEYWORDS = { "" };

    /** The list of categories to which the new category will be added. */
    private CategoryList categoryList;

    /**
     * Constructs an AddCategoryCommand with the specified CategoryList.
     *
     * @param categoryList The list of categories to which the new category will be added.
     */
    public AddCategoryCommand(CategoryList categoryList) {
        this.categoryList = categoryList;
    }

    /**
     * Executes the "add-category" command to add a new category to the list.
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

        Category category = new Category(categoryName);
        Category temp = null;
        try {
            temp = categoryList.addCategory(category);
        } catch (InvalidDescriptionFormatException e) {
            return List.of(CommandResultMessages.ADD_CATEGORY_FAIL + e.getMessage(),
                    ErrorMessages.INVALID_DESCRIPTION_GUIDE);
        } catch (InvalidCategoryNameException e) {
            return List.of(CommandResultMessages.ADD_CATEGORY_FAIL + e.getMessage());
        }

        if (temp == null) {
            return List.of(CommandResultMessages.ADD_CATEGORY_FAIL + ErrorMessages.DUPLICATED_CATEGORY);
        }

        Storage.saveCategory(categoryList.getCategories());
        return List.of(CommandResultMessages.ADD_CATEGORY_SUCCESS + categoryName);
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
