package seedu.command;

import seedu.datastorage.Storage;
import seedu.message.ErrorMessages;
import seedu.message.CommandResultMessages;
import seedu.message.InfoMessages;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.util.ArrayList;
import java.util.List;

public class DeleteTransactionCommand extends Command {
    public static final String COMMAND_WORD = "delete-transaction";
    public static final String COMMAND_GUIDE = "delete-transaction i/ INDEX: Delete a transaction";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {"i/"};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {};

    protected TransactionList transactions;

    public DeleteTransactionCommand(TransactionList transactions) {
        this.transactions = transactions;
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            List<String> messages = new ArrayList<>();
            messages.add(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
            messages.add(COMMAND_GUIDE);
            return messages;
        }

        String indexString = arguments.get(COMMAND_MANDATORY_KEYWORDS[0]);
        int index;
        try {
            index = Integer.parseInt(indexString);
        } catch (NumberFormatException e) {
            return List.of(CommandResultMessages.DELETE_TRANSACTION_FAIL + ErrorMessages.INVALID_NUMBER_FORMAT);
        } catch (Exception e) {
            return List.of(ErrorMessages.UNEXPECTED_ERROR_MESSAGE + e.getMessage());
        }

        int transactionListSize = transactions.size();
        if (index < 1 || index > transactionListSize) {
            return List.of(CommandResultMessages.DELETE_TRANSACTION_FAIL +
                    ErrorMessages.INDEX_OUT_OF_BOUNDS + transactions.size());
        }

        Transaction temp = transactions.getTransactions().get(index-1);
        transactions.deleteTransaction(index-1);
        Storage.saveTransaction(transactions.getTransactions());

        List<String> messages = new ArrayList<>();
        messages.add(CommandResultMessages.DELETE_TRANSACTION_SUCCESS + index+". " +temp.toString());

        // Print current transaction list
        messages.add(InfoMessages.CURRENT_LIST);
        List<Transaction> transactionList = transactions.getTransactions();
        for (Transaction transaction: transactionList) {
            messages.add(transactionList.indexOf(transaction) + 1 +". "+transaction.toString());
        }
        return messages;
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
