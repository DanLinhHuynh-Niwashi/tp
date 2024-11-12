//@@author YukeeHong-unused

//earliest code unused because need to make the project easier to maintain and make differrent part work together easier
import java.util.ArrayList;
import java.util.List;
public class CategoryList {
    private List<Category> categories;
    public CategoryList() {
        categories = new ArrayList<>();
        initializeDefaultCategories();
    }

    private void initializeDefaultCategories() {
        categories.add(new Category("Food"));
        categories.add(new Category("Entertainment"));
        categories.add(new Category("Transport"));
        categories.add(new Category("Utilities"));
        categories.add(new Category("Others"));
    }

    public void addCategory(String categoryName) {
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                System.out.println("Category '" + categoryName + "' already exists!");
                return;
            }
        }
        categories.add(new Category(categoryName));
        System.out.println("Category '" + categoryName + "' added successfully.");
    }

    public void deleteCategory(String categoryName) {
        Category toDelete = null;
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(categoryName)) {
                toDelete = category;
                break;
            }
        }
        if (toDelete != null) {
            categories.remove(toDelete);
            System.out.println("Category '" + categoryName + "' deleted successfully.");
        } else {
            System.out.println("Category '" + categoryName + "' not found!");
        }
    }

    public void listCategories() {
        System.out.println("Available categories:");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getName());
        }
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void interactiveAddCategory(String categoryName) {
        addCategory(categoryName);
    }
}
