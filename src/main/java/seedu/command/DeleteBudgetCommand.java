package seedu.command;

import seedu.budget.BudgetTracker;
import seedu.datastorage.Storage;
import seedu.exceptions.InvalidDateFormatException;
import seedu.message.CommandResultMessages;
import seedu.message.ErrorMessages;
import seedu.utils.DateTimeUtils;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class DeleteBudgetCommand extends Command {
    public static final String COMMAND_WORD = "delete-budget";
    public static final String COMMAND_GUIDE = "delete-budget m/ MONTH : Delete the budget for a certain month.";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {"m/"};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {};

    private final BudgetTracker budgetTracker;

    public DeleteBudgetCommand(BudgetTracker budgetTracker) {
        this.budgetTracker = budgetTracker;
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            List<String> messages = new ArrayList<>();
            messages.add(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
            messages.add(COMMAND_GUIDE);
            return messages;
        }

        String monthStr = arguments.get(COMMAND_MANDATORY_KEYWORDS[0]);
        YearMonth month;
        try {
            month = DateTimeUtils.parseYearMonth(monthStr);
        } catch (InvalidDateFormatException e) {
            return List.of(CommandResultMessages.DELETE_BUDGET_FAIL + ErrorMessages.MESSAGE_INVALID_YEAR_MONTH_FORMAT);
        } catch (Exception e) {
            return List.of(ErrorMessages.UNEXPECTED_ERROR_MESSAGE + e.getMessage());
        }

        if (!budgetTracker.getMonthlyBudgets().containsKey(month)) {
            return List.of(CommandResultMessages.DELETE_BUDGET_FAIL + ErrorMessages.BUDGET_NOT_FOUND);
        }

        budgetTracker.getMonthlyBudgets().remove(month);
        Storage.saveBudgets(budgetTracker.getMonthlyBudgets());

        return List.of(CommandResultMessages.DELETE_BUDGET_SUCCESS + "Budget for " + month + " has been deleted.");
    }

    @Override
    protected String[] getMandatoryKeywords() {
        return COMMAND_MANDATORY_KEYWORDS;
    }

    @Override
    protected String[] getExtraKeywords() {
        return COMMAND_EXTRA_KEYWORDS;
    }

    @Override
    protected String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    protected String getCommandGuide() {
        return COMMAND_GUIDE;
    }

}
