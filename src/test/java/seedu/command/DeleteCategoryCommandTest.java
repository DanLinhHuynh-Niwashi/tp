package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.main.UI;
import seedu.message.CommandResultMessages;
import seedu.message.ErrorMessages;
import seedu.transaction.Expense;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;


class DeleteCategoryCommandTest {

    CategoryList categoryList;
    TransactionList transactionList;
    DeleteCategoryCommand deleteCategoryCommand;



    @BeforeEach
    public void setUp() {
        categoryList = new CategoryList();
        transactionList = new TransactionList();
        // Add sample data to test
        transactionList.addTransaction(new Expense(1000, "",
                "2024-10-10 2359", new Category("Default")));
        transactionList.addTransaction(new Expense(1000, "",
                "2024-10-10 2359", new Category("Default")));
        deleteCategoryCommand = new DeleteCategoryCommand(categoryList, transactionList);
    }
    @Test
    void execute_validArgumentWithNoTransaction_categoryDeleted()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        categoryList.addCategory(new Category("Sports"));
        deleteCategoryCommand.setArguments(Map.of("", "Sports"));

        // Act
        List<String> result = deleteCategoryCommand.execute();
        List<Category> categories = categoryList.getCategories();

        // Assert
        assertEquals(0, categories.size(),
                "The category list should be empty after deletion.");
        assertEquals("Category deleted: Sports", result.get(0),
                "The result message should confirm the category was deleted.");
    }

    @Test
    void execute_validArgumentWithTransaction_recategorized()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        categoryList.addCategory(new Category("Default"));
        categoryList.addCategory(new Category("NewCategory"));
        deleteCategoryCommand.setArguments(Map.of("", "default"));

        deleteCategoryCommand.setUI(new TestUI("NewCategory"));
        // Act
        deleteCategoryCommand.execute();
        List<Category> categories = categoryList.getCategories();
        // Expected
        List<String> expectedTransactions = new ArrayList<>();
        // Add sample data to test
        expectedTransactions.add(new Expense(1000, "",
                "2024-10-10 2359", new Category("NewCategory")).toString());
        expectedTransactions.add(new Expense(1000, "",
                "2024-10-10 2359", new Category("NewCategory")).toString());

        List<String> actualTransactions = new ArrayList<>();
        for (Transaction transaction:transactionList.getTransactions()) {
            actualTransactions.add(transaction.toString());
        }
        // Assert
        assertEquals(1, categories.size(),
                "The category list should have 1 category left after deletion.");
        assertEquals(expectedTransactions, actualTransactions,
                "The current transaction list should update its category.");
    }

    @Test
    void execute_validArgumentWithTransaction_unCategorize()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        categoryList.addCategory(new Category("Default"));
        deleteCategoryCommand.setArguments(Map.of("", "Default"));

        deleteCategoryCommand.setUI(new TestUI("skip"));
        // Act
        deleteCategoryCommand.execute();
        List<Category> categories = categoryList.getCategories();
        // Expected
        List<String> expectedTransactions = new ArrayList<>();
        // Add sample data to test
        expectedTransactions.add(new Expense(1000, "",
                "2024-10-10 2359", new Category("")).toString());
        expectedTransactions.add(new Expense(1000, "",
                "2024-10-10 2359", new Category("")).toString());

        // Assert
        List<String> actualTransactions = new ArrayList<>();
        for (Transaction transaction:transactionList.getTransactions()) {
            actualTransactions.add(transaction.toString());
        }
        // Assert
        assertEquals(0, categories.size(),
                "The category list should have 0 category left after deletion.");
        assertEquals(expectedTransactions, actualTransactions,
                "The current transaction list should update its category.");
    }

    @Test
    void execute_validArgumentWithTransaction_newCategory()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        categoryList.addCategory(new Category("Default"));
        deleteCategoryCommand.setArguments(Map.of("", "Default"));

        deleteCategoryCommand.setUI(new TestUI("NewCategory yes"));
        // Act
        deleteCategoryCommand.execute();
        List<Category> categories = categoryList.getCategories();
        // Expected
        List<String> expectedTransactions = new ArrayList<>();
        // Add sample data to test
        expectedTransactions.add(new Expense(1000, "",
                "2024-10-10 2359", new Category("NewCategory")).toString());
        expectedTransactions.add(new Expense(1000, "",
                "2024-10-10 2359", new Category("NewCategory")).toString());

        // Assert
        List<String> actualTransactions = new ArrayList<>();
        for (Transaction transaction:transactionList.getTransactions()) {
            actualTransactions.add(transaction.toString());
        }
        // Assert
        assertEquals(1, categories.size(),
                "The category list should have 1 category left after deletion.");
        assertEquals(expectedTransactions, actualTransactions,
                "The current transaction list should update its category.");
    }

    @Test
    void execute_validArgumentWithTransaction_cancel()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        categoryList.addCategory(new Category("Default"));
        deleteCategoryCommand.setArguments(Map.of("", "Default"));

        deleteCategoryCommand.setUI(new TestUI("no"));
        // Act
        List<String> result = deleteCategoryCommand.execute();
        List<Category> categories = categoryList.getCategories();

        // Assert
        assertEquals(1, categories.size(),
                "The category list should have 1 category left after deletion.");
        assertEquals(CommandResultMessages.ACTION_CANCEL, result.get(0),
                "The current action should be cancelled.");
    }

    @Test
    void execute_categoryNotFound_errorMessage() {
        // Arrange
        deleteCategoryCommand.setArguments(Map.of("", "NonExistent"));

        // Act
        List<String> result = deleteCategoryCommand.execute();

        // Assert
        assertEquals(CommandResultMessages.DELETE_CATEGORY_FAIL + ErrorMessages.CATEGORY_NOT_FOUND,
                result.get(0), "The result message should indicate that " +
                "the category was not found.");
    }

    @Test
    public void execute_commandExecutedLackArgument_expectedOutput() {
        // Prepare test arguments
        Map<String, String> arguments = new HashMap<>();

        // Set the arguments using the method
        deleteCategoryCommand.setArguments(arguments);

        // Actual message list
        List<String> result = deleteCategoryCommand.execute();

        // Assert that the actual messages match the expected messages
        assertEquals(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE, result.get(0));
    }
}

/* Mock TestUI class to read the input */
class TestUI extends UI {
    private final String[] userInput;
    private int count = 0;
    // Constructor initializes the scanner and the output builder
    public TestUI(String userInput) {
        this.userInput = userInput.split(" ");
    }

    // Simulate getting user input
    @Override
    public String getUserInput() {
        if(count == userInput.length) {
            return "";
        }

        return userInput[count++];  // Return the pre-set user input
    }
}
