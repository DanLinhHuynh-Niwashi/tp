package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.message.CommandResultMessages;
import seedu.message.ErrorMessages;
import seedu.transaction.Expense;
import seedu.transaction.Income;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UpdateCategoryCommandTest {

    private TransactionList transactionList;
    private CategoryList categoryList;
    private UpdateCategoryCommand command;

    @BeforeEach
    void setUp()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        categoryList = new CategoryList();
        transactionList = new TransactionList();
        command = new UpdateCategoryCommand(transactionList, categoryList);
        categoryList.addCategory(new Category("Food"));
        categoryList.addCategory(new Category("Transport"));
        transactionList.addTransaction(
                new Expense(100, "lunch", "2024-01-01 1200", new Category("Food")));
    }

    @Test
    void execute_validUpdate_success() {
        // Arrange
        command.setArguments(Map.of(
                "i/", "1",
                "c/", "Transport"
        ));

        // Act
        List<String> result = command.execute();

        // Assert
        Transaction updatedTransaction = transactionList.getTransactions().get(0);
        assertTrue(updatedTransaction.toString().contains("Transport"));
        assertEquals(CommandResultMessages.UPDATE_TRANSACTION_SUCCESS + updatedTransaction.toString(),
                result.get(0));
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
    }

    @Test
    void execute_invalidIndexFormat_errorMessage() {
        // Arrange
        command.setArguments(Map.of(
                "i/", "abc",
                "c/", "Transport"
        ));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.UPDATE_TRANSACTION_FAIL
                + ErrorMessages.INVALID_NUMBER_FORMAT, result.get(0));
    }

    @Test
    void execute_wrongTransactionType_errorMessage() {
        // Arrange
        command.setArguments(Map.of(
                "i/", "2",
                "c/", "Transport"
        ));
        transactionList.addTransaction(
                new Income(100, "Salary", "2024-01-01 1200"));
        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.UPDATE_TRANSACTION_FAIL
                + ErrorMessages.NOT_AN_EXPENSE, result.get(0));
    }

    @Test
    void execute_categoryNotFound_errorMessage() {
        // Arrange
        command.setArguments(Map.of(
                "i/", "1",
                "c/", "NonExistentCategory"
        ));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.UPDATE_TRANSACTION_FAIL
                + ErrorMessages.CATEGORY_NOT_FOUND, result.get(0));
    }

    @Test
    void execute_indexOutOfBounds_errorMessage() {
        // Arrange
        command.setArguments(Map.of(
                "i/", "999",
                "c/", "Transport"
        ));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.UPDATE_TRANSACTION_FAIL
                + ErrorMessages.INDEX_OUT_OF_BOUNDS + "1", result.get(0));
    }

    @Test
    void execute_negativeIndex_errorMessage() {
        // Arrange
        command.setArguments(Map.of(
                "i/", "-1",
                "c/", "Transport"
        ));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.UPDATE_TRANSACTION_FAIL
                + ErrorMessages.INDEX_OUT_OF_BOUNDS + "1", result.get(0));
    }
}
