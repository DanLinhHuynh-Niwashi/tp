package seedu.command;

import seedu.category.Category;
import seedu.category.CategoryList;

import java.util.List;

// Command class for adding a new category
public class DeleteCategoryCommand extends Command {
    public static final String COMMAND_WORD = "delete-category";
    // A guide or description of the command
    public static final String COMMAND_GUIDE = "delete-category CATEGORY_NAME: Delete a new category";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = { "" };

    private CategoryList categoryList;

    public DeleteCategoryCommand(CategoryList categoryList) {
        this.categoryList = categoryList;
    }

    public DeleteCategoryCommand    () {
        // Default constructor
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            return List.of(LACK_ARGUMENTS_ERROR_MESSAGE);
        }
        String categoryName = arguments.get("");
        Category temp = categoryList.deleteCategory(categoryName);

        if(temp == null){
            return List.of("Category not found.");
        }
        return List.of("Category deleted: " + categoryName);
    }
    @Override
    protected String[] getMandatoryKeywords() {
        return COMMAND_MANDATORY_KEYWORDS;
    }

    @Override
    protected String[] getExtraKeywords() {
        return new String[0];
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
