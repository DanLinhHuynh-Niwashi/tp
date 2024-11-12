package seedu.command;

import seedu.budget.BudgetTracker;
import seedu.datastorage.Storage;
import seedu.exceptions.InvalidAmountFormatException;
import seedu.message.ErrorMessages;
import seedu.message.CommandResultMessages;
import seedu.utils.AmountUtils;

import java.util.ArrayList;
import java.util.List;

public class AddBudgetCommand extends Command {
    public static final String COMMAND_WORD = "add-budget";
    public static final String COMMAND_GUIDE = "add-budget a/ AMOUNT m/ MONTH : Set a new month budget";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = { "a/", "m/" };
    public static final String[] COMMAND_EXTRA_KEYWORDS = {};

    private final BudgetTracker budgetTracker;

    public AddBudgetCommand(BudgetTracker budgetTracker) {
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

        String amountStr = arguments.get(COMMAND_MANDATORY_KEYWORDS[0]);
        Double amount = null;
        try {
            amount = AmountUtils.parseAmount(amountStr);
        } catch (InvalidAmountFormatException e) {
            return List.of(CommandResultMessages.SET_BUDGET_FAIL + e.getMessage());
        }

        String monthStr = arguments.get(COMMAND_MANDATORY_KEYWORDS[1]);
        try {
            budgetTracker.setBudget(monthStr, amount);
            Storage.saveBudgets(budgetTracker.getMonthlyBudgets());
        } catch (IllegalArgumentException e) {
            return List.of(CommandResultMessages.SET_BUDGET_FAIL + e.getMessage());
        } catch (Exception e) {
            return List.of(ErrorMessages.UNEXPECTED_ERROR_MESSAGE + e.getMessage());
        }

        return List.of(CommandResultMessages.SET_BUDGET_SUCCESS + amount);
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
