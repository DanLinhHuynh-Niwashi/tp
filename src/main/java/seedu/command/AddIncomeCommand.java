package seedu.command;

import seedu.datastorage.Storage;
import seedu.exceptions.FutureTransactionException;
import seedu.exceptions.InvalidAmountFormatException;
import seedu.exceptions.InvalidDateFormatException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.message.ErrorMessages;
import seedu.message.CommandResultMessages;
import seedu.message.InfoMessages;
import seedu.transaction.Income;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;
import java.util.ArrayList;
import java.util.List;

public class AddIncomeCommand extends AddTransactionCommand {
    public static final String COMMAND_WORD = "add-income";
    public static final String COMMAND_GUIDE = "add-income [DESCRIPTION] a/ AMOUNT [d/ DATE]:" +
            " Add an income";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {"a/"};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {"d/"};

    public AddIncomeCommand(TransactionList transactions) {
        super(transactions);
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            List<String> messages = new ArrayList<>();
            messages.add(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
            messages.add(COMMAND_GUIDE);
            return messages;
        }

        String incomeName = null;
        try {
            incomeName = parseDescription(arguments);
        } catch (InvalidDescriptionFormatException e) {
            return List.of(CommandResultMessages.ADD_TRANSACTION_FAIL + e.getMessage(),
                    ErrorMessages.INVALID_DESCRIPTION_GUIDE);
        }

        Double amount;
        try {
            amount = parseAmount(arguments.get(COMMAND_MANDATORY_KEYWORDS[0]));
        } catch (InvalidAmountFormatException e) {
            return List.of(CommandResultMessages.ADD_TRANSACTION_FAIL + e.getMessage());
        }

        String dateString;
        try {
            dateString = parseDate(arguments.get(COMMAND_EXTRA_KEYWORDS[0]));
        } catch (InvalidDateFormatException | FutureTransactionException e) {
            return List.of(CommandResultMessages.ADD_TRANSACTION_FAIL + e.getMessage());
        }

        try {
            Transaction temp = createTransaction(amount, incomeName, dateString);
            transactions.addTransaction(temp);

            Storage.saveTransaction(transactions.getTransactions());

            // Print current transaction list
            List<String> messages = new ArrayList<>();
            messages.add(CommandResultMessages.ADD_TRANSACTION_SUCCESS + temp.toString());
            messages.add(InfoMessages.CURRENT_LIST);
            List<Transaction> transactionList = transactions.getTransactions();
            for (Transaction transaction: transactionList) {
                messages.add(transactionList.indexOf(transaction) + 1 +". "+transaction.toString());
            }
            return messages;
        } catch (Exception e) {
            return List.of(CommandResultMessages.ADD_TRANSACTION_FAIL + e.getMessage());
        }

    }

    @Override
    protected Transaction createTransaction(double amount, String description, String date) throws Exception {
        return new Income(amount, description, date);
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
