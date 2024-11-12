package seedu.budget;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.message.BudgetMessages;

import seedu.transaction.Expense;
import seedu.transaction.TransactionList;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BudgetTrackerTest {

    private BudgetTracker budgetTracker;
    private TransactionList transactionList;

    @BeforeEach
    public void setUp() {
        // Initialize a mock TransactionList (you can mock this or create simple transaction objects)
        transactionList = new TransactionList();
        budgetTracker = new BudgetTracker(transactionList);
    }


    @Test
    public void testCalculatePastProgress_budgetExceeded_errorMessage() {
        // Given: Budget for May 2024 is set to 500.00, but expenses exceed the budget.
        YearMonth month = YearMonth.of(2024, 5);
        Map<YearMonth, Double> budgets = new HashMap<>();
        budgets.put(YearMonth.of(2024, 5), 500.0);
        budgetTracker.setMonthlyBudgets(budgets);

        // Add transactions
        transactionList.addTransaction(new Expense(200.0, "", "2024-05-20"));
        transactionList.addTransaction(new Expense(200.0,   "", "2024-05-20"));
        transactionList.addTransaction(new Expense(200.0, "","2024-05-20"));

        // Total expenses = 200 + 200 + 150 = 550, which exceeds the budget of 500
        String result = budgetTracker.calculatePastProgress(month);

        // Assert that the result indicates the budget was exceeded
        assertEquals(String.format(BudgetMessages.BUDGET_EXCEEDED_PAST,
                600.0, 500.0, month), result);
    }

    @Test
    public void testCalculatePastProgress_budgetWithin_errorMessage() {
        // Given: Budget for May 2024 is set to 500.00, but expenses exceed the budget.
        YearMonth month = YearMonth.of(2024, 5);
        Map<YearMonth, Double> budgets = new HashMap<>();
        budgets.put(YearMonth.of(2024, 5), 500.0);
        budgetTracker.setMonthlyBudgets(budgets);

        // Add transactions
        transactionList.addTransaction(new Expense(200.0, "", "2024-05-20"));
        transactionList.addTransaction(new Expense(200.0,   "", "2024-05-20"));

        // Total expenses = 200 + 200 + 150 = 550, which exceeds the budget of 500
        String result = budgetTracker.calculatePastProgress(month);

        // Assert that the result indicates the budget was exceeded
        assertEquals(String.format(BudgetMessages.WELL_DONE_WITHIN_BUDGET,
                month, 400.0, 500.0), result);
    }
}
