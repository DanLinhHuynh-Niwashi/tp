package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.budget.BudgetTracker;
import seedu.message.CommandResultMessages;
import seedu.transaction.TransactionList;
import seedu.message.ErrorMessages;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ViewBudgetCommandTest {

    private BudgetTracker budgetTracker;
    private ViewBudgetCommand viewBudgetCommand;

    @BeforeEach
    public void setUp() {
        TransactionList transactionList = new TransactionList();
        budgetTracker = new BudgetTracker(transactionList);
        viewBudgetCommand = new ViewBudgetCommand(budgetTracker);
    }

    @Test
    public void execute_viewSpecificMonthBudget_success() {
        // Set budget for November 2024 and check its progress
        budgetTracker.setBudget("2024-11", 1000.0);

        viewBudgetCommand.setArguments(Map.of("m/", "2024-11"));
        List<String> result = viewBudgetCommand.execute();

        assertEquals("Great job! You're on track. Spent so far: $0.00, " +
                "Projected spending for this month: $0.00 (within the budget of $1000.00).", result.get(0));
    }

    @Test
    public void execute_viewAllBudgets_success() {
        budgetTracker.setBudget("2024-11", 800.0);
        budgetTracker.setBudget("2024-12", 1000.0);
        budgetTracker.setBudget("2025-01", 1200.0);

        viewBudgetCommand.setArguments(Map.of()); // No specific month argument
        List<String> result = viewBudgetCommand.execute();

        String expectedOutput = """
                1. Budget for 2024-11: $800.00
                2. Budget for 2024-12: $1000.00
                3. Budget for 2025-01: $1200.00""";

        String actualOutput = String.join("\n", result);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void execute_viewSpecificMonthInvalidMonthFormat_failure() {
        // Invalid month format
        viewBudgetCommand.setArguments(Map.of("m/", "12-2024"));
        List<String> result = viewBudgetCommand.execute();

        assertEquals(CommandResultMessages.TRACK_PROGRESS_FAIL +
                ErrorMessages.MESSAGE_INVALID_YEAR_MONTH_FORMAT, result.get(0));
    }

    @Test
    public void execute_viewSpecificMonthNoBudget_failure() {
        // Valid month format but no budget set for this month
        viewBudgetCommand.setArguments(Map.of("m/", "2024-11"));
        List<String> result = viewBudgetCommand.execute();

        assertEquals(ErrorMessages.BUDGET_NOT_FOUND, result.get(0));
    }

    @Test
    public void execute_viewSpecificFutureMonth_failure() {
        // Future month that should not be allowed
        YearMonth futureMonth = YearMonth.now().plusYears(2);
        viewBudgetCommand.setArguments(Map.of("m/", futureMonth.toString()));
        List<String> result = viewBudgetCommand.execute();

        assertEquals("Progress can only be checked for current or past months.", result.get(0));
    }
}
