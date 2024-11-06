package seedu.command;

import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;
import seedu.utils.AmountUtils;
import seedu.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public abstract class AddTransactionCommand extends Command {

    protected static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    protected TransactionList transactions;

    public AddTransactionCommand(TransactionList transactions) {
        this.transactions = transactions;
    }

    protected String parseDescription(Map<String, String> arguments) {
        String description = arguments.get("");
        return (description == null || description.isEmpty()) ? "" : description;
    }

    protected Double parseAmount(String amountStr) throws Exception {
        return AmountUtils.parseAmount(amountStr);
    }

    protected String parseDate(String dateStr) throws Exception {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDateTime.now().format(DEFAULT_FORMATTER);
        }
        DateTimeUtils.parseDateTime(dateStr); // Validates the date
        return dateStr;
    }

    public abstract List<String> execute();

    protected abstract Transaction createTransaction(double amount, String description, String date) throws Exception;

    protected abstract String[] getMandatoryKeywords();
    protected abstract String[] getExtraKeywords();
    protected abstract String getCommandWord();
    protected abstract String getCommandGuide();
}
