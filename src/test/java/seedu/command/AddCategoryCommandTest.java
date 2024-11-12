package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.message.CommandResultMessages;
import seedu.message.ErrorMessages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddCategoryCommandTest {

    private CategoryList categoryList;
    private AddCategoryCommand command;

    @BeforeEach
    void setUp() {
        categoryList = new CategoryList();
        command = new AddCategoryCommand(categoryList);
    }

    @Test
    void execute_validCategory_success() {
        // Arrange
        command.setArguments(Map.of("", "Food"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, categoryList.getCategories().size());
        assertTrue(categoryList.getCategories().stream()
                .anyMatch(c -> c.getName().equals("Food")));
        assertEquals(CommandResultMessages.ADD_CATEGORY_SUCCESS + "Food",
                result.get(0));
    }

    @Test
    void execute_duplicateCategory_failure()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        categoryList.addCategory(new Category("Food"));
        command.setArguments(Map.of("", "Food"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, categoryList.getCategories().size());
        assertEquals(CommandResultMessages.ADD_CATEGORY_FAIL
                + ErrorMessages.DUPLICATED_CATEGORY, result.get(0));
    }

    @Test
    void execute_lackArguments_errorMessage() {
        // Arrange
        Map<String, String> arguments = new HashMap<>();
        command.setArguments(arguments);

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE, result.get(0));
        assertEquals(command.COMMAND_GUIDE, result.get(1));
        assertEquals(0, categoryList.getCategories().size());
    }

    @Test
    void execute_emptyName_success() {
        // Arrange
        command.setArguments(Map.of("", ""));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, categoryList.getCategories().size());
        assertTrue(categoryList.getCategories().stream()
                .anyMatch(c -> c.getName().equals("")));
        assertEquals(CommandResultMessages.ADD_CATEGORY_SUCCESS + "",
                result.get(0));
    }

    @Test
    void execute_whitespaceName_success() {
        // Arrange
        command.setArguments(Map.of("", "   "));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, categoryList.getCategories().size());
        assertTrue(categoryList.getCategories().stream()
                .anyMatch(c -> c.getName().equals("   ")));
        assertEquals(CommandResultMessages.ADD_CATEGORY_SUCCESS + "   ",
                result.get(0));
    }

    @Test
    void execute_multipleCategories_success() {
        // Arrange
        command.setArguments(Map.of("", "Food"));
        command.execute();
        command.setArguments(Map.of("", "Transport"));
        command.execute();
        command.setArguments(Map.of("", "Entertainment"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(3, categoryList.getCategories().size());
        assertTrue(categoryList.getCategories().stream()
                .anyMatch(c -> c.getName().equals("Entertainment")));
        assertEquals(CommandResultMessages.ADD_CATEGORY_SUCCESS + "Entertainment",
                result.get(0));
    }

    @Test
    void execute_caseInsensitive_failure() {
        // Arrange
        command.setArguments(Map.of("", "Food"));
        command.execute();
        command.setArguments(Map.of("", "food"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, categoryList.getCategories().size());
        assertTrue(categoryList.getCategories().stream()
                .anyMatch(c -> c.getName().equals("Food")));
        assertEquals(CommandResultMessages.ADD_CATEGORY_FAIL
                + ErrorMessages.DUPLICATED_CATEGORY, result.get(0));
    }

    @Test
    void execute_categoryWithSpaces_success() {
        // Arrange
        command.setArguments(Map.of("", "Fast Food"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, categoryList.getCategories().size());
        assertTrue(categoryList.getCategories().stream()
                .anyMatch(c -> c.getName().equals("Fast Food")));
        assertEquals(CommandResultMessages.ADD_CATEGORY_SUCCESS + "Fast Food",
                result.get(0));
    }
}
