package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.domain.EntryFilter;
import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Rights;
import tech.wcobalt.lab_impl.infrastructure.RemoveOperation;
import tech.wcobalt.lab_impl.infrastructure.RemoveOperationsManager;
import tech.wcobalt.lab_impl.persistence.EntriesRepository;

import java.util.List;

public class EntriesCRUDUseCaseImpl implements EntriesCRUDUseCase {
    private RemoveOperationsManager removeOperationsManager;
    private EntriesRepository entriesRepository;

    public EntriesCRUDUseCaseImpl(RemoveOperationsManager removeOperationsManager, EntriesRepository entriesRepository) {
        this.removeOperationsManager = removeOperationsManager;
        this.entriesRepository = entriesRepository;
    }

    @Override
    public void removeEntry(FamilyMember whoPerforms, int entry) throws RightsViolationException {
        Entry entryObject = entriesRepository.loadEntry(entry);

        RemoveOperation removeOperation = removeOperationsManager.getRemoveOperation(entryObject.getTypeId());

        removeOperation.removeEntry(entryObject);
    }

    @Override
    public List<Entry> searchEntries(FamilyMember whoPerforms, EntryFilter entryFilter) throws RightsViolationException {
        return entriesRepository.loadEntriesByFilter(entryFilter);
    }

    @Override
    public void saveEntry(FamilyMember whoPerforms, Entry entry) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            entriesRepository.saveEntry(entry);
        } else
            throw new RightsViolationException("Non administrator shall change no entries");
    }
}
