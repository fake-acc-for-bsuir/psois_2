package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.EntryFilter;
import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface EntriesCRUDUseCase {
    void removeEntry(FamilyMember whoPerforms, int entry) throws RightsViolationException;

    List<Entry> searchEntries(FamilyMember whoPerforms, EntryFilter entryFilter) throws RightsViolationException;

    void saveEntry(FamilyMember whoPerforms, Entry entry) throws RightsViolationException;
}
