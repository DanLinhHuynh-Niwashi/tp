package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.category.Category;
import seedu.category.CategoryList;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.main.UI;
import seedu.transaction.Expense;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;
import seedu.message.CommandResultMessages;
import seedu.message.ErrorMessages;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AddExpenseCommandTest {

    private TransactionList transactionList;
    private AddExpenseCommand command;
    private CategoryList categoryList;
    private UI ui;

    @BeforeEach
    void setUp() {
        transactionList = new TransactionList();
        categoryList = new CategoryList();
        ui = new UI();
        command = new AddExpenseCommand(transactionList, ui, categoryList);
    }

    @Test
    void execute_addExpenseAllValidFields_success()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        categoryList.addCategory(new Category("Food"));
        command.setArguments(Map.of(
                "", "dinner",
                "a/", "1000",
                "d/", "2024-10-01 1800",
                "c/", "Food"
        ));

        Expense expectedExpense = new Expense(1000, "dinner", "2024-10-01 1800", new Category("Food"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, transactionList.size());
        assertEquals(expectedExpense.toString(), transactionList.getTransactions().get(0).toString());
    }

    @Test
    void execute_addExpenseInvalidAmount_notAdding() {
        // Arrange
        command.setArguments(Map.of(
                "", "dinner",
                "a/", "invalid",
                "d/", "2024-10-01 1800",
                "c/", "Food"
        ));
        List<Transaction> expectedList = new ArrayList<>();

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(expectedList, transactionList.getTransactions());
        assertEquals(CommandResultMessages.ADD_TRANSACTION_FAIL + "Invalid amount format: invalid", result.get(0));
    }

    @Test
    void execute_addExpenseInvalidDate_notAdding() {
        // Arrange
        command.setArguments(Map.of(
                "", "dinner",
                "a/", "1000",
                "d/", "2024",
                "c/", "Food"
        ));
        List<Transaction> expectedList = new ArrayList<>();

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(expectedList, transactionList.getTransactions());
        assertEquals(CommandResultMessages.ADD_TRANSACTION_FAIL + ErrorMessages.MESSAGE_INVALID_DATE_FORMAT,
                result.get(0));
    }

    @Test
    void execute_addExpenseNoDate_success()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        String currentDateTime = LocalDateTime.now().format(formatter);
        categoryList.addCategory(new Category("Food"));

        command.setArguments(Map.of(
                "", "dinner",
                "a/", "1000",
                "c/", "Food"
        ));

        Expense expectedExpense = new Expense(1000, "dinner", currentDateTime,
                new Category("Food"));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, transactionList.size());
        assertEquals(expectedExpense.toString(), transactionList.getTransactions().get(0).toString());
    }

    @Test
    void execute_addExpenseLackArguments_errorMessage() {
        // Arrange
        Map<String, String> arguments = new HashMap<>();
        command.setArguments(arguments);

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE, result.get(0));
        assertEquals(0, transactionList.getTransactions().size());
    }

    @Test
    void execute_addExpenseNegativeAmount_notAdding() {
        // Arrange
        command.setArguments(Map.of(
                "", "dinner",
                "a/", "-1000",
                "c/", "Food"
        ));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(0, transactionList.getTransactions().size());
        assertEquals(CommandResultMessages.ADD_TRANSACTION_FAIL
                + ErrorMessages.NEGATIVE_AMOUNT +": -1000", result.get(0));
    }

    @Test
    void execute_addExpenseNoDescription_success()
            throws InvalidCategoryNameException, InvalidDescriptionFormatException {
        // Arrange
        categoryList.addCategory(new Category("Food"));
        command.setArguments(Map.of(
                "a/", "1000",
                "c/", "Food"
        ));

        // Act
        List<String> result = command.execute();

        // Assert
        assertEquals(1, transactionList.getTransactions().size());
        Transaction addedTransaction = transactionList.getTransactions().get(0);
        assertEquals("", addedTransaction.getDescription());
        assertEquals(1000.0, addedTransaction.getAmount());
    }
}
