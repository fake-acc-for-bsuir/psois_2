package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Rights;
import tech.wcobalt.lab_impl.persistence.FamilyMembersRepository;

public class CreateFirstFamilyAdministratorUseCaseImpl implements CreateFirstFamilyAdministratorUseCase {
    private FamilyMembersRepository familyMembersRepository;

    public CreateFirstFamilyAdministratorUseCaseImpl(FamilyMembersRepository familyMembersRepository) {
        this.familyMembersRepository = familyMembersRepository;
    }

    @Override
    public FamilyMember createFirstFamilyAdministrator(String nickname, String password) {
        FamilyMember fatherFounder = new FamilyMember(nickname, password, 0, Rights.FAMILY_ADMINISTRATOR);

        return familyMembersRepository.createFamilyMember(fatherFounder);
    }
}
