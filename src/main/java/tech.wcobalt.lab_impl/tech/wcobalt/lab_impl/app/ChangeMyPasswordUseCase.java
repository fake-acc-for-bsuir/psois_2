package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.FamilyMember;

public interface ChangeMyPasswordUseCase {
    void changeMyPassword(FamilyMember familyMember, String oldPassword, String newPassword) throws RightsViolationException;
}
