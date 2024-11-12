package seedu.command;

import org.junit.jupiter.api.Test;
import seedu.budget.BudgetTracker;
import seedu.message.CommandResultMessages;
import seedu.message.ErrorMessages;
import seedu.transaction.TransactionList;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddBudgetCommandTest {

    @Test
    void execute_addBudgetAllValidFields_success() {
        TransactionList transactionList = new TransactionList();
        BudgetTracker budgetTracker = new BudgetTracker(transactionList);
        AddBudgetCommand command = new AddBudgetCommand(budgetTracker);

        // Get the current month to make sure the test always run
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the current year and month as yyyy-MM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String monthStr = currentDateTime.format(formatter);

        command.setArguments(Map.of("a/", "1000", "m/", monthStr));
        List<String> result = command.execute();

        assertEquals(1000.0, budgetTracker.getMonthlyBudget(YearMonth.now()), 0.001);
        assertEquals(CommandResultMessages.SET_BUDGET_SUCCESS + "1000.0", result.get(0));
    }

    @Test
    void execute_addBudgetMissingMandatoryFields_exceptionThrown() {
        TransactionList transactionList = new TransactionList();
        BudgetTracker budgetTracker = new BudgetTracker(transactionList);
        AddBudgetCommand command = new AddBudgetCommand(budgetTracker);

        // Missing mandatory field "a/" (amount)
        command.setArguments(Map.of("m/", "2024-12"));
        List<String> result = command.execute();

        assertEquals(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE, result.get(0));
    }

    @Test
    void execute_addBudgetPastMonth_failure() {
        TransactionList transactionList = new TransactionList();
        BudgetTracker budgetTracker = new BudgetTracker(transactionList);
        AddBudgetCommand command = new AddBudgetCommand(budgetTracker);

        // Setting budget for a past month, e.g., January 2023
        command.setArguments(Map.of("a/", "1000", "m/", "2023-01"));
        List<String> result = command.execute();

        assertEquals(CommandResultMessages.SET_BUDGET_FAIL + ErrorMessages.MESSAGE_PAST_MONTH_BUDGET,
                result.get(0));
    }

    @Test
    void execute_addBudgetInvalidAmountFormat_failure() {
        TransactionList transactionList = new TransactionList();
        BudgetTracker budgetTracker = new BudgetTracker(transactionList);
        AddBudgetCommand command = new AddBudgetCommand(budgetTracker);

        // Get the current month to make sure the test always run
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Format the current year and month as yyyy-MM
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String monthStr = currentDateTime.format(formatter);

        // Invalid amount format (non-numeric value)
        command.setArguments(Map.of("a/", "one thousand", "m/", monthStr));
        List<String> result = command.execute();

        assertEquals(CommandResultMessages.SET_BUDGET_FAIL +
                ErrorMessages.INVALID_AMOUNT_FORMAT + "one thousand", result.get(0));
    }

    @Test
    void execute_addBudgetInvalidYearMonthFormat_failure() {
        TransactionList transactionList = new TransactionList();
        BudgetTracker budgetTracker = new BudgetTracker(transactionList);
        AddBudgetCommand command = new AddBudgetCommand(budgetTracker);

        // Invalid YearMonth format
        command.setArguments(Map.of("a/", "1000", "m/", "12-2024"));
        List<String> result = command.execute();

        assertEquals(CommandResultMessages.SET_BUDGET_FAIL
                        + ErrorMessages.MESSAGE_INVALID_YEAR_MONTH_FORMAT, result.get(0));
    }

}
