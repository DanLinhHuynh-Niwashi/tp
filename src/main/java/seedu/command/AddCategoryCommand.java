package seedu.command;

import seedu.category.Category;
import seedu.category.CategoryList;

import java.util.List;

// Command class for adding a new category
public class AddCategoryCommand extends Command {
    public static final String COMMAND_WORD = "add-category";
    // A guide or description of the command
    public static final String COMMAND_GUIDE = "add-category CATEGORY_NAME: Add a new category";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = { "" };

    private CategoryList categoryList;

    public AddCategoryCommand(CategoryList categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            return List.of(LACK_ARGUMENTS_ERROR_MESSAGE);
        }
        String categoryName = arguments.get("");
        Category category = new Category(categoryName);
        Category temp = categoryList.addCategory(category);

        if(temp == null){
            return List.of("Duplicated category.");
        }
        return List.of("Category added: " + categoryName);
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
