package seedu.command;

import seedu.transaction.Transaction;
import seedu.transaction.Income;
import seedu.transaction.Expense;
import seedu.transaction.TransactionList;

import java.util.ArrayList;
import java.util.List;

public class ViewTotalCommand extends Command {
    public static final String COMMAND_WORD = "view-total";
    public static final String COMMAND_GUIDE = "view-total: View your total income - total expenses.";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {};

    private TransactionList transactionList;

    public ViewTotalCommand(TransactionList transactionList) {
        this.transactionList = transactionList;
    }


    @Override
    public List<String> execute() {
        List<Transaction> transactions = transactionList.getTransactions();
        double totalIncome = transactions.stream()
                .filter(t -> t instanceof Income)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double totalExpenses = transactions.stream()
                .filter(t -> t instanceof Expense)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double netTotal = totalIncome - totalExpenses;

        List<String> messages = new ArrayList<>();
        messages.add(String.format("Total Income: $%.2f", totalIncome));
        messages.add(String.format("Total Expenses: $%.2f", totalExpenses));
        messages.add(String.format("Net Total: $%.2f", netTotal));
        return messages;
    }

    /**
     * Gets the mandatory keywords for the command.
     *
     * @return An array of strings containing the mandatory keywords associated with this command.
     */
    @Override
    protected String[] getMandatoryKeywords() {
        return COMMAND_MANDATORY_KEYWORDS;
    }

    /**
     * Gets the extra keywords for the command.
     *
     * @return An array of strings containing the extra keywords associated with this command.
     */
    @Override
    protected String[] getExtraKeywords() {
        return COMMAND_EXTRA_KEYWORDS;
    }

    /**
     * Gets the word for the command.
     *
     * @return A string representing the command word.
     */
    @Override
    protected String getCommandWord() {
        return COMMAND_WORD;
    }

    /**
     * Gets the guide for the command.
     *
     * @return A string representing the command guide.
     */
    @Override
    protected String getCommandGuide() {
        return COMMAND_GUIDE;
    }
}
