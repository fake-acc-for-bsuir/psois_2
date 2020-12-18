package tech.wcobalt.lab_impl.infrastructure;

import tech.wcobalt.lab_impl.domain.Entry;
import tech.wcobalt.lab_impl.persistence.CDVideosRepository;

public class CDVideosRemoveOperation implements RemoveOperation {
    private CDVideosRepository cdVideosRepository;

    public CDVideosRemoveOperation(CDVideosRepository cdVideosRepository) {
        this.cdVideosRepository = cdVideosRepository;
    }

    @Override
    public void removeEntry(Entry entry) {
        cdVideosRepository.removeCDVideo(entry);
    }
}
