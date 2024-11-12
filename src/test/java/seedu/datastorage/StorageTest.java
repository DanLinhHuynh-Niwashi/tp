package seedu.datastorage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import seedu.category.Category;
import seedu.transaction.Expense;
import seedu.transaction.Income;
import seedu.transaction.Transaction;

import java.io.File;
import java.nio.file.Path;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class StorageTest {

    @TempDir
    Path tempDir;  // Temporary directory for testing

    private File transactionsFile;
    private File categoriesFile;
    private File budgetsFile;

    @BeforeEach
    void setUp() {
        transactionsFile = tempDir.resolve("transactions.json").toFile();
        categoriesFile = tempDir.resolve("categories.json").toFile();
        budgetsFile = tempDir.resolve("budgets.json").toFile();

        // Assuming `Storage.setPaths` is implemented to change paths for testing
        Storage.setPaths(transactionsFile.getPath(), categoriesFile.getPath(), budgetsFile.getPath());
    }

    @Test
    void testLoadTransactions() {
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Transaction> transactions = Storage.loadTransactions(categories);
        assertNotNull(transactions, "Transactions list should not be null.");
        assertTrue(transactions.isEmpty(), "Transactions list should be empty if no file exists.");
    }

    @Test
    void testSaveTransaction() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Income(100, "Salary", "2024-11-07T00:00:00"));
        transactions.add(new Expense(50, "Groceries", "2024-11-07T00:00:00", new Category("Food")));

        Storage.saveTransaction(transactions);

        assertTrue(transactionsFile.exists(), "Transactions file should be created.");
        assertFalse(transactions.isEmpty(), "Transactions should be saved to file.");
    }

    @Test
    void testLoadCategories() {
        ArrayList<Category> categories = Storage.loadCategories();
        assertNotNull(categories, "Categories list should not be null.");
        assertTrue(categories.isEmpty(), "Categories list should be empty if no file exists.");
    }

    @Test
    void testSaveCategory() {
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(new Category("Food"));
        categories.add(new Category("Transport"));

        Storage.saveCategory(categories);

        assertTrue(categoriesFile.exists(), "Categories file should be created.");
        assertFalse(categories.isEmpty(), "Categories should be saved to file.");
    }

    @Test
    void testLoadBudgets() {
        Map<YearMonth, Double> budgets = Storage.loadBudgets();
        assertNotNull(budgets, "Budgets map should not be null.");
        assertTrue(budgets.isEmpty(), "Budgets map should be empty if no file exists.");
    }

    @Test
    void testSaveBudgets() {
        Map<YearMonth, Double> budgets = new HashMap<>();
        budgets.put(YearMonth.of(2024, 11), 1000.0);
        budgets.put(YearMonth.of(2024, 12), 1200.0);

        Storage.saveBudgets(budgets);

        assertTrue(budgetsFile.exists(), "Budgets file should be created.");
        assertFalse(budgets.isEmpty(), "Budgets should be saved to file.");
    }
}
