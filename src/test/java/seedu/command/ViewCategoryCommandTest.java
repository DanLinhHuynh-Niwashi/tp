package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDescriptionFormatException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ViewCategoryCommandTest {
    private ViewCategoryCommand viewCategoryCommand;
    private CategoryList categories;
    @BeforeEach
    public void setUp() {
        categories = new CategoryList();
        categories.initializeDefaultCategories();
        viewCategoryCommand = new ViewCategoryCommand(categories);
    }

    @Test
    void setCategoryList_newCategoryList_equalCategoryList()
            throws NoSuchFieldException, IllegalAccessException,
            InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Expected list
        CategoryList temp = new CategoryList();
        temp.addCategory(new Category("Category test"));

        // Set category
        viewCategoryCommand.setCategoryList(temp);


        Field categoriesField = ViewCategoryCommand.class.getDeclaredField("categoryList");
        categoriesField.setAccessible(true); // Make private field accessible
        CategoryList commandCategoryList = (CategoryList) categoriesField.get(viewCategoryCommand);

        // Verify the result
        assertEquals(temp, commandCategoryList);
        temp.deleteCategory("Category test");
    }

    @Test
    void execute_rightCommand_rightOutput() {
        // Expected result
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add("1. Food");
        expectedMessages.add("2. Entertainment");
        expectedMessages.add("3. Transport");
        expectedMessages.add("4. Utilities");
        expectedMessages.add("5. Others");

        // Actual message list
        List<String> actualMessages = viewCategoryCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, actualMessages);
    }
}
