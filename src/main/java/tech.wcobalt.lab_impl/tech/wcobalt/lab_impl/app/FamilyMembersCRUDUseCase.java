package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.List;

public interface FamilyMembersCRUDUseCase {
    FamilyMember loadFamilyMember(int familyMember);

    FamilyMember createFamilyMember(FamilyMember whoPerforms, FamilyMember familyMember) throws RightsViolationException;

    void saveFamilyMember(FamilyMember whoPerforms, FamilyMember familyMember) throws RightsViolationException;

    void removeFamilyMember(FamilyMember whoPerforms, int familyMemberId) throws RightsViolationException;

    List<FamilyMember> loadAllFamilyMembers(FamilyMember whoPerforms) throws RightsViolationException;

    boolean doesAtLeastOneFamilyAdministratorExist();
}
