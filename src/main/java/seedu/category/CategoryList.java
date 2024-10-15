package seedu.category;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryList {
    private static Logger logger = Logger.getLogger("CategoryList");
    private List<Category> categories;

    public CategoryList() {
        categories = new ArrayList<>();
        initializeDefaultCategories();
    }

    // Initialize
    private void initializeDefaultCategories() {
        categories.add(new Category("Food"));
        categories.add(new Category("Entertainment"));
        categories.add(new Category("Transport"));
        categories.add(new Category("Utilities"));
        categories.add(new Category("Others"));
    }

    // Add Category
    public Category addCategory(Category newCategory) {
        for (Category category : this.categories) {
            if (category.getName().equalsIgnoreCase(newCategory.getName())) {
                logger.log(Level.INFO, "Category '" + newCategory.getName() + "' already exists!");
                return null;
            }
        }
        categories.add(newCategory);
        logger.log(Level.INFO, "Category '" + newCategory.getName() + "' added successfully.");
        return newCategory;
    }

    // Delete Category
    public Category deleteCategory(String categoryName) {
        Category toDelete = null;
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                toDelete = category;
                break;
            }
        }
        if (toDelete != null) {
            categories.remove(toDelete);
            logger.log(Level.INFO, "Category '" + categoryName + "' deleted successfully.");
        } else {
            logger.log(Level.INFO, "Category '" + categoryName + "' not found!");
        }
        return toDelete;
    }


    // Get category list
    public List<Category> getCategories() {
        return categories;
    }


    public void interactiveAddCategory(String categoryName) {
        Category newCategory = new Category(categoryName);
        addCategory(newCategory);
    }


}

