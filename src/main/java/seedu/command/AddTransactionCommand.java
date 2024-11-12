package seedu.command;

import seedu.exceptions.FutureTransactionException;
import seedu.exceptions.InvalidAmountFormatException;
import seedu.exceptions.InvalidDateFormatException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.message.ErrorMessages;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;
import seedu.utils.AmountUtils;
import seedu.utils.DateTimeUtils;
import seedu.utils.DescriptionUtils;

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

    protected String parseDescription(Map<String, String> arguments) throws InvalidDescriptionFormatException {
        String description = DescriptionUtils.parseDescription(arguments.get(""));
        return (description == null || description.isEmpty()) ? "" : description;
    }

    protected Double parseAmount(String amountStr) throws InvalidAmountFormatException {
        return AmountUtils.parseAmount(amountStr);
    }

    protected String parseDate(String dateStr)
            throws InvalidDateFormatException, FutureTransactionException {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDateTime.now().format(DEFAULT_FORMATTER);
        }
        String[] datetimeParts = dateStr.trim().split(" ", 2);

        // If only the date is provided, append time as "2359" (23:59 AM)
        if (datetimeParts.length == 1) {
            dateStr += " 0000";
        }

        LocalDateTime dateTime = DateTimeUtils.parseDateTime(dateStr); // Validates the date
        if(dateTime.isAfter(LocalDateTime.now())) {
            throw new FutureTransactionException(ErrorMessages.FUTURE_TRANSACTION);
        }
        return dateStr;
    }

    public abstract List<String> execute();

    protected abstract Transaction createTransaction(double amount, String description, String date) throws Exception;

    protected abstract String[] getMandatoryKeywords();
    protected abstract String[] getExtraKeywords();
    protected abstract String getCommandWord();
    protected abstract String getCommandGuide();
}
