package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.EntriesCollection;
import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface CollectionsCRUDUseCase {
    EntriesCollection createCollection(FamilyMember whoPerforms, EntriesCollection collection) throws RightsViolationException;

    void saveCollection(FamilyMember whoPerforms, EntriesCollection collection) throws RightsViolationException;

    void removeCollection(FamilyMember whoPerforms, int collection) throws RightsViolationException;

    List<EntriesCollection> loadAllCollections(FamilyMember whoPerforms) throws RightsViolationException;

    EntriesCollection loadCollection(FamilyMember whoPerforms, int collection) throws RightsViolationException;

    void addEntryToCollection(FamilyMember whoPerforms, int entry, int collection) throws RightsViolationException;

    void removeEntryFromCollection(FamilyMember whoPerforms, int entry, int collection) throws RightsViolationException;
}
