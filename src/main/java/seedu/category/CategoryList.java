package seedu.category;

import seedu.datastorage.Storage;
import seedu.exceptions.InvalidCategoryNameException;
import seedu.exceptions.InvalidDescriptionFormatException;
import seedu.message.ErrorMessages;
import seedu.utils.DescriptionUtils;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryList {
    private static final Logger logger = Logger.getLogger("CategoryList");
    private ArrayList<Category> categories;

    public CategoryList() {
        categories = new ArrayList<>();
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
        if (categories.isEmpty()) {
            initializeDefaultCategories();
        }
    }

    public int size() {
        return categories.size();
    }

    // Initialize
    public void initializeDefaultCategories() {
        categories.add(new Category("Food"));
        categories.add(new Category("Entertainment"));
        categories.add(new Category("Transport"));
        categories.add(new Category("Utilities"));
        categories.add(new Category("Others"));
    }

    // Add Category
    public Category addCategory(Category newCategory)
            throws InvalidDescriptionFormatException, InvalidCategoryNameException {

        DescriptionUtils.parseDescription(newCategory.getName());
        if (newCategory.getName().equalsIgnoreCase("skip")
            || newCategory.getName().equalsIgnoreCase("yes")
            || newCategory.getName().equalsIgnoreCase("no")) {
            throw new InvalidCategoryNameException(ErrorMessages.INVALID_CATEGORY_NAME);
        }
        for (Category category : this.categories) {
            if (category.equals(newCategory)) {
                logger.log(Level.INFO, "Category '" + newCategory.getName() + "' already exists!");
                Storage.saveCategory(categories);
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
    public ArrayList<Category> getCategories() {
        return categories;
    }

    public Category findCategory(String categoryName) {
        for (Category category: categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        return null;
    }
}
