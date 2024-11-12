package seedu.main;

import seedu.budget.BudgetTracker;
import seedu.category.CategoryList;

import seedu.command.AddBudgetCommand;
import seedu.command.AddCategoryCommand;
import seedu.command.AddExpenseCommand;
import seedu.command.AddIncomeCommand;
import seedu.command.ByeCommand;
import seedu.command.Command;
import seedu.command.DeleteCategoryCommand;
import seedu.command.DeleteTransactionCommand;
import seedu.command.HelpCommand;
import seedu.command.HistoryCommand;
import seedu.command.KeywordsSearchCommand;
import seedu.command.UpdateCategoryCommand;
import seedu.command.ViewCategoryCommand;
import seedu.command.ViewExpenseCommand;
import seedu.command.ViewIncomeCommand;
import seedu.command.ViewTotalCommand;
import seedu.command.ViewBudgetCommand;
import seedu.command.DeleteBudgetCommand;
import seedu.datastorage.Storage;

import seedu.transaction.TransactionList;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Main {

    public static final String NAME = "uNivUSaver";
    public static final String HI_MESSAGE = "Hello, %s is willing to help!";
    public static final String INVALID_COMMAND_ERROR_MESSAGE = "Invalid command.";
    private static final Logger logger = Logger.getLogger("Main");

    private static Parser parser; //Parser to parse the commands

    private static UI ui;

    // Singleton CategoryList for use across classes
    private static CategoryList categories; //Category list to store categories

    // Singleton TransactionList for use across classes
    private static TransactionList transactions;

    private static BudgetTracker budgetTracker;

    private static boolean isRunning = true;


    public static void main(String[] args) {
        // Get the root logger and set its level to OFF to disable all logs
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.OFF);

        while (isRunning) {
            run();
        }
    }

    /**
     * Setter for the chatbot's running state.
     *
     * @param isRunning A boolean showing if the chatbot should continue running.
     */
    public static void setRunning(boolean isRunning) {
        Main.isRunning = isRunning;
    }

    /**
     * Getter for the chatbot's running state.
     *
     * @return isRunning A boolean showing if the chatbot should continue running.
     */
    public static boolean getRunning() {
        return isRunning;
    }


    /**
     * Starts the chatbot and enters the command processing loop.
     */
    public static void run() {
        try {
            start();
            runCommandLoop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            logger.log(Level.WARNING, "Unknown error: " + e.getMessage());
        }
    }

    /**
     * Starts the chatbot by printing the logo and greeting messages,
     * and sign up the Command objects.
     */
    public static void start() {
        logger.log(Level.INFO, "Starting uNivUSaver...");
        ui = new UI();
        parser = new Parser();

        categories = new CategoryList();
        categories.setCategories(Storage.loadCategories());
        Storage.saveCategory(categories.getCategories());

        transactions = new TransactionList();
        transactions.setTransactions(Storage.loadTransactions(categories.getCategories()));

        budgetTracker = new BudgetTracker(transactions);
        budgetTracker.setMonthlyBudgets(Storage.loadBudgets());

        setupCommands();

        ui.printMessage(String.format(HI_MESSAGE, NAME));
        printWelcomeMessage();
    }

    public static void printWelcomeMessage() {
        ui.printMessage("Remember to record your spending today so you can track your spending accurately.");
        double todaySpending = transactions.getTodaySpending();

        double monthSpending = transactions.getMonthSpending();
        ui.printMessage("Expenses for " + LocalDateTime.now().getMonth() + ": $" + monthSpending);
        double monthIncome = transactions.getMonthIncome();
        ui.printMessage("Incomes for " + LocalDateTime.now().getMonth() + ": $" + monthIncome);
        String reminder = "Reminder: Please check if your spending is within your budget!";
        ui.printMessage("Today's total spending: $" + todaySpending);
        YearMonth currentMonth = YearMonth.now();
        Double monthlyBudget = budgetTracker.getMonthlyBudget(currentMonth);
        //System.out.println(monthlyBudget);
        if (monthlyBudget != null) {
            double monthlyExpense = budgetTracker.getMonthlyExpense(currentMonth);
            double remaining = monthlyBudget - monthlyExpense;
            ui.printMessage("This month's budget remaining: $" + String.format("%.2f", remaining));
        }
    }




    /**
     * Signs up the Command objects.
     */
    private static void setupCommands() {
        assert categories != null : "Categories should be initialized.";
        assert transactions != null : "Transactions should be initialized.";
        assert budgetTracker != null : "BudgetTracker should be initialized.";

        HelpCommand helpCommand = new HelpCommand();
        parser.registerCommands(helpCommand);

        parser.registerCommands(new AddExpenseCommand(transactions, ui, categories));
        parser.registerCommands(new AddIncomeCommand(transactions));
        parser.registerCommands(new AddCategoryCommand(categories));
        parser.registerCommands(new AddBudgetCommand(budgetTracker));

        parser.registerCommands(new DeleteTransactionCommand(transactions));
        parser.registerCommands(new DeleteCategoryCommand(categories, transactions));
        parser.registerCommands(new DeleteBudgetCommand(budgetTracker));

        parser.registerCommands(new UpdateCategoryCommand(transactions, categories));

        parser.registerCommands(new ViewCategoryCommand(categories));
        parser.registerCommands(new ViewExpenseCommand(transactions));
        parser.registerCommands(new ViewIncomeCommand(transactions));
        parser.registerCommands(new HistoryCommand(transactions));

        parser.registerCommands(new ViewTotalCommand(transactions));

        parser.registerCommands(new KeywordsSearchCommand(transactions));
        parser.registerCommands(new ViewBudgetCommand(budgetTracker));

        parser.registerCommands(new ByeCommand());

        // Set command list for the help command
        logger.log(Level.INFO, "Setting command list for HelpCommand...");
        helpCommand.setCommands(new ArrayList<>(parser.getCommands().values()));
    }

    /**
     * Main command processing loop that retrieves user commands, processes, and displays the results.
     * The loop continues until the application is stopped.
     */
    private static void runCommandLoop() {
        while (isRunning) {

            String commandString = ui.getUserInput();
            String[] commandParts = commandString.split(" ", 2);

            Command command = parser.parseCommand(commandParts[0]);

            if (command == null) {
                List<String> messages = new ArrayList<>();
                messages.add(INVALID_COMMAND_ERROR_MESSAGE);
                ui.showCommandResult(messages);
                continue;
            }

            if (commandParts.length == 2) {
                Map<String, String> arguments = parser.extractArguments(command, commandParts[1]);
                command.setArguments(arguments);
            } else {
                command.setArguments(new HashMap<String, String>());
            }

            List<String> messages = command.execute();
            ui.showCommandResult(messages);
        }
        Storage.saveCategory(categories.getCategories());
        Storage.saveTransaction(transactions.getTransactions());
        Storage.saveBudgets(budgetTracker.getMonthlyBudgets());
    }
}
