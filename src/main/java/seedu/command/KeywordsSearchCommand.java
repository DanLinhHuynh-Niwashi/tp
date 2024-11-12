package seedu.command;

import seedu.message.CommandResultMessages;
import seedu.message.ErrorMessages;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles the "search" command to find transactions based on provided keywords.
 * <p>
 * This command allows users to search for transactions by providing keywords, and
 * returns a list of transactions that match the provided keywords.
 * </p>
 */
public class KeywordsSearchCommand extends Command {
    /** Command word for invoking this command. */
    public static final String COMMAND_WORD = "search";
    /** Command usage guide for users. */
    public static final String COMMAND_GUIDE = "search k/ [keyword_1] [keyword_2] ... [keyword_n]:" +
            " Search transaction by keywords";
    /** Keywords required for the command to execute correctly. */
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {"k/"};
    /** Optional keywords that can be used with the command. */
    public static final String[] COMMAND_EXTRA_KEYWORDS = {};

    /** The transaction list to search in. */
    protected TransactionList transactions;

    /**
     * Constructs a KeywordsSearchCommand with the specified list of transactions.
     *
     * @param transactions The list of transactions to search.
     */
    public KeywordsSearchCommand(TransactionList transactions) {
        this.transactions = transactions;
    }

    /**
     * Executes the "search" command to find transactions based on provided keywords.
     *
     * @return A list of strings containing either error messages, usage guide, or matching transactions.
     */
    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            List<String> messages = new ArrayList<>();
            messages.add(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
            messages.add(COMMAND_GUIDE);
            return messages;
        }

        // Extract the value under "k/" which contains all keywords
        String keywordsString = arguments.get("k/");
        if (keywordsString == null || keywordsString.trim().isEmpty()) {
            return List.of(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
        }

        // Split the keywordsString into individual keywords
        List<String> keywords = Arrays.asList(keywordsString.trim().split("\\s+"));

        if (keywords.isEmpty()) {
            return List.of(ErrorMessages.LACK_ARGUMENTS_ERROR_MESSAGE);
        }

        // Search for transactions matching the keywords
        List<Transaction> result = transactions.searchTransactionsByKeywords(keywords);

        if (result.isEmpty()) {
            return List.of(CommandResultMessages.FIND_KEYWORD_EMPTY);
        }

        // Convert the matching transactions to a list of strings
        List<String> resultStrings = result.stream().map(Transaction::toString)
            .collect(Collectors.toList());

        return resultStrings;
    }

    /**
     * Returns the mandatory keywords for this command.
     *
     * @return An array of mandatory keywords.
     */
    @Override
    protected String[] getMandatoryKeywords() {
        return COMMAND_MANDATORY_KEYWORDS;
    }

    /**
     * Returns any extra keywords supported by this command.
     *
     * @return An array of extra keywords.
     */
    @Override
    protected String[] getExtraKeywords() {
        return COMMAND_EXTRA_KEYWORDS;
    }

    /**
     * Returns the word used to invoke this command.
     *
     * @return The command word.
     */
    @Override
    protected String getCommandWord() {
        return COMMAND_WORD;
    }

    /**
     * Returns a guide on how to use this command.
     *
     * @return The command usage guide.
     */
    @Override
    protected String getCommandGuide() {
        return COMMAND_GUIDE;
    }
}
