package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Category;
import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface CategoriesCRUDUseCase {
    Category loadCategory(int category);
    
    Category createCategory(FamilyMember whoPerforms, Category category) throws RightsViolationException;

    void saveCategory(FamilyMember whoPerforms, Category category) throws RightsViolationException;

    void removeCategory(FamilyMember whoPerforms, int category) throws RightsViolationException;

    Category loadRootCategory(FamilyMember whoPerforms) throws RightsViolationException;

    List<Category> loadChildrenOfCategory(FamilyMember whoPerforms, int category) throws RightsViolationException;
}
