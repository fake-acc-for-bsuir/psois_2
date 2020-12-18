package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Category;
import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Rights;
import tech.wcobalt.lab_impl.persistence.CategoriesRepository;

import java.util.List;

public class CategoriesCRUDUseCaseImpl implements CategoriesCRUDUseCase {
    private CategoriesRepository categoriesRepository;

    public CategoriesCRUDUseCaseImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Category loadCategory(int category) {
        return categoriesRepository.loadCategory(category);
    }

    @Override
    public Category createCategory(FamilyMember whoPerforms, Category category) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            return categoriesRepository.createCategory(category);
        } else
            throw new RightsViolationException("Non-administrator shall add no categories");
    }

    @Override
    public void saveCategory(FamilyMember whoPerforms, Category category) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            categoriesRepository.saveCategory(category);
        } else
            throw new RightsViolationException("Non-administrator shall change no categories");
    }

    @Override
    public void removeCategory(FamilyMember whoPerforms, int category) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            Category categoryObject = categoriesRepository.loadCategory(category);

            categoriesRepository.removeCategory(categoryObject);
        } else
            throw new RightsViolationException("Non-administrator shall remove no categories");
    }

    @Override
    public Category loadRootCategory(FamilyMember whoPerforms) throws RightsViolationException {
        return categoriesRepository.loadRootCategory();
    }

    @Override
    public List<Category> loadChildrenOfCategory(FamilyMember whoPerforms, int category) throws RightsViolationException {
        return categoriesRepository.loadChildrenCategories(category, false);
    }
}
