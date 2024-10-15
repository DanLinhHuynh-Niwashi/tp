package seedu.command;

import org.junit.jupiter.api.Test;
import seedu.category.Category;
import seedu.category.CategoryList;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddCategoryCommandTest {

    @Test
    void execute() {
        // Arrange
        CategoryList categoryList = new CategoryList();
        AddCategoryCommand addCategoryCommand = new AddCategoryCommand(categoryList);
        addCategoryCommand.setArguments(Map.of("", "Sports"));

        // Act
        List<String> result = addCategoryCommand.execute();
        List<Category> categories = categoryList.getCategories();

        // Assert
        assertEquals(6, categories.size(), "The category list should contain one category.");
        assertEquals("Sports", categories.get(5).getName(), "The category name should be 'Sports'.");
        assertEquals("Category added: Sports", result.get(0), "The result message should confirm the " +
                "category was added.");
    }
}
