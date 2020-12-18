package tech.wcobalt.lab_impl.infrastructure;

import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.persistence.CDAudiosRepository;

public class CDAudiosRemoveOperation implements RemoveOperation {
    private CDAudiosRepository cdAudiosRepository;

    public CDAudiosRemoveOperation(CDAudiosRepository cdAudiosRepository) {
        this.cdAudiosRepository = cdAudiosRepository;
    }

    @Override
    public void removeEntry(Entry entry) {
        cdAudiosRepository.removeCDAudio(entry);
    }
}
