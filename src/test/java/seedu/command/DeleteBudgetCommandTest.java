package seedu.command;

import org.junit.jupiter.api.BeforeEach;
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

class DeleteBudgetCommandTest {

    private BudgetTracker budgetTracker;

    @BeforeEach
    void setUp() {
        TransactionList transactionList = new TransactionList();
        budgetTracker = new BudgetTracker(transactionList);
    }

    @Test
    void execute_deleteBudgetValidMonth_success() {
        YearMonth currentMonth = YearMonth.now();
        budgetTracker.setBudget(currentMonth.toString(), 1000.0);

        DeleteBudgetCommand command = new DeleteBudgetCommand(budgetTracker);
        command.setArguments(Map.of("m/", currentMonth.toString()));

        List<String> result = command.execute();

        assertEquals(CommandResultMessages.DELETE_BUDGET_SUCCESS
                + "Budget for " + currentMonth + " has been deleted.", result.get(0));
    }

    @Test
    void execute_deleteBudgetNonExistentMonth_failure() {
        // Attempt to delete a budget for a month without an existing budget
        DeleteBudgetCommand command = new DeleteBudgetCommand(budgetTracker);

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String monthStr = currentDateTime.format(formatter);

        command.setArguments(Map.of("m/", monthStr));
        List<String> result = command.execute();

        assertEquals(CommandResultMessages.DELETE_BUDGET_FAIL + ErrorMessages.BUDGET_NOT_FOUND, result.get(0));
    }

    @Test
    void execute_deleteBudgetInvalidMonthFormat_failure() {
        // Invalid month format
        DeleteBudgetCommand command = new DeleteBudgetCommand(budgetTracker);
        command.setArguments(Map.of("m/", "invalid-month"));

        List<String> result = command.execute();

        assertEquals(CommandResultMessages.DELETE_BUDGET_FAIL
                + ErrorMessages.MESSAGE_INVALID_YEAR_MONTH_FORMAT, result.get(0));
    }
}
