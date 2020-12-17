package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Category;
import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.EntryFilter;
import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.persistence.CategoriesRepository;
import tech.wcobalt.lab_impl.persistence.EntriesRepository;

import java.util.ArrayList;
import java.util.List;

public class ViewCategoryUseCaseImpl implements ViewCategoryUseCase {
    private CategoriesRepository categoriesRepository;
    private EntriesRepository entriesRepository;

    public ViewCategoryUseCaseImpl(CategoriesRepository categoriesRepository, EntriesRepository entriesRepository) {
        this.categoriesRepository = categoriesRepository;
        this.entriesRepository = entriesRepository;
    }

    @Override
    public List<CategoryElement> loadCategoryElements(FamilyMember whoPerforms, int category, boolean doLoadEntries)
            throws RightsViolationException {
        List<CategoryElement> result = new ArrayList<>();

        List<Category> categories = categoriesRepository.loadChildrenCategories(category, false);

        if (doLoadEntries) {
            EntryFilter entryFilter = new EntryFilter();
            entryFilter.setCategoryFilter(category, false);

            List<Entry> entries = entriesRepository.loadEntriesByFilter(entryFilter);

            for (Entry entry : entries)
                result.add(new CategoryElement(false, null, entry));
        }

        for (Category c : categories)
            result.add(new CategoryElement(true, c, null));

        return result;
    }
}
