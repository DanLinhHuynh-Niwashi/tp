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


class ViewIncomeCommandTest {

    private ViewIncomeCommand viewIncomeCommand;
    private TransactionList inputTransactionList;
    private Transaction item1;
    private Transaction item2;
    private Transaction item3;
    private Transaction item4;

    @BeforeEach
    public void setUp() {
        TransactionList transactionList = new TransactionList();
        viewIncomeCommand = new ViewIncomeCommand(transactionList);

        inputTransactionList = new TransactionList();
        item1 = new Income(300, "", "2024-01-15");
        inputTransactionList.addTransaction(item1);
        item2 = new Income(300, "", "2024-02-15");
        inputTransactionList.addTransaction(item2);
        item3 = new Income(300, "", "2024-03-15");
        inputTransactionList.addTransaction(item3);
        item4 = new Expense(300, "", "2024-01-15", new Category("Abc"));
        inputTransactionList.addTransaction(item4);
    }

    int getIndex (Transaction item) {
        return inputTransactionList.getTransactions().indexOf(item)+1;
    }
    @Test
    void setTransactionList_newTransactionList_equalTransactionList()
            throws NoSuchFieldException, IllegalAccessException{

        // Set transactions
        viewIncomeCommand.setTransactionList(inputTransactionList);


        Field transactionsField = ViewIncomeCommand.class.getDeclaredField("transactionList");
        transactionsField.setAccessible(true); // Make private field accessible
        TransactionList commandTransactionList = (TransactionList) transactionsField.get(viewIncomeCommand);

        // Verify the result
        assertEquals(inputTransactionList, commandTransactionList);
    }

    @Test
    void execute_withFromAfterTo_showInvalidStartEndMessage() {
        // Set transactions
        viewIncomeCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("t/", "2024-01-15");
        arguments.put("f/", "2024-02-15");

        viewIncomeCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(CommandResultMessages.VIEW_TRANSACTION_FAIL +
                ErrorMessages.MESSAGE_INVALID_START_END);

        // Execute the command
        List<String> messages = viewIncomeCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withoutArguments_show3Incomes() {
        // Set transactions
        viewIncomeCommand.setTransactionList(inputTransactionList);

        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(getIndex(item1) + ". "+item1.toString());
        expectedMessages.add(getIndex(item2) + ". "+item2.toString());
        expectedMessages.add(getIndex(item3) + ". "+item3.toString());
        // Execute the command
        List<String> messages = viewIncomeCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withStart_show2IncomesWithValidStart() {
        // Set transactions
        viewIncomeCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("f/", "2024-02-15");

        viewIncomeCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(getIndex(item2) + ". "+item2.toString());
        expectedMessages.add(getIndex(item3) + ". "+item3.toString());

        // Execute the command
        List<String> messages = viewIncomeCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withEnd_show2IncomesWithValidEnd() {
        // Set transactions
        viewIncomeCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("t/", "2024-02-15");

        viewIncomeCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(getIndex(item1) + ". "+item1.toString());
        expectedMessages.add(getIndex(item2) + ". "+item2.toString());

        // Execute the command
        List<String> messages = viewIncomeCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withStartEnd_show1IncomeWithValidPeriod() {
        // Set transactions
        viewIncomeCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("t/", "2024-02-15");
        arguments.put("f/", "2024-02-15");

        viewIncomeCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(getIndex(item2) + ". "+item2.toString());

        // Execute the command
        List<String> messages = viewIncomeCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withInvalidDate_showInvalidDateFormatMessage() {
        // Set transactions
        viewIncomeCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("t/", "Lmao");
        arguments.put("f/", "2024-02-15");

        viewIncomeCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(CommandResultMessages.VIEW_TRANSACTION_FAIL +
                ErrorMessages.MESSAGE_INVALID_DATE_FORMAT);

        // Execute the command
        List<String> messages = viewIncomeCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }

    @Test
    void execute_withNotExistIncome_showNoIncomeMessage() {
        // Set transactions
        viewIncomeCommand.setTransactionList(inputTransactionList);

        Map<String, String> arguments = new HashMap<>();
        arguments.put("f/", "2024-12-12");

        viewIncomeCommand.setArguments(arguments);
        // Expected messages
        List<String> expectedMessages = new ArrayList<>();
        expectedMessages.add(CommandResultMessages.VIEW_TRANSACTION_EMPTY);

        // Execute the command
        List<String> messages = viewIncomeCommand.execute();

        // Verify the result
        assertEquals(expectedMessages, messages);
    }
}
