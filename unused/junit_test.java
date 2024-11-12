//@@author YukeeHong-unused

//unsused because test need to be spllit into specific classes
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
public class JUnitTest {
    @Test
    public void testCategoryCreation() {
        // Arrange
        String categoryName = "Food";
        // Act
        Category category = new Category(categoryName);
        // Assert
        assertEquals(categoryName, category.getName(), "The category name should match the input name.");
    }
    @Test
    public void testGetName() {
        // Arrange
        String categoryName = "Entertainment";
        Category category = new Category(categoryName);
        // Act
        String result = category.getName();
        // Assert
        assertEquals(categoryName, result, "The getName() method should return the correct category name.");
    }
    @Test
    public void testAddCategory() {
        // Arrange
        CategoryList categoryList = new CategoryList();
        String newCategoryName = "Health";
        // Act
        categoryList.addCategory(newCategoryName);
        List<Category> categories = categoryList.getCategories();
        // Assert
        boolean categoryExists = categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(newCategoryName));
        assertTrue(categoryExists, "The new category should be added to the list.");
    }
    @Test
    public void testDeleteCategory() {
        // Arrange
        CategoryList categoryList = new CategoryList();
        String categoryNameToDelete = "Food";
        // Act
        categoryList.deleteCategory(categoryNameToDelete);
        List<Category> categories = categoryList.getCategories();
        // Assert
        boolean categoryExists = categories.stream().anyMatch(c -> c.getName().equalsIgnoreCase(categoryNameToDelete));
        assertFalse(categoryExists, "The category should be deleted from the list.");
    }
    @Test
    public void testListCategories() {
        // Arrange
        CategoryList categoryList = new CategoryList();
        // Act
        List<Category> categories = categoryList.getCategories();
        // Assert
        assertNotNull(categories, "The category list should not be null.");
        assertTrue(categories.size() > 0, "The category list should contain default categories.");
    }
    @Test
    public void testAddTransaction() {
        // Arrange
        TransactionList transactionList = new TransactionList();
        Transaction transaction = new Transaction(100.0, "Groceries", "2024-10-10", new Category("Food"));
        // Act
        transactionList.addTransaction(transaction);
        List<Transaction> transactions = transactionList.getTransactions();
        // Assert
        assertTrue(transactions.contains(transaction), "The transaction should be added to the list.");
    }
    @Test
    public void testDeleteTransaction() {
        // Arrange
        TransactionList transactionList = new TransactionList();
        Transaction transaction = new Transaction(50.0, "Taxi", "2024-10-11", new Category("Transport"));
        transactionList.addTransaction(transaction);
        int initialSize = transactionList.getTransactions().size();
        // Act
        transactionList.deleteTransaction(0);
        int newSize = transactionList.getTransactions().size();
        // Assert
        assertEquals(initialSize - 1, newSize, "The transaction should be removed from the list.");
    }
    @Test
    public void testListTransactions() {
        // Arrange
        TransactionList transactionList = new TransactionList();
        transactionList.addTransaction(new Transaction(20.0, "Coffee", "2024-10-09", new Category("Food")));
        // Act
        List<Transaction> transactions = transactionList.getTransactions();
        // Assert
        assertNotNull(transactions, "The transaction list should not be null.");
        assertTrue(transactions.size() > 0, "The transaction list should contain at least one transaction.");
    }
    @Test
    public void testTransactionCreation() {
        // Arrange
        double amount = 150.0;
        String description = "Dinner";
        String date = "2024-10-12";
        Category category = new Category("Food");
        // Act
        Transaction transaction = new Transaction(amount, description, date, category);
        // Assert
        assertEquals(amount, transaction.getAmount(), "The transaction amount should match the input amount.");
        assertEquals(description, transaction.getDescription(), "The transaction description should match the input description.");
        assertEquals(date, transaction.getDate(), "The transaction date should match the input date.");
        assertEquals(category, transaction.getCategory(), "The transaction category should match the input category.");
    }
    @Test
    public void testTransactionToString() {
        // Arrange
        Transaction transaction = new Transaction(75.0, "Movie", "2024-10-13", new Category("Entertainment"));
        String expectedString = "Transaction [amount=75.0, description=Movie, date=2024-10-13, category=Entertainment]";
        // Act
        String result = transaction.toString();
        // Assert
        assertEquals(expectedString, result, "The toString() method should return the correct transaction details.");
    }
}