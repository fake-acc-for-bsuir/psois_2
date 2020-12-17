package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.CDAudio;
import tech.wcobalt.lab_impl.domain.CDAudioFilter;
import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface CDAudiosCRUDUseCase {
    CDAudio createCDAudio(FamilyMember whoPerforms, CDAudio cdAudio) throws RightsViolationException;

    void saveCDAudio(FamilyMember whoPerforms, CDAudio cdAudio) throws RightsViolationException;

    CDAudio loadCDAudio(FamilyMember whoPerforms, int cdAudio) throws RightsViolationException;

    List<CDAudio> loadCDAudios(FamilyMember whoPerforms, CDAudioFilter cdAudioFilter) throws RightsViolationException;
}
