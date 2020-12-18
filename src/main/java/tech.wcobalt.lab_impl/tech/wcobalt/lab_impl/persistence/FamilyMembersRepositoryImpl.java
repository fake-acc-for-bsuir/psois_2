package tech.wcobalt.lab_impl.persistence;

import tech.wcobalt.lab_impl.domain.FamilyMember;

import java.util.ArrayList;
import java.util.List;

public class FamilyMembersRepositoryImpl implements FamilyMembersRepository {
    private List<FamilyMember> familyMembers;
    private int nextId = 10000;

    public FamilyMembersRepositoryImpl(List<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    @Override
    public FamilyMember loadFamilyMember(int id) {
        for (FamilyMember familyMember : familyMembers) {
            if (familyMember.getId() == id)
                return familyMember;
        }

        return null;
    }

    @Override
    public List<FamilyMember> loadAllFamilyMembers() {
        List<FamilyMember> copy = new ArrayList<>();

        for (FamilyMember familyMember : familyMembers)
            copy.add(copyFamilyMember(familyMember));

        return copy;
    }

    @Override
    public FamilyMember createFamilyMember(FamilyMember familyMember) {
        familyMember.setId(nextId++);

        familyMembers.add(copyFamilyMember(familyMember));

        return copyFamilyMember(familyMember);
    }

    @Override
    public void saveFamilyMember(FamilyMember familyMember) {
        removeFamilyMember(familyMember);

        familyMembers.add(copyFamilyMember(familyMember));
    }

    @Override
    public void removeFamilyMember(FamilyMember familyMember) {
        familyMembers.removeIf(fm -> fm.getId() == familyMember.getId());
    }

    @Override
    public FamilyMember loadFamilyMember(String nickname) {
        for (FamilyMember familyMember : familyMembers) {
            if (familyMember.getNickname().equals(nickname))
                return familyMember;
        }

        return null;
    }

    private FamilyMember copyFamilyMember(FamilyMember familyMember) {
        FamilyMember copy = new FamilyMember(familyMember.getNickname(), "", familyMember.getId(),
                familyMember.getRights());

        copy.setPasswordHash(familyMember.getPasswordHash());

        return copy;
    }
}
