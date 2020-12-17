package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.Category;

import java.util.List;

public interface CategoriesRepository {
    Category loadCategory(int id);

    List<Category> loadChildrenCategories(int category, boolean recursively);

    Category createCategory(Category category);

    void saveCategory(Category category);

    void removeCategory(Category category);

    Category loadRootCategory();
}
