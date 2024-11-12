package seedu.datastorage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import seedu.category.Category;
import seedu.exceptions.InvalidAmountFormatException;
import seedu.exceptions.InvalidDateFormatException;
import seedu.main.UI;
import seedu.message.ErrorMessages;
import seedu.message.InfoMessages;
import seedu.transaction.Transaction;
import seedu.transaction.Expense;
import seedu.transaction.Income;
import seedu.utils.AmountUtils;
import seedu.utils.DateTimeUtils;
import seedu.utils.DescriptionUtils;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.logging.Level;


/**
 * Handles the loading and saving of transactions, categories, and budgets to and from JSON files.
 * Provides methods to load and save transactions, categories, and budgets using Gson serialization.
 */
public class Storage {
    private static final Logger logger = Logger.getLogger("Storage");
    private static String transactionsPath = "transactions.json";
    private static String categoriesPath = "categories.json";
    private static String budgetsPath = "budgets.json";

    private static final Gson gson;
    private static final UI ui = new UI();

    static {
        // Register the RuntimeTypeAdapterFactory for polymorphic types
        RuntimeTypeAdapterFactory<Transaction> transactionAdapterFactory = RuntimeTypeAdapterFactory
                .of(Transaction.class, "type")
                .registerSubtype(Expense.class, "expense")
                .registerSubtype(Income.class, "income");

        gson = new GsonBuilder()
                .registerTypeAdapterFactory(transactionAdapterFactory)
                .setPrettyPrinting()
                .create();
    }

    /**
     * Loads transactions from the transactions JSON file.
     * If the file does not exist, creates an empty transactions file.
     *
     * @return A list of transactions loaded from the file, or an empty list if no transactions are found.
     */
    public static ArrayList<Transaction> loadTransactions(ArrayList<Category> categories) {
        File transactionsFile = new File(transactionsPath);

        if (!transactionsFile.exists()) {
            ui.printMessage(InfoMessages.NO_TRANSACTION_FILE_FOUND);
            saveTransaction(new ArrayList<>()); // Create an empty transactions file
        }

        try (FileReader reader = new FileReader(transactionsPath)) {
            Type listType = new TypeToken<ArrayList<Transaction>>() {}.getType();
            ArrayList<Transaction> temp =  new ArrayList<>(gson.fromJson(reader, listType));
            ArrayList<Transaction> transactions = new ArrayList<>();
            for (Transaction t : temp) {
                if (isValidTransaction(t, categories)) {
                    transactions.add(t);
                }
            }
            return transactions;
        } catch (IOException e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_LOADING_TRANSACTIONS, e.getMessage()));
        } catch (com.google.gson.JsonSyntaxException | com.google.gson.JsonIOException e) {
            ui.printMessage(ErrorMessages.INVALID_JSON_TRANSACTIONS);
            saveTransaction(new ArrayList<>());
            return new ArrayList<>();
        } catch (Exception e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_DESERIALIZING_TRANSACTIONS, e.getMessage()));
        }

        return new ArrayList<>();
    }

    /**
     * Validates a transaction to ensure that it contains valid data.
     *
     * @param t The transaction to validate.
     * @return True if the transaction is valid, false otherwise.
     */
    private static boolean isValidTransaction(Transaction t, ArrayList<Category> categories) {
        if (t == null) {
            return false;
        }

        if (t instanceof Expense) {
            String validCategoryName = ((Expense) t).getCategory().getName().trim();
            Category validCategory = new Category(validCategoryName);
            ((Expense) t).setCategory(validCategory);
            if (validCategoryName.isEmpty()) {
                return true;
            }
            if (!categories.contains(validCategory)) {
                logger.log(Level.WARNING, "Can not find the category in the category list: " + t);
                return false;
            }
        }
        try {
            AmountUtils.parseAmount(String.valueOf(t.getAmount()));
        } catch (InvalidAmountFormatException e) {
            logger.log(Level.WARNING, "Transaction has invalid amount: " + t);
            return false;
        }

        if (!DescriptionUtils.isValidDescription(t.getDescription())) {
            logger.log(Level.WARNING, "Transaction has invalid description: " + t);
            return false;
        }

        try {
            LocalDateTime date = DateTimeUtils.parseDateTime(t.getDateTimeString());
            if(date.isAfter(LocalDateTime.now())) {
                logger.log(Level.WARNING, "Transaction is in the future: " + t);
                return false;
            }
        } catch (InvalidDateFormatException e) {
            logger.log(Level.WARNING, "Transaction has an invalid date format: " + t);
            return false;
        }
        return true;
    }

    /**
     * Saves the list of transactions to the transactions JSON file.
     *
     * @param transactions The list of transactions to be saved.
     */
    public static void saveTransaction(ArrayList<Transaction> transactions) {
        try (FileWriter writer = new FileWriter(transactionsPath)) {
            gson.toJson(transactions, writer);
            writer.flush();
        } catch (IOException e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_SAVING_TRANSACTIONS, e.getMessage()));
        }
    }

    /**
     * Loads categories from the categories JSON file.
     * If the file does not exist, creates an empty categories file.
     *
     * @return A list of categories loaded from the file, or an empty list if no categories are found.
     */
    public static ArrayList<Category> loadCategories() {
        File categoriesFile = new File(categoriesPath);
        if (!categoriesFile.exists()) {
            ui.printMessage(InfoMessages.NO_CATEGORY_FILE_FOUND);
            saveCategory(new ArrayList<>()); // Create an empty file if it doesn't exist
        }

        try (FileReader reader = new FileReader(categoriesPath)) {
            Type listType = new TypeToken<ArrayList<Category>>() {}.getType();
            ArrayList<Category> temp =  gson.fromJson(reader, listType);
            ArrayList<Category> categories = new ArrayList<>();
            if (temp!= null) {
                for (Category t : temp) {
                    Category validT = new Category(t.getName().trim());
                    if (isValidCategory(validT, categories)) {
                        categories.add(validT);
                    }
                }
            }
            return categories;
        } catch (IOException e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_LOADING_CATEGORIES, e.getMessage()));
        } catch (Exception e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_DESERIALIZING_CATEGORIES, e.getMessage()));
        }
        return new ArrayList<>();
    }

    private static boolean isValidCategory(Category t, ArrayList<Category> categories) {
        if (!DescriptionUtils.isValidDescription(t.getName())) {
            logger.log(Level.WARNING, "Category has invalid description: " + t);
            return false;
        }
        if (t.getName().equalsIgnoreCase("skip")
                || t.getName().equalsIgnoreCase("yes")
                || t.getName().equalsIgnoreCase("no")) {
            logger.log(Level.WARNING, "Category has invalid description: " + t);
            return false;
        }
        if (categories.contains(t)) {
            logger.log(Level.WARNING, "Duplicated category: " + t);
            return false;
        }
        return true;
    }

    /**
     * Saves the list of categories to the categories JSON file.
     *
     * @param categories The list of categories to be saved.
     */
    public static void saveCategory(ArrayList<Category> categories) {
        try (FileWriter writer = new FileWriter(categoriesPath)) {
            gson.toJson(categories, writer);
            writer.flush();
        } catch (IOException e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_SAVING_CATEGORIES, e.getMessage()));
        }
    }

    /**
     * Loads budgets from the budgets JSON file.
     * If the file does not exist, creates an empty budgets file.
     *
     * @return A map of YearMonth to budget amounts loaded from the file, or an empty map if no budgets are found.
     */
    public static Map<YearMonth, Double> loadBudgets() {
        File budgetsFile = new File(budgetsPath);
        if (!budgetsFile.exists()) {
            ui.printMessage(InfoMessages.NO_BUDGET_FILE_FOUND);
            saveBudgets(new HashMap<>()); // Create an empty file if it doesn't exist
        }

        try (FileReader reader = new FileReader(budgetsPath)) {
            Type mapType = new TypeToken<Map<String, Double>>() {}.getType();
            Map<String, Double> budgetsAsStringMap = gson.fromJson(reader, mapType);
            return budgetsAsStringMap.entrySet().stream()
                    .collect(Collectors.toMap(e -> YearMonth.parse(e.getKey()), Map.Entry::getValue));
        } catch (IOException e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_LOADING_BUDGETS, e.getMessage()));
        } catch (Exception e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_DESERIALIZING_BUDGETS, e.getMessage()));
        }
        return new HashMap<>();
    }

    /**
     * Saves the map of YearMonth to budget amounts to the budgets JSON file.
     *
     * @param budgets The map of YearMonth to budget amounts to be saved.
     */
    public static void saveBudgets(Map<YearMonth, Double> budgets) {
        Map<String, Double> budgetsAsStringMap = budgets.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue));
        try (FileWriter writer = new FileWriter(budgetsPath)) {
            gson.toJson(budgetsAsStringMap, writer);
            writer.flush();
        } catch (IOException e) {
            ui.printMessage(String.format(ErrorMessages.ERROR_SAVING_BUDGETS, e.getMessage()));
        }
    }

    public static void setPaths(String transactionsPath, String categoriesPath, String budgetsPath) {
        Storage.transactionsPath = transactionsPath;
        Storage.categoriesPath = categoriesPath;
        Storage.budgetsPath = budgetsPath;
    }
}
