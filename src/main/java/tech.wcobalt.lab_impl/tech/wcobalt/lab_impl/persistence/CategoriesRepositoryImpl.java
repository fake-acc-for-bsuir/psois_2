package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Category;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class CategoriesRepositoryImpl implements CategoriesRepository {
    private List<Category> categories;
    private int nextId = 10000;
    private Category rootCategory;

    public CategoriesRepositoryImpl(List<Category> categories) {
        this.categories = categories;

        rootCategory = createCategory(new Category(-1, -1, ""));
    }

    @Override
    public Category loadCategory(int id) {
        for (Category category : categories) {
            if (category.getId() == id)
                return copyCategory(category);
        }

        return null;
    }

    @Override
    public List<Category> loadChildrenCategories(int category, boolean recursively) {
        List<Category> result = new ArrayList<>();

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(category);
        boolean firstIteration = true;

        while (!queue.isEmpty()) {
            int id = queue.peek();
            queue.remove();

            Category c = loadCategory(id);

            if (!firstIteration)
                result.add(c);

            if (recursively || firstIteration)
                queue.addAll(c.getChildrenCategories());

            firstIteration = false;
        }

        return result;
    }

    @Override
    public Category createCategory(Category category) {
        category.setId(nextId++);

        if (category.getParentCategory() != -1) {
            Category parentCategory = loadCategory(category.getParentCategory());

            parentCategory.getChildrenCategories().add(category.getId());

            saveCategoryBackend(parentCategory);
        }

        categories.add(copyCategory(category));

        return copyCategory(category);
    }

    private void saveCategoryBackend(Category category) {
        categories.removeIf(c -> c.getId() == category.getId());

        categories.add(copyCategory(category));
    }

    @Override
    public void saveCategory(Category category) {
        if (category.getId() != rootCategory.getId())
            saveCategoryBackend(category);
    }

    @Override
    public void removeCategory(Category category) {
        if (category.getId() != rootCategory.getId()) {

            List<Category> children = loadChildrenCategories(category.getId(), false);

            for (Category child : children)
                removeCategory(child);

            categories.removeIf(c -> c.getId() == category.getId());

            Category parent = loadCategory(category.getParentCategory());
            parent.getChildrenCategories().removeIf(i -> i == category.getId());

            saveCategoryBackend(parent);
        }
    }

    @Override
    public Category loadRootCategory() {
        return copyCategory(rootCategory);
    }

    private Category copyCategory(Category category) {
        Category copy = new Category(category.getId(), category.getParentCategory(), category.getName());

        copy.getChildrenCategories().addAll(category.getChildrenCategories());

        return copy;
    }
}
