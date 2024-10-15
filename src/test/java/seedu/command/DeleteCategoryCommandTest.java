package seedu.command;

import org.junit.jupiter.api.Test;
import seedu.category.Category;
import seedu.category.CategoryList;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DeleteCategoryCommandTest {

    @Test
    void execute() {
        // Arrange
        CategoryList categoryList = new CategoryList();
        categoryList.addCategory(new Category("Sports"));
        DeleteCategoryCommand deleteCategoryCommand = new DeleteCategoryCommand(categoryList);
        deleteCategoryCommand.setArguments(Map.of("", "Sports"));

        // Act
        List<String> result = deleteCategoryCommand.execute();
        List<Category> categories = categoryList.getCategories();

        // Assert
        assertEquals(5, categories.size(), "The category list should be empty after deletion.");
        assertEquals("Category deleted: Sports", result.get(0), "The result message should confirm " +
                "the category was deleted.");
    }

    @Test
    void executeCategoryNotFound() {
        // Arrange
        CategoryList categoryList = new CategoryList();
        DeleteCategoryCommand deleteCategoryCommand = new DeleteCategoryCommand(categoryList);
        deleteCategoryCommand.setArguments(Map.of("", "NonExistent"));

        // Act
        List<String> result = deleteCategoryCommand.execute();

        // Assert
        assertEquals("Category not found.", result.get(0), "The result message should indicate that " +
                "the category was not found.");
    }
}
