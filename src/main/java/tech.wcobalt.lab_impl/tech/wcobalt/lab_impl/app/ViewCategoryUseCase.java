package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface ViewCategoryUseCase {
    List<CategoryElement> loadCategoryElements(FamilyMember whoPerforms, int category, boolean doLoadEntries) throws RightsViolationException;
}
