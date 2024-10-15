package seedu.command;

import seedu.transaction.Income;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AddIncomeCommand extends AddTransactionCommand {
    public static final String COMMAND_WORD = "add-income";
    public static final String COMMAND_GUIDE = "add-income [DESCRIPTION] [a/ AMOUNT] [d/ DATE]";
    public static final String[] COMMAND_MANDATORY_KEYWORDS = {"a/"};
    public static final String[] COMMAND_EXTRA_KEYWORDS = {"d/"};
    public static final String ERROR_MESSAGE = "Error creating Income!";

    public AddIncomeCommand(TransactionList transactions) {
        super(transactions);
    }

    @Override
    public List<String> execute() {
        if (!isArgumentsValid()) {
            return List.of(LACK_ARGUMENTS_ERROR_MESSAGE);
        }

        String incomeName = arguments.get("");
        if (incomeName == null || incomeName.isEmpty()) {
            incomeName = "";
        }

        String amountString = arguments.get(COMMAND_MANDATORY_KEYWORDS[0]);

        String dateString = arguments.get(COMMAND_EXTRA_KEYWORDS[0]);
        if (dateString == null || dateString.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            dateString = LocalDateTime.now().format(formatter);
        }
        double amount;
        try {
            amount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            return List.of("Invalid amount");
        }
        try {
            transactions.addTransaction(createTransaction(amount, incomeName, dateString));
            return List.of("Income added successfully!");
        } catch (Exception e) {
            return List.of(ERROR_MESSAGE + ": " + e.getMessage());
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
