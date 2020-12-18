package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.CDAudio;
import tech.wcobalt.lab_impl.domain.CDAudioFilter;
import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Rights;
import tech.wcobalt.lab_impl.persistence.CDAudiosRepository;
import tech.wcobalt.lab_impl.persistence.EntriesRepository;

import java.util.List;

public class CDAudiosCRUDUseCaseImpl implements CDAudiosCRUDUseCase {
    private EntriesRepository entriesRepository;
    private CDAudiosRepository cdAudiosRepository;

    public CDAudiosCRUDUseCaseImpl(EntriesRepository entriesRepository, CDAudiosRepository cdAudiosRepository) {
        this.entriesRepository = entriesRepository;
        this.cdAudiosRepository = cdAudiosRepository;
    }

    @Override
    public CDAudio createCDAudio(FamilyMember whoPerforms, CDAudio cdAudio) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            return cdAudiosRepository.createCDAudio(cdAudio);
        } else
            throw new RightsViolationException("Non administrator shall create no CD audios");
    }

    @Override
    public void saveCDAudio(FamilyMember whoPerforms, CDAudio cdAudio) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            cdAudiosRepository.saveCDAudio(cdAudio);
        } else
            throw new RightsViolationException("Non administrator shall change no CD audio");
    }

    @Override
    public CDAudio loadCDAudio(FamilyMember whoPerforms, int cdAudio) throws RightsViolationException {
        return cdAudiosRepository.loadCDAudio(cdAudio);
    }

    @Override
    public List<CDAudio> loadCDAudios(FamilyMember whoPerforms, CDAudioFilter cdAudioFilter) throws RightsViolationException {
        return cdAudiosRepository.loadCDAudiosByFilter(cdAudioFilter);
    }
}
