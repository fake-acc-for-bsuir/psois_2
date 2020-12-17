package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.FamilyMember;

public interface CreateFirstFamilyAdministratorUseCase {
    FamilyMember createFirstFamilyAdministrator(String nickname, String password);
}
