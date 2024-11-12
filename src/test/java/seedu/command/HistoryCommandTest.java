package seedu.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seedu.category.Category;
import seedu.message.CommandResultMessages;
import seedu.message.ErrorMessages;
import seedu.transaction.Expense;
import seedu.transaction.Income;
import seedu.transaction.Transaction;
import seedu.transaction.TransactionList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;



class HistoryCommandTest {

    private HistoryCommand historyCommand;
    private TransactionList inputTransactionList;
    private Transaction item1;
    private Transaction item2;
    private Transaction item3;
    private Transaction item4;
    private Transaction item5;
    private Transaction item6;
    @BeforeEach
    public void setUp() {
        TransactionList transactionList = new TransactionList();

        historyCommand = new HistoryCommand(transactionList);

        inputTransactionList = new TransactionList();
        item1 = new Income(300, "", "2024-01-15");
        inputTransactionList.addTransaction(item1);
        item2 = new Income(300, "", "2024-02-15");
        inputTransactionList.addTransaction(item2);
        item3 = new Income(300, "", "2024-03-15");
        inputTransactionList.addTransaction(item3);
        item4 = new Expense(300, "", "2024-01-15", new Category("Abc"));
        inputTransactionList.addTransaction(item4);
        item5 = new Expense(300, "", "2024-08-15", new Category("Abc"));
        inputTransactionList.addTransaction(item5);
        item6 = new Expense(300, "", "2024-05-15", new Category("Abc"));
        inputTransactionList.addTransaction(item6);
    }
    int getIndex (Transaction item) {
        return inputTransactionList.getTransactions().indexOf(item)+1;
    }
    @Test
    void setTransactionList_newTransactionList_equalTransactionList()
            throws NoSuchFieldException, IllegalAccessException{

        // Set transactions
        historyCommand.setTransactionList(inputTransactionList);


        Field transactionsField = HistoryCommand.class.getDeclaredField("transactionList");
        transactionsField.setAccessible(true); // Make private field accessible
        TransactionList commandTransactionList = (TransactionList) transactionsField.get(historyCommand);

        // Verify the result
        assertEquals(inputTransactionList, commandTransactionList);
    }

    @Test
    void execute_withoutArguments_showAllTransaction() {
        // Set transactions
        historyCommand.setTransactionList(inputTransactionList);

        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(getIndex(item1) + ". "+item1.toString());
        expectedMessages.add(getIndex(item4) + ". "+item4.toString());
        expectedMessages.add(getIndex(item2) + ". "+item2.toString());
        expectedMessages.add(getIndex(item3) + ". "+item3.toString());
        expectedMessages.add(getIndex(item6) + ". "+item6.toString());
        expectedMessages.add(getIndex(item5) + ". "+item5.toString());
        // Execute the command
        List<String> messages = historyCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withStart_showTransactionWithValidStart() {
        // Set transactions
        historyCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("f/", "2024-02-15");

        historyCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(getIndex(item2) + ". "+item2.toString());
        expectedMessages.add(getIndex(item3) + ". "+item3.toString());
        expectedMessages.add(getIndex(item6) + ". "+item6.toString());
        expectedMessages.add(getIndex(item5) + ". "+item5.toString());

        // Execute the command
        List<String> messages = historyCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withEnd_showTransactionWithValidEnd() {
        // Set transactions
        historyCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("t/", "2024-02-15");

        historyCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(getIndex(item1) + ". "+item1.toString());
        expectedMessages.add(getIndex(item4) + ". "+item4.toString());
        expectedMessages.add(getIndex(item2) + ". "+item2.toString());

        // Execute the command
        List<String> messages = historyCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withStartEnd_showTransactionsWithValidPeriod() {
        // Set transactions
        historyCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("t/", "2024-02-15");
        arguments.put("f/", "2024-02-15");

        historyCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(getIndex(item2) + ". "+item2.toString());

        // Execute the command
        List<String> messages = historyCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withInvalidDate_showInvalidDateFormatMessage() {
        // Set transactions
        historyCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("t/", "Lmao");
        arguments.put("f/", "2024-02-15");

        historyCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(CommandResultMessages.VIEW_TRANSACTION_FAIL +
                ErrorMessages.MESSAGE_INVALID_DATE_FORMAT);

        // Execute the command
        List<String> messages = historyCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withFromAfterTo_showInvalidStartEndMessage() {
        // Set transactions
        historyCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("t/", "2024-01-15");
        arguments.put("f/", "2024-02-15");

        historyCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(CommandResultMessages.VIEW_TRANSACTION_FAIL +
                ErrorMessages.MESSAGE_INVALID_START_END);

        // Execute the command
        List<String> messages = historyCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withNotExistIncome_showNoTransactionMessage() {
        // Set transactions
        historyCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("f/", "2024-12-12");

        historyCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(CommandResultMessages.VIEW_TRANSACTION_EMPTY);

        // Execute the command
        List<String> messages = historyCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }
}
