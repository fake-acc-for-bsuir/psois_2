package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.CDVideo;
import tech.wcobalt.lab_impl.domain.CDVideoFilter;
import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface CDVideosCRUDUseCase {
    CDVideo createCDVideo(FamilyMember whoPerforms, CDVideo cdVideo) throws RightsViolationException;

    void saveCDVideo(FamilyMember whoPerforms, CDVideo cdVideo) throws RightsViolationException;

    CDVideo loadCDVideo(FamilyMember whoPerforms, int cdVideo) throws RightsViolationException;

    List<CDVideo> loadCDVideos(FamilyMember whoPerforms, CDVideoFilter cdVideoFilter) throws RightsViolationException;
}
