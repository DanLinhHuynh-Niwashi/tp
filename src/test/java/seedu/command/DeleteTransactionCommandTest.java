package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.category.Category;
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
import static org.junit.jupiter.api.Assertions.assertFalse;

class DeleteTransactionCommandTest {

    private TransactionList transactionList;
    private DeleteTransactionCommand command;
    private Transaction testExpense;
    private Transaction testIncome;

    @BeforeEach
    void setUp() {
        transactionList = new TransactionList();
        command = new DeleteTransactionCommand(transactionList);

        testExpense = new Expense(100, "lunch", "2024-01-01 1200", new Category("Food"));
        testIncome = new Income(2000, "salary", "2024-01-01 1400");

        transactionList.addTransaction(testExpense);
        transactionList.addTransaction(testIncome);
    }
    int getIndex (Transaction item) {
        return transactionList.getTransactions().indexOf(item)+1;
    }
    @Test
    void execute_validIndex_success() {
        // Arrange
        command.setArguments(Map.of("i/", "1"));

        // Act
        int index = getIndex(testExpense);
        List<String> result = command.execute();

        // Assert
        assertEquals(1, transactionList.size());
        assertEquals(CommandResultMessages.DELETE_TRANSACTION_SUCCESS + index + ". " + testExpense.toString(),
                result.get(0));
        assertFalse(transactionList.getTransactions().contains(testExpense));
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
        assertEquals(2, transactionList.size()); // 确保没有删除任何记录
    }

    @Test
    void execute_invalidIndexFormat_errorMessage() {
        // Arrange
        command.setArguments(Map.of("i/", "abc"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.DELETE_TRANSACTION_FAIL
                + ErrorMessages.INVALID_NUMBER_FORMAT, result.get(0));
        assertEquals(2, transactionList.size()); // 确保没有删除任何记录
    }

    @Test
    void execute_indexOutOfBounds_errorMessage() {
        // Arrange
        command.setArguments(Map.of("i/", "999"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.DELETE_TRANSACTION_FAIL
                + ErrorMessages.INDEX_OUT_OF_BOUNDS + transactionList.size(), result.get(0));
        assertEquals(2, transactionList.size()); // 确保没有删除任何记录
    }

    @Test
    void execute_indexLessThanOne_errorMessage() {
        // Arrange
        command.setArguments(Map.of("i/", "0"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.DELETE_TRANSACTION_FAIL
                + ErrorMessages.INDEX_OUT_OF_BOUNDS + transactionList.size(), result.get(0));
        assertEquals(2, transactionList.size()); // 确保没有删除任何记录
    }

    @Test
    void execute_negativeIndex_errorMessage() {
        // Arrange
        command.setArguments(Map.of("i/", "-1"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(CommandResultMessages.DELETE_TRANSACTION_FAIL
                + ErrorMessages.INDEX_OUT_OF_BOUNDS + transactionList.size(), result.get(0));
        assertEquals(2, transactionList.size()); // 确保没有删除任何记录
    }

    @Test
    void execute_deleteLastTransaction_success() {
        // Arrange
        command.setArguments(Map.of("i/", "2"));

        // Act
        int index = getIndex(testIncome);
        List<String> result = command.execute();

        // Assert
        assertEquals(1, transactionList.size());
        assertEquals(CommandResultMessages.DELETE_TRANSACTION_SUCCESS + index + ". " + testIncome.toString(),
                result.get(0));
        assertFalse(transactionList.getTransactions().contains(testIncome));
    }
}
