package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.*;
import tech.wcobalt.lab_impl.persistence.CDVideosRepository;
import tech.wcobalt.lab_impl.persistence.EntriesRepository;

import java.util.List;

public class CDVideosCRUDUseCaseImpl implements CDVideosCRUDUseCase {
    private EntriesRepository entriesRepository;
    private CDVideosRepository cdVideosRepository;

    public CDVideosCRUDUseCaseImpl(EntriesRepository entriesRepository, CDVideosRepository cdVideosRepository) {
        this.entriesRepository = entriesRepository;
        this.cdVideosRepository = cdVideosRepository;
    }

    @Override
    public CDVideo createCDVideo(FamilyMember whoPerforms, CDVideo cdVideo) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            return cdVideosRepository.createCDVideo(cdVideo);
        } else
            throw new RightsViolationException("Non administrator shall create no CD videos");
    }

    @Override
    public void saveCDVideo(FamilyMember whoPerforms, CDVideo cdVideo) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            cdVideosRepository.saveCDVideo(cdVideo);
        } else
            throw new RightsViolationException("Non administrator shall change no CD video");
    }

    @Override
    public CDVideo loadCDVideo(FamilyMember whoPerforms, int cdVideo) throws RightsViolationException {
        return cdVideosRepository.loadCDVideo(cdVideo);
    }

    @Override
    public List<CDVideo> loadCDVideos(FamilyMember whoPerforms, CDVideoFilter cdVideoFilter) throws RightsViolationException {
        return cdVideosRepository.loadCDVideosByFilter(cdVideoFilter);
    }
}
