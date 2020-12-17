package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.persistence.FamilyMembersRepository;

public class ChangeMyPasswordUseCaseImpl implements ChangeMyPasswordUseCase {
    private FamilyMembersRepository familyMembersRepository;

    public ChangeMyPasswordUseCaseImpl(FamilyMembersRepository familyMembersRepository) {
        this.familyMembersRepository = familyMembersRepository;
    }

    @Override
    public void changeMyPassword(FamilyMember familyMember, String oldPassword, String newPassword) throws RightsViolationException {
        if (familyMember.checkPassword(oldPassword)) {
            familyMember.setPassword(newPassword);

            familyMembersRepository.saveFamilyMember(familyMember);
        } else
            throw new RightsViolationException("Passwords don't match");
    }
}
