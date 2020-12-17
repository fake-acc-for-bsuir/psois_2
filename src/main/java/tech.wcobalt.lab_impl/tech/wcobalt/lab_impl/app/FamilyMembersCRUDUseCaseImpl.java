package tech.wcobalt.lab_impl.app;

import tech.wcobalt.lab_impl.domain.FamilyMember;
import tech.wcobalt.lab_impl.domain.Rights;
import tech.wcobalt.lab_impl.persistence.FamilyMembersRepository;

import java.util.ArrayList;
import java.util.List;

public class FamilyMembersCRUDUseCaseImpl implements FamilyMembersCRUDUseCase {
    private FamilyMembersRepository familyMembersRepository;

    public FamilyMembersCRUDUseCaseImpl(FamilyMembersRepository familyMembersRepository) {
        this.familyMembersRepository = familyMembersRepository;
    }

    @Override
    public FamilyMember createFamilyMember(FamilyMember whoPerforms, FamilyMember familyMember) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            return familyMembersRepository.createFamilyMember(familyMember);
        } else
            throw new RightsViolationException("Non administrator shall create no family members");
    }

    @Override
    public void saveFamilyMember(FamilyMember whoPerforms, FamilyMember familyMember) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            familyMembersRepository.saveFamilyMember(familyMember);
        } else
            throw new RightsViolationException("Non administrator shall save no family members");
    }

    @Override
    public void removeFamilyMember(FamilyMember whoPerforms, int familyMemberId) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            familyMembersRepository.removeFamilyMember(
                    new FamilyMember("", "", familyMemberId, Rights.FAMILY_ADMINISTRATOR));
        } else
            throw new RightsViolationException("Non administrator shall remove no family members");
    }

    @Override
    public List<FamilyMember> loadAllFamilyMembers(FamilyMember whoPerforms) throws RightsViolationException {
        if (whoPerforms.getRights() == Rights.FAMILY_ADMINISTRATOR) {
            return familyMembersRepository.loadAllFamilyMembers();
        } else
            throw new RightsViolationException("Non administrator shall not retrieve all family members");
    }

    @Override
    public boolean doesAtLeastOneFamilyAdministratorExist() {
        List<FamilyMember> familyMembers = familyMembersRepository.loadAllFamilyMembers();
        boolean doesAtLeastOneFamilyAdministratorExist = false;

        for (FamilyMember familyMember : familyMembers) {
            if (familyMember.getRights() == Rights.FAMILY_ADMINISTRATOR) {
                doesAtLeastOneFamilyAdministratorExist = true;

                break;
            }
        }

        return doesAtLeastOneFamilyAdministratorExist;
    }

    @Override
    public FamilyMember loadFamilyMember(int familyMember) {
        return familyMembersRepository.loadFamilyMember(familyMember);
    }
}
